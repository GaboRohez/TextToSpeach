package gaborohez.texttospeach

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
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

    private fun isEnableSpeech(): Boolean {
        var flag = true
        textToSpeech = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                flag = true
                Log.d(TAG, getString(R.string.success))
                textToSpeech!!.language = Locale.US
                textToSpeech!!.setSpeechRate(0.6f)
            } else {
                flag = false
                Log.d(TAG, getString(R.string.no_available))
            }
        }
        return flag
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

    private fun setUpEvents() {

        binding.tilMessage.editText!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.toString().isNotEmpty())
                    binding.tilMessage.error = null
            }
        })

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioEnglish -> textToSpeech!!.language = Locale.US
                R.id.radioSpanish -> textToSpeech!!.language = Locale("ES")
            }
        }

        binding.btnPlay.setOnClickListener {
            val messageToSpeech = binding.tilMessage.editText!!.text.toString().trim()
            if (messageToSpeech.isEmpty()) {
                binding.tilMessage.error = getString(R.string.warning_message)
            }
            textToSpeech!!.speak(messageToSpeech, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }
}