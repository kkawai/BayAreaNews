package com.kk.android.bayareanews.common

import java.net.URI

class StringUtil2 {

    companion object {
        @JvmStatic
        fun determinePublisher(link: String): String {
            val url = URI(link)
            //println("domain: ${url.host}")
            val array = url.host.split(".")
            return array[array.size-2]
        }
    }

}