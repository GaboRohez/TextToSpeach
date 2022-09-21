package gaborohez.texttospeach

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import gaborohez.texttospeach.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (!isEnableSpeech()) {
            showDialog()
        }

        setUpEvents()
    }

    private fun setUpEvents() {
        binding.btnPlay.setOnClickListener {
            val messageToSpeech = binding.tilMessage.editText!!.text.toString().trim()
            textToSpeech!!.speak(messageToSpeech, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.error_message))
            .setPositiveButton(
                getString(R.string.accept)
            ) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
        builder.create()
        builder.show()
    }

    private fun isEnableSpeech(): Boolean {
        var flag = true
        textToSpeech = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                flag = true
                Log.d(TAG, getString(R.string.success))
                textToSpeech!!.language = Locale.US
            } else {
                flag = false
                Log.d(TAG, getString(R.string.no_available))
            }
        }
        return flag
    }
}