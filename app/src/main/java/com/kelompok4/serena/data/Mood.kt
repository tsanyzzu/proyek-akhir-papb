package com.kelompok4.serena.data

data class Mood(
    val id: String = System.currentTimeMillis().toString(),
    val moodName: String, // "Gembira", "Sedih", "Netral", "Marah", "Depresi"
    val moodEmoji: String,
    val date: Long = System.currentTimeMillis(),
    val userEmail: String
)

object MoodTypes {
    const val GEMBIRA = "Gembira"
    const val SEDIH = "Sedih"
    const val NETRAL = "Netral"
    const val MARAH = "Marah"
    const val DEPRESI = "Depresi"

    fun getMoodEmoji(moodName: String): String {
        return when (moodName) {
            GEMBIRA -> "😊"
            SEDIH -> "😢"
            NETRAL -> "😐"
            MARAH -> "😠"
            DEPRESI -> "😭"
            else -> "😐"
        }
    }

    fun getMoodColor(moodName: String): String {
        return when (moodName) {
            GEMBIRA -> "#FFD63A" // Secondary500
            SEDIH -> "#4CA8E0" // TertiaryBlue500
            NETRAL -> "#78AF81" // Primary300
            MARAH -> "#EE6161" // TertiaryRed500
            DEPRESI -> "#266030" // Primary700
            else -> "#78AF81"
        }
    }
}

data class MoodStats(
    val totalMoods: Int = 0,
    val gembirCount: Int = 0,
    val sedihCount: Int = 0,
    val netralCount: Int = 0,
    val marahCount: Int = 0,
    val depresiCount: Int = 0
) {
    val gembirPercentage: Float
        get() = if (totalMoods > 0) gembirCount.toFloat() / totalMoods else 0f

    val sedihPercentage: Float
        get() = if (totalMoods > 0) sedihCount.toFloat() / totalMoods else 0f

    val netralPercentage: Float
        get() = if (totalMoods > 0) netralCount.toFloat() / totalMoods else 0f

    val marahPercentage: Float
        get() = if (totalMoods > 0) marahCount.toFloat() / totalMoods else 0f

    val depresiPercentage: Float
        get() = if (totalMoods > 0) depresiCount.toFloat() / totalMoods else 0f

    val dominantMood: String
        get() {
            val counts = mapOf(
                MoodTypes.GEMBIRA to gembirCount,
                MoodTypes.SEDIH to sedihCount,
                MoodTypes.NETRAL to netralCount,
                MoodTypes.MARAH to marahCount,
                MoodTypes.DEPRESI to depresiCount
            )
            return counts.maxByOrNull { it.value }?.key ?: MoodTypes.NETRAL
        }
}