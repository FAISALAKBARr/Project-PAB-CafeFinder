package com.projectpab.kelompok3.cafefinder

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object FavoriteManager {
    private const val PREFS_NAME = "favorite_prefs"
    private const val FAVORITES_KEY = "favorites"
    private const val FAVORITE_COUNT_KEY = "favorite_count"

    fun addFavorite(context: Context, cafe: Cafe) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context).toMutableSet()
        favorites.add(cafe.name)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()

        updateFavoriteCount(context, favorites.size)
        Log.d("FavoriteManager", "Added favorite: ${cafe.name}, new count: ${favorites.size}")
    }

    fun removeFavorite(context: Context, cafe: Cafe) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context).toMutableSet()
        favorites.remove(cafe.name)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()

        updateFavoriteCount(context, favorites.size)
        Log.d("FavoriteManager", "Removed favorite: ${cafe.name}, new count: ${favorites.size}")
    }

    fun isFavorite(context: Context, cafe: Cafe): Boolean {
        return getFavorites(context).contains(cafe.name)
    }

    fun getFavorites(context: Context): Set<String> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(FAVORITES_KEY, setOf()) ?: setOf()
    }

    fun getFavoriteCount(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val count = prefs.getInt(FAVORITE_COUNT_KEY, 0)
        Log.d("FavoriteManager", "Retrieved favorite count: $count")
        return count
    }

    fun updateFavoriteCount(context: Context, count: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(FAVORITE_COUNT_KEY, count).apply()
        Log.d("FavoriteManager", "Updated favorite count: $count")
    }
}
