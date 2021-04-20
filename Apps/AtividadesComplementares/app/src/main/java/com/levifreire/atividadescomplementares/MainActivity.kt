package com.levifreire.atividadescomplementares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity(), TaskListFragment.OnTaskClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onTaskClick(task: Task) {
        showDetailsActivity(task.id)
    }

    private fun showDetailsActivity(id: Int) {
        TaskDetailsActivity.open(this, id)
    }
}