package com.levifreire.atividadescomplementares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity(), TaskListFragment.OnTaskClickListener,
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private var lastSearchTerm: String = ""
    private var searchView: SearchView? = null
    private val listFragment: TaskListFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragmentList) as TaskListFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TERM, lastSearchTerm)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastSearchTerm = savedInstanceState.getString(SEARCH_TERM) ?: ""
    }

    override fun onTaskClick(task: Task) {
        if (isTablet()) {
            showDetailsFragment(task.id)
        } else {
            showDetailsActivity(task.id)
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
    }
}