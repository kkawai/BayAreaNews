package com.kk.android.bayareanews.common.speech

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import java.util.Locale

class GetSpeech : ActivityResultContract<Unit, String>() {
    @CallSuper
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            .putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            .putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")
    }

    override fun getSynchronousResult(
        context: Context,
        input: Unit
    ): SynchronousResult<String>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return if (resultCode == Activity.RESULT_OK) {
            val result = intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            result?.get(0).toString()
        } else {
            ""
        }
    }
}
