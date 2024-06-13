package com.projectpab.kelompok3.cafefinder.ui.home

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projectpab.kelompok3.cafefinder.R
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object RandomizeManager {
    private const val PREFS_NAME = "DailyRandomize"
    private const val KEY_DATE = "date"
    private const val KEY_NUMBERS = "numbers"

    fun getDate(): String {
        val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US)
        if (dateFormat is SimpleDateFormat) {
            dateFormat.applyPattern("dMMyyyy")
        }
        val date = dateFormat.format(Date())
        return date
    }

    fun getRandomizedIndex(random: Random, maxIndex: Int): IntArray = IntArray(5) {random.nextInt(0,maxIndex)}

    private fun saveArrayToPrefs(prefs: SharedPreferences, key: String, array: IntArray) {
        val gson = Gson()
        val json = gson.toJson(array)
        with(prefs.edit()) {
            putString(key, json)
            apply()
        }
    }

    private fun getArrayFromPrefs(prefs: SharedPreferences, key: String): IntArray? {
        val gson = Gson()
        val json = prefs.getString(key, null) ?: return null
        val type: Type = object : TypeToken<IntArray>() {}.type
        return gson.fromJson(json, type)
    }

    fun getStoredRandomNumbers(context: Context): IntArray {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentDate = getDate()

        val maxIndex = context.resources.getStringArray(R.array.data_cafe).size - 1

        val storedDate = prefs.getString(KEY_DATE, "")
        val storedNumbers = getArrayFromPrefs(prefs, KEY_NUMBERS)

        return if (storedDate == currentDate && storedNumbers != null) {
            storedNumbers
        } else {
            val newRandomNumbers = getRandomizedIndex(Random(getDate().toInt()), maxIndex)
            with(prefs.edit()) {
                putString(KEY_DATE, currentDate)
                apply()
            }
            saveArrayToPrefs(prefs, KEY_NUMBERS, newRandomNumbers)
            newRandomNumbers
        }
    }
}