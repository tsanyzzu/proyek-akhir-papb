package com.kelompok4.serena.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CounselingDataManager {
    private const val PREF_NAME = "counseling_data"
    private const val KEY_COUNSELING_LIST = "counseling_list"
    private val gson = Gson()

    // Simpan data konseling
    fun saveCounseling(context: Context, counseling: Counseling): Boolean {
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val counselingList = getAllCounseling(context). toMutableList()

            counselingList.add(counseling)

            val json = gson.toJson(counselingList)
            prefs.edit().putString(KEY_COUNSELING_LIST, json).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Ambil semua data konseling
    fun getAllCounseling(context: Context): List<Counseling> {
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context. MODE_PRIVATE)
            val json = prefs.getString(KEY_COUNSELING_LIST, null) ?: return emptyList()

            val type = object : TypeToken<List<Counseling>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Ambil konseling berdasarkan user email
    fun getCounselingByUser(context: Context, userEmail: String): List<Counseling> {
        return getAllCounseling(context).filter { it.userEmail == userEmail }
    }

    // Ambil konseling yang dijadwalkan (belum selesai)
    fun getScheduledCounseling(context: Context, userEmail: String): List<Counseling> {
        return getCounselingByUser(context, userEmail)
            .filter { it.status == CounselingStatus. SCHEDULED }
            .sortedBy { it.createdAt }
    }

    // Update status konseling
    fun updateCounselingStatus(
        context: Context,
        counselingId: String,
        newStatus: CounselingStatus
    ): Boolean {
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context. MODE_PRIVATE)
            val counselingList = getAllCounseling(context).toMutableList()

            val index = counselingList.indexOfFirst { it.id == counselingId }
            if (index != -1) {
                counselingList[index] = counselingList[index].copy(status = newStatus)
                val json = gson.toJson(counselingList)
                prefs.edit().putString(KEY_COUNSELING_LIST, json).apply()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Hapus konseling
    fun deleteCounseling(context: Context, counselingId: String): Boolean {
        return try {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val counselingList = getAllCounseling(context).toMutableList()

            counselingList.removeAll { it. id == counselingId }

            val json = gson.toJson(counselingList)
            prefs.edit().putString(KEY_COUNSELING_LIST, json).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}