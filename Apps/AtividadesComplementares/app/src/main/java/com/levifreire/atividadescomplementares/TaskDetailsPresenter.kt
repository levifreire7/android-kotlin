package com.levifreire.atividadescomplementares

class TaskDetailsPresenter(
    private val view: TaskDetailsView,
    private val repository: TaskRepository
) {
    fun loadTaskDetails(id: Int) {
        val task = repository.findById(id)
        if (task != null) {
            view.showTaskDetails(task)
        } else {
            view.errorTaskNotFound()
        }
    }
}