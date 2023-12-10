package com.kk.android.bayareanews.common

import java.net.URI

class StringUtil2 {

    companion object {
        @JvmStatic
        fun determinePublisher(link: String): String {
            try {
                val url = URI(link)
                //println("domain: ${url.host}")
                val array = url.host.split(".")
                return array[array.size - 2]
            }catch (e: Exception) {
                MLog.e("StringUtil2", "determinePublisher() failed on: $link")
                return ""
            }
        }
    }

}