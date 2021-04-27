package com.levifreire.atividadescomplementares

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.ListFragment
import com.google.android.material.snackbar.Snackbar

class TaskListFragment : ListFragment(), TaskListView, AdapterView.OnItemLongClickListener,
    ActionMode.Callback {
    private val presenter = TaskListPresenter(this, MemoryRepository)
    private var actionMode: ActionMode? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        presenter.init()
        listView.onItemLongClickListener = this
    }

    override fun showTasks(tasks: List<Task>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_activated_1, tasks)
        listAdapter = adapter
    }

    override fun showTaskDetails(task: Task) {
        if (activity is OnTaskClickListener) {
            val listener = activity as OnTaskClickListener
            listener.onTaskClick(task)
        }
    }

    override fun showDeleteMode() {
        val appCompatActivity = (activity as AppCompatActivity)
        actionMode = appCompatActivity.startSupportActionMode(this)
        listView.onItemLongClickListener = null
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun hideDeleteMode() {
        listView.onItemLongClickListener = this
        for (i in 0 until listView.count) {
            listView.setItemChecked(i, false)
        }
        listView.post {
            actionMode?.finish()
            listView.choiceMode = ListView.CHOICE_MODE_NONE
        }
    }

    override fun showSelectedTasks(tasks: List<Task>) {
        listView.post {
            for (i in 0 until listView.count) {
                val task = listView.getItemAtPosition(i) as Task
                if (tasks.find { it.id == task.id } != null) {
                    listView.setItemChecked(i, true)
                }
            }
        }
    }

    override fun updateSelectionCountText(count: Int) {
        view?.post {
            actionMode?.title =
                resources.getQuantityString(R.plurals.list_task_selected, count, count)
        }
    }

    override fun showMessageTasksDeleted(count: Int) {
        Snackbar.make(
            listView,
            resources.getQuantityString(R.plurals.message_tasks_deleted, count, count),
            Snackbar.LENGTH_LONG
        ).setAction(R.string.undo) {
            presenter.undoDelete()
        }
            .show()
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val task = l.getItemAtPosition(position) as Task
        presenter.selectTask(task)
    }

    fun search(text: String) {
        presenter.search(text)
    }

    fun clearSearch() {
        presenter.search("")
    }

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val consumed = (actionMode == null)
        if (consumed) {
            val task = parent?.getItemAtPosition(position) as Task
            presenter.showDeleteMode()
            presenter.selectTask(task)
        }
        return consumed
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        activity?.menuInflater?.inflate(R.menu.task_delete_list, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete) {
            presenter.deleteSelected { tasks ->
                if (activity is OnTaskDeletedListener) {
                    (activity as OnTaskDeletedListener).onTasksDeleted(tasks)
                }
            }
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        presenter.hideDeleteMode()
    }

    interface OnTaskDeletedListener {
        fun onTasksDeleted(tasks: List<Task>)
    }
}