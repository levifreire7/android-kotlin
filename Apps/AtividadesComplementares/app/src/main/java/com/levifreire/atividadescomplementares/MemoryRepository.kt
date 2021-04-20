package com.levifreire.atividadescomplementares

object MemoryRepository : TaskRepository {
    private val tasksList = mutableListOf<Task>()
    private var nextId = 1

    init {
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Reunião Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Idioma Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Atividade Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Exercicio Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Rotina Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Apresentação Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Seminário Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021 14:30",
                "Auditório",
                "-",
                "Extra",
                2
            )
        )
    }

    override fun save(task: Task) {
        task.id = nextId++
        tasksList.add(task)
    }

    override fun update(task: Task): Boolean {
        val index = tasksList.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasksList[index] = task
            return true
        }
        return false
    }

    override fun delete(vararg task: Task) {
        tasksList.removeAll(task)
    }

    override fun search(term: String): List<Task> {
        return if (term.isEmpty()) {
            tasksList
        } else {
            tasksList.filter { it.title.toUpperCase().contains(term.toUpperCase()) }
        }
    }

    override fun findById(id: Int): Task? {
        return tasksList.find { it.id == id }
    }
}