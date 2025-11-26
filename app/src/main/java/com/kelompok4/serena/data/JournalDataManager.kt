package com.kelompok4.serena.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JournalDataManager {
    private const val FILE_NAME = "journals.json"

    private fun getFile(context: Context): File {
        return File(context.filesDir, FILE_NAME)
    }

    private fun readJournals(context: Context): MutableList<Journal> {
        val file = getFile(context)
        if (!file.exists()) return mutableListOf()
        val json = file.readText()
        if (json.isEmpty()) return mutableListOf()
        val type = object : TypeToken<MutableList<Journal>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun writeJournals(context: Context, journals: List<Journal>) {
        val file = getFile(context)
        val json = Gson().toJson(journals)
        file.writeText(json)
    }

    fun addJournal(context: Context, journal: Journal): Boolean {
        return try {
            val journals = readJournals(context)
            journals.add(journal)
            writeJournals(context, journals)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updateJournal(context: Context, journal: Journal): Boolean {
        return try {
            val journals = readJournals(context)
            val index = journals.indexOfFirst { it.id == journal.id }
            if (index != -1) {
                journals[index] = journal
                writeJournals(context, journals)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    fun deleteJournal(context: Context, journalId: String): Boolean {
        return try {
            val journals = readJournals(context)
            journals.removeAll { it.id == journalId }
            writeJournals(context, journals)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getJournalById(context: Context, journalId: String): Journal? {
        return readJournals(context).find { it.id == journalId }
    }

    fun getJournalsByUser(context: Context, userEmail: String): List<Journal> {
        return readJournals(context)
            .filter { it.userEmail == userEmail }
            .sortedByDescending { it.date }
    }

    fun getLatestJournal(context: Context, userEmail: String): Journal? {
        return getJournalsByUser(context, userEmail).firstOrNull()
    }
}