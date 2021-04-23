package com.levifreire.atividadescomplementares

class TaskValidator {

    fun validate(info: Task) = with(info) {
        checkTitle(title) && checkDate(date) && checkTime(time) && checkLocal(local) && checkWorkload(workload)
    }

    private fun checkWorkload(workload: Int) = workload > 0

    private fun checkLocal(local: String) = local.length in 2..30

    private fun checkDate(date: String) = date.length in 8..10

    private fun checkTime(time: String) = time.length in 4..5

    private fun checkTitle(title: String) = title.length in 2..30
}