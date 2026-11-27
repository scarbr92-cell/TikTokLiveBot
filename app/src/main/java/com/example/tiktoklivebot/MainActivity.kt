package com.example.tiktoklivebot

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {

    private lateinit var liveLink: EditText
    private lateinit var interval: EditText
    private lateinit var status: TextView
    private lateinit var startButton: Button

    private val handler = Handler(Looper.getMainLooper())
    private var isRepeating = false

    private val copyTask = object : Runnable {
        override fun run() {
            if (!isRepeating) return

            val url = liveLink.text.toString().trim()
            copyToClipboard(url)

            val seconds = interval.text.toString().toLongOrNull() ?: 60L
            status.text = "Repetição ativa: link copiado. Próxima cópia em ${seconds}s."
            handler.postDelayed(this, seconds * 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 48, 40, 32)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }

        layout.addView(TextView(this).apply {
            text = "TikTok Live — copiador de link"
            textSize = 24f
        })

        layout.addView(TextView(this).apply {
            text = "Cole o link da mesma live:"
            textSize = 16f
        })

        liveLink = EditText(this).apply {
            hint = "https://www.tiktok.com/..."
            setSingleLine(true)
        }
        layout.addView(liveLink)

        layout.addView(TextView(this).apply {
            text = "Intervalo entre cópias (segundos; mínimo 30):"
            textSize = 16f
        })

        interval = EditText(this).apply {
            hint = "60"
            setText("60")
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        layout.addView(interval)

        layout.addView(Button(this).apply {
            text = "Copiar uma vez"
            setOnClickListener {
                val url = validatedUrl() ?: return@setOnClickListener
                copyToClipboard(url)
                status.text = "Link copiado."
            }
        })

        startButton = Button(this).apply {
            text = "Iniciar repetição"
            setOnClickListener { startRepeating() }
        }
        layout.addView(startButton)

        layout.addView(Button(this).apply {
            text = "Parar repetição"
            setOnClickListener { stopRepeating() }
        })

        status = TextView(this).apply {
            text = "Status: parado"
            textSize = 16f
            setPadding(0, 25, 0, 0)
        }
        layout.addView(status)

        layout.addView(TextView(this).apply {
            text = "A repetição funciona enquanto esta tela estiver aberta. O Android pode limitar o acesso à área de transferência em segundo plano; por isso, este app não promete copiar com a tela fechada. Não envia mensagens nem curtidas."
            textSize = 13f
            setPadding(0, 24, 0, 0)
        })

        setContentView(ScrollView(this).apply { addView(layout) })
    }

    private fun validatedUrl(): String? {
        val url = liveLink.text.toString().trim()
        if (!url.startsWith("https://www.tiktok.com/") &&
            !url.startsWith("https://vt.tiktok.com/")) {
            liveLink.error = "Cole um link válido do TikTok"
            return null
        }
        return url
    }

    private fun copyToClipboard(url: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("TikTok Live", url))
    }

    private fun startRepeating() {
        val url = validatedUrl() ?: return
        val seconds = interval.text.toString().toLongOrNull()

        if (seconds == null || seconds < 30) {
            interval.error = "Use pelo menos 30 segundos"
            return
        }

        if (isRepeating) return

        isRepeating = true
        startButton.isEnabled = false
        status.text = "Repetição iniciada."
        handler.removeCallbacks(copyTask)
        copyToClipboard(url)
        status.text = "Repetição ativa: link copiado. Próxima cópia em ${seconds}s."
        handler.postDelayed(copyTask, seconds * 1000L)
    }

    private fun stopRepeating() {
        isRepeating = false
        handler.removeCallbacks(copyTask)
        if (::startButton.isInitialized) startButton.isEnabled = true
        status.text = "Status: parado"
    }

    override fun onDestroy() {
        stopRepeating()
        super.onDestroy()
    }
}
