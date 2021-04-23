package com.levifreire.atividadescomplementares

data class Task(
    var id: Int = 0,
    var title: String = "",
    var date: String = "",
    var time: String = "",
    var local: String = "",
    var description: String = "",
    var category: String = "",
    var workload: Int = 0
) {
    override fun toString(): String = title
}