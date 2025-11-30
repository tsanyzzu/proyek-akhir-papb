package com.kelompok4.serena.data

data class Journal(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val content: String,
    val mood: String, // "Gembira", "Sedih", "Depresi", "Netral", "Marah"
    val moodEmoji: String, // Emoji yang sesuai dengan mood
    val date: Long = System.currentTimeMillis(),
    val userEmail: String
)

data class MoodOption(
    val name: String,
    val emoji: String,
    val description: String
)

object MoodOptions {
    val moods = listOf(
        MoodOption("Gembira", "😊", "Kamu merasa Gembira"),
        MoodOption("Sedih", "😔", "Kamu merasa Sedih"),
        MoodOption("Depresi", "😢", "Kamu merasa Depresi"),
        MoodOption("Netral", "😐", "Kamu merasa Netral"),
        MoodOption("Marah", "😠", "Kamu merasa Marah")
    )

    fun getMoodByName(name: String): MoodOption? {
        return moods.find { it.name == name }
    }
}