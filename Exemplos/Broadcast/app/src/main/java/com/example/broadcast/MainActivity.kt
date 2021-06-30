package com.example.broadcast

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.broadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSend.setOnClickListener {
            sendImplicitBroadcast()
        }

    }

    private fun sendImplicitBroadcast() {
        val intent = Intent(ACTION_EVENT)
        val matches = packageManager.queryBroadcastReceivers(intent, 0)
        for (resolveInfo in matches) {
            val explicit = Intent(intent)
            val componentName = ComponentName(
                resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name
            )
            explicit.component = componentName
            sendBroadcast(explicit)
        }
    }

    companion object {
        private const val ACTION_EVENT = "com.example.broadcast.ACTION_EVENT"
    }
}