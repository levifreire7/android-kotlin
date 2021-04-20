package com.levifreire.atividadescomplementares

interface TaskListView {
    fun showTasks(tasks: List<Task>)
    fun showTaskDetails(task: Task)
}