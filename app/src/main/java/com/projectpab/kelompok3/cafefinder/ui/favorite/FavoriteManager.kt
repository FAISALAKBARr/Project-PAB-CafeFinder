package com.projectpab.kelompok3.cafefinder

import android.content.Context
import android.content.SharedPreferences

object FavoriteManager {
    private const val PREFS_NAME = "favorite_prefs"
    private const val FAVORITES_KEY = "favorites"

    fun addFavorite(context: Context, cafe: Cafe) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context).toMutableSet()
        favorites.add(cafe.name)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun removeFavorite(context: Context, cafe: Cafe) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context).toMutableSet()
        favorites.remove(cafe.name)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun isFavorite(context: Context, cafe: Cafe): Boolean {
        return getFavorites(context).contains(cafe.name)
    }

    fun getFavorites(context: Context): Set<String> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(FAVORITES_KEY, setOf()) ?: setOf()
    }
}
