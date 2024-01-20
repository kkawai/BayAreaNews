package com.kk.android.bayareanews.common

import android.content.Context
import android.content.Intent
import android.net.Uri


object PlaystoreUtil {
    fun open(context: Context) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.PLAYSTORE_URL)
            )
        )
    }
}