package com.kk.android.bayareanews.common

import android.util.Base64

object EncodingUtil {
    fun encodeUrlSafe(str: String): String {
        return Base64.encodeToString(str.toByteArray(),Base64.URL_SAFE)
    }

    fun decodeUrlSafe(str: String): String {
        return String(Base64.decode(str, Base64.URL_SAFE))
    }
}