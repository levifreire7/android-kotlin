package com.levifreire.atividadescomplementares

class TaskListPresenter(private val view: TaskListView, private val repository: TaskRepository) {
    fun search(term: String){
        view.showTasks(repository.search(term))
    }

    fun showTaskDetails(task: Task){
        view.showTaskDetails(task)
    }
}