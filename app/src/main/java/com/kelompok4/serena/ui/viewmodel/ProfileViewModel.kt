package com.kelompok4.serena.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kelompok4.serena.data.User
import com.kelompok4.serena.data.UserDataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
<<<<<<< Updated upstream
=======
import kotlinx.coroutines.tasks.await
import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
>>>>>>> Stashed changes

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(context: Context, email: String) {
        viewModelScope.launch {
            _user.value = UserDataManager.getUserByEmail(context, email)
        }
    }

    fun updateUser(context: Context, updated: User) {
        viewModelScope.launch {
            UserDataManager.updateUser(context, updated)
            _user.value = updated
        }
    }
<<<<<<< Updated upstream
=======

    /**
     * Update field-field user di Firestore.
     * Digunakan untuk perubahan non-auth (mis. fullName, username, profilePhotoUrl).
     */
    fun updateUserFields(newData: Map<String, Any>, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                val docId = currentDocId ?: auth.currentUser?.uid
                if (docId == null) {
                    onComplete(false, "User ID tidak tersedia untuk update.")
                    return@launch
                }

                val finalData = newData + ("updatedAt" to Timestamp.now())
                db.collection("users").document(docId).update(finalData).await()

                // reload user setelah update
                val updatedDoc = db.collection("users").document(docId).get().await()
                _user.value = updatedDoc.toObject(User::class.java)

                onComplete(true, null)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false, e.message)
            }
        }
    }

    /**
     * Update Email:
     * - Coba update di FirebaseAuth terlebih dahulu.
     * - Jika perlu reauth, dan password diberikan, akan coba reauthenticate lalu retry update.
     * - Setelah Auth success, update field "email" di Firestore.
     *
     * @param newEmail alamat email baru
     * @param currentPassword optional; dibutuhkan jika Firebase menolak karena sesi lama
     * @param onComplete callback (success:Boolean, message:String?)
     */
    fun updateEmail(newEmail: String, currentPassword: String? = null, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    onComplete(false, "Tidak ada pengguna yang sedang login.")
                    return@launch
                }

                // 1) Coba update email di FirebaseAuth langsung
                try {
                    currentUser.updateEmail(newEmail).await()
                } catch (authEx: Exception) {
                    // Jika membutuhkan re-authentication, Firebase melempar FirebaseAuthRecentLoginRequiredException
                    if (authEx is FirebaseAuthRecentLoginRequiredException) {
                        if (currentPassword.isNullOrBlank()) {
                            onComplete(false, "Aksi ini memerlukan verifikasi ulang. Masukkan kata sandi Anda atau login ulang.")
                            return@launch
                        } else {
                            // lakukan reauthenticate dengan credential email/password
                            try {
                                val currentEmail = currentUser.email ?: ""
                                val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)
                                currentUser.reauthenticate(credential).await()
                                // setelah reauth sukses, coba update email lagi
                                currentUser.updateEmail(newEmail).await()
                            } catch (reauthEx: Exception) {
                                reauthEx.printStackTrace()
                                onComplete(false, "Verifikasi ulang gagal: ${reauthEx.message}")
                                return@launch
                            }
                        }
                    } else {
                        // error lain saat update email
                        authEx.printStackTrace()
                        onComplete(false, "Gagal mengubah email: ${authEx.message}")
                        return@launch
                    }
                }

                // 2) Jika update di Auth berhasil, update ke Firestore
                val docId = currentDocId ?: currentUser.uid
                val finalData = mapOf(
                    "email" to newEmail,
                    "updatedAt" to Timestamp.now()
                )
                db.collection("users").document(docId).update(finalData).await()

                // 3) reload user state dari Firestore
                val updatedDoc = db.collection("users").document(docId).get().await()
                _user.value = updatedDoc.toObject(User::class.java)

                onComplete(true, null)

            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false, e.message)
            }
        }
    }

    fun uploadProfilePhoto(uri: Uri, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser
                if (currentUser == null) {
                    onComplete(false, "Tidak ada pengguna yang login.")
                    return@launch
                }

                val uid = currentUser.uid
                val storageRef = FirebaseStorage.getInstance()
                    .reference
                    .child("profile_photos/${uid}_${System.currentTimeMillis()}.jpg")

                // upload file
                storageRef.putFile(uri).await()

                // ambil download url
                val downloadUrl = storageRef.downloadUrl.await().toString()

                // update photoURL di FirebaseAuth (opsional tapi bagus sinkronisasi)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(downloadUrl))
                    .build()
                currentUser.updateProfile(profileUpdates).await()

                // update Firestore user doc
                val docId = currentDocId ?: uid
                val finalData = mapOf(
                    "profilePhotoUrl" to downloadUrl,
                    "profilePhotoUpdatedAt" to Timestamp.now(),
                    "updatedAt" to Timestamp.now()
                )
                db.collection("users").document(docId).update(finalData).await()

                // reload local state
                val updatedDoc = db.collection("users").document(docId).get().await()
                _user.value = updatedDoc.toObject(User::class.java)

                onComplete(true, null)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false, e.message)
            }
        }
    }

    // Convenience helpers
    fun updateFullName(newFullName: String, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        updateUserFields(mapOf("fullName" to newFullName), onComplete)
    }

    fun updateUsername(newUsername: String, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        updateUserFields(mapOf("username" to newUsername), onComplete)
    }

    fun updateProfilePhotoUrl(newUrl: String, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        updateUserFields(mapOf("profilePhotoUrl" to newUrl, "profilePhotoUpdatedAt" to Timestamp.now()), onComplete)
    }
>>>>>>> Stashed changes
}