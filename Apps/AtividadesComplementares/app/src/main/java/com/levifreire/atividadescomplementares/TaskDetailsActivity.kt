package com.levifreire.atividadescomplementares

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class TaskDetailsActivity : AppCompatActivity() {
    private val taskID: Int by lazy { intent.getIntExtra(TASK_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        if (savedInstanceState == null) {
            showTaskDetailsFragment()
        }
    }

    private fun showTaskDetailsFragment() {
        val fragment = TaskDetailsFragment.newInstance(taskID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, TaskDetailsFragment.TAG_DETAILS)
            .commit()
    }

    companion object {
        private const val TASK_ID = "task_id"
        fun open(context: Context, taskId: Int) {
            context.startActivity(Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra(TASK_ID, taskId)
            })
        }
    }
}