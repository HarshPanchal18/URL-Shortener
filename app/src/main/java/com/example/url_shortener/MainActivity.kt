package com.example.url_shortener

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.url_shortener.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val urlShortenerService = UrlShortenerService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shortenBtn.setOnClickListener {
            val longUrl = binding.urlEditText.text.toString()
            if (longUrl.isNotEmpty())
                shortenUrl(longUrl)
        }

        binding.copyBtn.setOnClickListener {
            if(binding.resultTextView.text.isNotEmpty()) {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("shortedUrl", binding.resultTextView.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun shortenUrl(longUrl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val shortUrl = urlShortenerService.shortenThisUrl(longUrl)

            withContext(Dispatchers.Main) {
                binding.resultTextView.text = shortUrl
                binding.copyBtn.visibility = View.VISIBLE
            }
        }
    }
}
