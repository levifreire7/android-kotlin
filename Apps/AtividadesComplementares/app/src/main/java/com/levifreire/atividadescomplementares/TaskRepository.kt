package com.levifreire.atividadescomplementares

interface TaskRepository {
    fun save(task: Task)
    fun delete(vararg task: Task)
    fun search(term: String): List<Task>
    fun findById(id: Int): Task?
}