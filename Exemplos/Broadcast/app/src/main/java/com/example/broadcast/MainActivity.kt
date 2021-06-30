package com.example.broadcast

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.broadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val receiver: InternalReceiver = InternalReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSend.setOnClickListener {
            sendImplicitBroadcast()
        }

        binding.btnLocal.setOnClickListener {
            val intent = Intent(ACTION_EVENT)
            LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val filterLocal = IntentFilter(ACTION_EVENT)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, filterLocal)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(receiver)
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

    inner class InternalReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            binding.txtMessage.text = "Ação:\n${intent?.action}"
        }
    }
}