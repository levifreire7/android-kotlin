package com.levifreire.atividadescomplementares

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TaskFormActivity : AppCompatActivity() {
    private val hotelId: Int by lazy { intent.getIntExtra(TASK_ID, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        sendInformation(hotelId)
    }

    private fun sendInformation(id: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentTaskForm)
        fragment?.arguments = Bundle().apply {
            putInt(TASK_ID, id)
        }
    }

    companion object{
        private const val TASK_ID = "task_id"

        fun open(context: Context, taskId: Int = 0) {
            context.startActivity(Intent(context, TaskFormActivity::class.java).apply {
                putExtra(TASK_ID, taskId)
            })
        }
    }
}