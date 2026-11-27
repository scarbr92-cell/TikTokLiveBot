
package com.example.tiktoklivebot

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {

    private lateinit var liveLink: EditText
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 48, 40, 32)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }

        layout.addView(TextView(this).apply {
            text = "TikTok Live Bot"
            textSize = 26f
        })

        layout.addView(TextView(this).apply {
            text = "Cole o link da LIVE:"
            textSize = 16f
        })

        liveLink = EditText(this).apply {
            hint = "https://www.tiktok.com/..."
            setSingleLine(true)
        }

        layout.addView(liveLink)

        layout.addView(Button(this).apply {
            text = "Abrir LIVE no TikTok"
            setOnClickListener {
                val url = validatedUrl() ?: return@setOnClickListener
                openTikTok(url)
            }
        })

        layout.addView(Button(this).apply {
            text = "Compartilhar LIVE"
            setOnClickListener {
                val url = validatedUrl() ?: return@setOnClickListener
                shareLive(url)
            }
        })

        layout.addView(Button(this).apply {
            text = "Copiar link"
            setOnClickListener {
                val url = validatedUrl() ?: return@setOnClickListener
                copyLink(url)
            }
        })

        status = TextView(this).apply {
            text = "Status: pronto"
            textSize = 16f
            setPadding(0, 25, 0, 0)
        }

        layout.addView(status)

        setContentView(ScrollView(this).apply {
            addView(layout)
        })
    }

    private fun validatedUrl(): String? {
        val url = liveLink.text.toString().trim()
        val uri = Uri.parse(url)

        val validHost = uri.host?.let {
            it == "tiktok.com" ||
            it.endsWith(".tiktok.com")
        } ?: false

        if (uri.scheme != "https" || !validHost) {
            liveLink.error = "Cole um link HTTPS válido do TikTok"
            return null
        }

        return url
    }

    private fun openTikTok(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("com.zhiliaoapp.musically")

        try {
            startActivity(intent)
            status.text = "Abrindo LIVE no TikTok..."
        } catch (e: ActivityNotFoundException) {
            try {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                )
                status.text = "Abrindo link no navegador..."
            } catch (ex: Exception) {
                status.text = "Não foi possível abrir o link."
            }
        }
    }

    private fun shareLive(url: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }

        startActivity(
            Intent.createChooser(intent, "Compartilhar LIVE")
        )

        status.text = "Menu de compartilhamento aberto."
    }

    private fun copyLink(url: String) {
        val clipboard =
            getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

        clipboard.setPrimaryClip(
            ClipData.newPlainText("TikTok Live", url)
        )

        status.text = "Link copiado."
    }
}


