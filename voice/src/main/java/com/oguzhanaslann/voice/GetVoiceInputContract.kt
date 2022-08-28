package com.oguzhanaslann.voice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract

class GetVoiceInputContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            return spokenText
        }

        return null
    }
}
