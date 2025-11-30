package com.kelompok4.serena.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import com.kelompok4.serena.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private var currentDocId: String? = null

    /** Load user berdasarkan email (atau gunakan uid jika tersedia) */
    fun loadUserByEmail(email: String) {
        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("users")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val doc = querySnapshot.documents[0]
                    currentDocId = doc.id
                    _user.value = doc.toObject(User::class.java)
                } else {
                    currentDocId = null
                    _user.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                currentDocId = null
                _user.value = null
            }
        }
    }

    /** Load user berdasarkan uid (document id) */
    fun loadUserByUid(uid: String) {
        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(uid).get().await()
                if (doc.exists()) {
                    currentDocId = doc.id
                    _user.value = doc.toObject(User::class.java)
                } else {
                    currentDocId = null
                    _user.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                currentDocId = null
                _user.value = null
            }
        }
    }

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
}