package com.kelompok4.serena.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.Calendar

object MoodDataManager {
    private const val FILE_NAME = "moods.json"

    // --- Helper Private untuk File I/O ---

    private fun getFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readMoods(context: Context): MutableList<Mood> {
        val file = getFile(context)
        if (!file.exists()) return mutableListOf()
        return try {
            val json = file.readText()
            if (json.isEmpty()) return mutableListOf()
            val type = object : TypeToken<MutableList<Mood>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    private fun writeMoods(context: Context, moods: List<Mood>) {
        try {
            val file = getFile(context)
            val json = Gson().toJson(moods)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Fungsi Public untuk UI ---

    // Menambah mood baru ke penyimpanan
    fun addMood(context: Context, mood: Mood): Boolean {
        return try {
            val moods = readMoods(context)
            moods.add(mood)
            writeMoods(context, moods)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Mengambil semua mood milik user tertentu (diurutkan dari yang terbaru)
    fun getMoods(context: Context, userEmail: String): List<Mood> {
        return readMoods(context)
            .filter { it.userEmail == userEmail }
            .sortedByDescending { it.date }
    }

    // Mengecek apakah hari ini sudah ada mood yang tersimpan
    fun getTodayMood(context: Context, userEmail: String): Mood? {
        val today = Calendar.getInstance()
        // Reset jam ke 00:00:00 untuk membandingkan hari
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val startOfDay = today.timeInMillis

        val tomorrow = Calendar.getInstance()
        tomorrow.timeInMillis = startOfDay
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = tomorrow.timeInMillis

        return readMoods(context)
            .filter { it.userEmail == userEmail }
            .find { it.date in startOfDay until endOfDay }
    }

    // Mengambil mood dalam periode tertentu (misal: untuk grafik mingguan)
    fun getMoodsForPeriod(
        context: Context,
        userEmail: String,
        startDate: Long,
        endDate: Long
    ): List<Mood> {
        return readMoods(context)
            .filter {
                it.userEmail == userEmail &&
                        it.date >= startDate &&
                        it.date <= endDate
            }
            .sortedBy { it.date }
    }

    // Menghitung statistik mood (jumlah gembira, sedih, dll)
    fun getMoodStats(context: Context, userEmail: String, days: Int = 7): MoodStats {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -days)
        val startDate = calendar.timeInMillis

        val moods = getMoodsForPeriod(context, userEmail, startDate, System.currentTimeMillis())

        var gembirCount = 0
        var sedihCount = 0
        var netralCount = 0
        var marahCount = 0
        var depresiCount = 0

        moods.forEach { mood ->
            when (mood.moodName) {
                MoodTypes.GEMBIRA -> gembirCount++
                MoodTypes.SEDIH -> sedihCount++
                MoodTypes.NETRAL -> netralCount++
                MoodTypes.MARAH -> marahCount++
                MoodTypes.DEPRESI -> depresiCount++
            }
        }

        return MoodStats(
            totalMoods = moods.size,
            gembirCount = gembirCount,
            sedihCount = sedihCount,
            netralCount = netralCount,
            marahCount = marahCount,
            depresiCount = depresiCount
        )
    }

    // Tambahkan di dalam object MoodDataManager
    fun clearAllMoods(context: Context): Boolean {
        val file = getFile(context)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}