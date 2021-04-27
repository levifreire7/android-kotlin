package com.levifreire.atividadescomplementares

object MemoryRepository : TaskRepository {
    private val tasksList = mutableListOf<Task>()
    private var nextId = 1

    init {
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Cultura",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Meio Ambiente",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Lazer",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Cultura",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Meio Ambiente",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Lazer",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Inteligencia Artifical",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Cultura",
                2
            )
        )
        save(
            Task(
                0,
                "Palestra Meio Ambiente",
                "21/03/2021",
                "14:30",
                "Auditório",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sed urna luctus magna volutpat dictum. Pellentesque turpis lectus, placerat ut.",
                "Lazer",
                2
            )
        )
    }

    override fun save(task: Task) {
        val index = tasksList.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasksList[index] = task
        } else {
            task.id = nextId++
            tasksList.add(task)
        }
    }

    override fun delete(vararg task: Task) {
        tasksList.removeAll(task)
    }

    override fun search(term: String): List<Task> {
        val result = if (term.isEmpty()) {
            tasksList
        } else {
            tasksList.filter { it.title.toUpperCase().contains(term.toUpperCase()) }
        }
        return result.sortedBy { it.title }
    }

    override fun findById(id: Int): Task? {
        return tasksList.find { it.id == id }
    }
}