package com.levifreire.atividadescomplementares

data class Task(
    var id: Int,
    var title: String,
    var dateTime: String,
    var local: String,
    var description: String,
    var category: String,
    var workload: Int
) {
    override fun toString(): String = title
}