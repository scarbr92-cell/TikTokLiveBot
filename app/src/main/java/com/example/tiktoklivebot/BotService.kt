package com.example.tiktoklivebot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class BotService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var commandText = "!pontos"
    private var intervalSeconds = 60L
    private var elapsed = 0L

    private val ticker = object : Runnable {
        override fun run() {
            elapsed += intervalSeconds
            updateNotification()
            handler.postDelayed(this, intervalSeconds * 1000L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(
                NotificationChannel("live_bot", "Sessão do bot", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        commandText = intent?.getStringExtra("command") ?: "!pontos"
        intervalSeconds = (intent?.getLongExtra("interval", 60L) ?: 60L).coerceAtLeast(30L)
        startForeground(1, buildNotification())
        handler.removeCallbacks(ticker)
        handler.postDelayed(ticker, intervalSeconds * 1000L)
        return START_NOT_STICKY
    }

    private fun buildNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(this, "live_bot") else Notification.Builder(this)
        return builder.setContentTitle("TikTok Live Bot — sessão de teste")
            .setContentText("Integração de chat pendente. Nenhuma mensagem enviada.")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification() {
        getSystemService(NotificationManager::class.java).notify(1, buildNotification())
    }

    override fun onDestroy() {
        handler.removeCallbacks(ticker)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
