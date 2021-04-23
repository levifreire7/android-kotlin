package com.levifreire.atividadescomplementares

import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

class TaskFormPresenter(private val view: TaskFormView, private val repository: TaskRepository) {
    private val validator = TaskValidator()

    fun loadTask(id: Int) {
        val task = repository.findById(id)
        if (task != null) {
            view.showTask(task)
        }
    }

    fun saveTask(task: Task): Boolean {
        return if (validator.validate(task)) {
            try {
                repository.save(task)
                true
            } catch (e: Exception) {
                view.errorSaveTask()
                false
            }
        } else {
            view.errorInvalidTask()
            false
        }
    }

    fun initFields() {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val hourFormat = SimpleDateFormat("HH:mm", Locale.US)
        val dateValue = dateFormat.format(date)
        val hourValue = hourFormat.format(date)

        view.initFields(dateValue, hourValue)
    }
}