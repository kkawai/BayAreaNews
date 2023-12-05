package com.kk.android.bayareanews.common

import android.content.Context
import android.content.Intent

object ShareUtil {
    fun shareUrl(context: Context, url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        //sendIntent.putExtra(Intent.EXTRA_SUBJECT, rss.title) //not as many good share options besides email
        sendIntent.type = "text/plain"
        context.startActivity(Intent.createChooser(sendIntent, null))
    }
}