package com.levifreire.atividadescomplementares

interface TaskDetailsView {
    fun showTaskDetails(task: Task)
    fun errorTaskNotFound()
}