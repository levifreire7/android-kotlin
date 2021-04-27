package com.levifreire.atividadescomplementares

class TaskListPresenter(private val view: TaskListView, private val repository: TaskRepository) {
    private var lastTerm = ""
    private var inDeleteMode = false
    private val selectedItems = mutableListOf<Task>()
    private val deletedItems = mutableListOf<Task>()

    fun init() {
        if (inDeleteMode) {
            showDeleteMode()
            view.updateSelectionCountText(selectedItems.size)
            view.showSelectedTasks(selectedItems)
        } else {
            refresh()
        }

    }

    fun search(term: String) {
        lastTerm = term
        view.showTasks(repository.search(term))
    }

//    fun showTaskDetails(task: Task) {
//        view.showTaskDetails(task)
//    }

    fun selectTask(task: Task) {
        if (inDeleteMode) {
            toggleTaskSelected(task)
            if (selectedItems.size == 0) {
                view.hideDeleteMode()
            } else {
                view.updateSelectionCountText(selectedItems.size)
                view.showSelectedTasks(selectedItems)
            }
        } else {
            view.showTaskDetails(task)
        }
    }

    private fun toggleTaskSelected(task: Task) {
        val existing = selectedItems.find { it.id == task.id }
        if (existing == null) {
            selectedItems.add(task)
        } else {
            selectedItems.removeAll { it.id == task.id }
        }
    }

    fun showDeleteMode() {
        inDeleteMode = true
        view.showDeleteMode()
    }

    fun hideDeleteMode() {
        inDeleteMode = false
        selectedItems.clear()
        view.hideDeleteMode()
    }

    private fun refresh() {
        search(lastTerm)
    }

    fun deleteSelected(callback: (List<Task>) -> Unit) {
        repository.delete(*selectedItems.toTypedArray())
        deletedItems.clear()
        deletedItems.addAll(selectedItems)
        refresh()
        callback(selectedItems)
        hideDeleteMode()
        view.showMessageTasksDeleted(deletedItems.size)
    }

    fun undoDelete() {
        if (deletedItems.isNotEmpty()) {
            for (task in deletedItems) {
                repository.save(task)
            }
            search(lastTerm)
        }
    }
}