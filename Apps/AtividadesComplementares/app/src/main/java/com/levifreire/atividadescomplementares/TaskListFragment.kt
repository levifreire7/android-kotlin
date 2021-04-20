package com.levifreire.atividadescomplementares

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment

class TaskListFragment : ListFragment(), TaskListView {
    private val presenter = TaskListPresenter(this, MemoryRepository)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.search("")
    }

    override fun showTasks(tasks: List<Task>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tasks)
        listAdapter = adapter
    }

    override fun showTaskDetails(task: Task) {
        if (activity is OnTaskClickListener) {
            val listener = activity as OnTaskClickListener
            listener.onTaskClick(task)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val task = l.getItemAtPosition(position) as Task
        presenter.showTaskDetails(task)
    }

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
    }
}