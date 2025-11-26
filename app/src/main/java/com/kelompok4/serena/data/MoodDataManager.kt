package com.kelompok4.serena.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*

object MoodDataManager {
    private const val FILE_NAME = "moods.json"

    private fun getFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readMoods(context: Context): MutableList<Mood> {
        val file = getFile(context)
        if (!file.exists()) return mutableListOf()
        val json = file.readText()
        if (json.isEmpty()) return mutableListOf()
        val type = object : TypeToken<MutableList<Mood>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun writeMoods(context: Context, moods: List<Mood>) {
        val file = getFile(context)
        val json = Gson().toJson(moods)
        file.writeText(json)
    }

    fun addMood(context: Context, mood: Mood): Boolean {
        return try {
            val moods = readMoods(context)
            moods.add(mood)
            writeMoods(context, moods)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getMoodsByUser(context: Context, userEmail: String): List<Mood> {
        return readMoods(context)
            .filter { it.userEmail == userEmail }
            .sortedByDescending { it.date }
    }

    fun getTodayMood(context: Context, userEmail: String): Mood? {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val tomorrow = Calendar.getInstance()
        tomorrow.timeInMillis = today.timeInMillis
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)

        return readMoods(context)
            .filter { it.userEmail == userEmail }
            .find { it.date >= today.timeInMillis && it.date < tomorrow.timeInMillis }
    }

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

    fun getWeeklyMoodData(context: Context, userEmail: String): Map<String, List<Mood>> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -6)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val result = mutableMapOf<String, List<Mood>>()
        val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

        for (i in 0..6) {
            val dayStart = calendar.timeInMillis
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val dayEnd = calendar.timeInMillis

            val dayMoods = getMoodsForPeriod(context, userEmail, dayStart, dayEnd)
            result[days[i]] = dayMoods

            calendar.timeInMillis = dayEnd
        }

        return result
    }
}