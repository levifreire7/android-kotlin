package com.levifreire.atividadescomplementares

interface TaskListView {
    fun showTasks(tasks: List<Task>)
    fun showTaskDetails(task: Task)
    fun showDeleteMode()
    fun hideDeleteMode()
    fun showSelectedTasks(tasks: List<Task>)
    fun updateSelectionCountText(count: Int)
}