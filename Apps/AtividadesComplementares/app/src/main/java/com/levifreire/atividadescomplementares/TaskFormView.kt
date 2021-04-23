package com.levifreire.atividadescomplementares

interface TaskFormView {
    fun showTask(task: Task)
    fun errorInvalidTask()
    fun errorSaveTask()
    fun initFields(date: String, hour: String)
}