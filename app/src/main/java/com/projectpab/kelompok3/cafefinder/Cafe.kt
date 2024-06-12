package com.projectpab.kelompok3.cafefinder

data class Cafe(
    val name: String,
    val description: String,
    val imageId: Int,
    val category: String,
    val recommendation: String,
    var rating: Float
)
