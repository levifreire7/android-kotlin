package com.levifreire.atividadescomplementares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.levifreire.atividadescomplementares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskListFragment.OnTaskClickListener,
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,
    TaskListFragment.OnTaskDeletedListener {

    private var lastSearchTerm: String = ""
    private var searchView: SearchView? = null
    private val listFragment: TaskListFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragmentList) as TaskListFragment }
    private var taskIdSelected: Int = -1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabAdd.setOnClickListener {
            listFragment.hideDeleteMode()
            TaskFormActivity.open(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TERM, lastSearchTerm)
        outState.putInt(TASK_ID_SELECTED, taskIdSelected)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastSearchTerm = savedInstanceState.getString(SEARCH_TERM) ?: ""
        taskIdSelected = savedInstanceState.getInt(TASK_ID_SELECTED) ?: 0
    }

    override fun onTaskClick(task: Task) {
        if (isTablet()) {
            taskIdSelected = task.id
            showDetailsFragment(task.id)
        } else {
            showDetailsActivity(task.id)
        }
    }

    override fun onTasksDeleted(tasks: List<Task>) {
        if (tasks.find { it.id == taskIdSelected } != null) {
            val fragment = supportFragmentManager.findFragmentByTag(TaskDetailsFragment.TAG_DETAILS)
            if (fragment != null) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }

    private fun showDetailsFragment(taskId: Int) {
        searchView?.setOnQueryTextListener(null)

        val fragment = TaskDetailsFragment.newInstance(taskId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, TaskDetailsFragment.TAG_DETAILS)
            .commit()
    }

    private fun isTablet() = resources.getBoolean(R.bool.tablet)

    private fun showDetailsActivity(id: Int) {
        TaskDetailsActivity.open(this, id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if (lastSearchTerm.isNotEmpty()) {
            Handler().post {
                val query = lastSearchTerm
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info -> AboutDialogFragment().show(supportFragmentManager, "sobre")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        lastSearchTerm = newText ?: ""
        listFragment.search(lastSearchTerm)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        lastSearchTerm = ""
        listFragment.clearSearch()
        return true
    }

    companion object {
        const val SEARCH_TERM = "lastSearch"
        const val TASK_ID_SELECTED = "lastSelectedId"
    }
}