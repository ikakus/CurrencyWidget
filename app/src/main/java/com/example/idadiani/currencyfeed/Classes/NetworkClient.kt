package com.example.idadiani.currencyfeed.Classes

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by i.dadiani on 2/24/2016.
 */

open class NetworkClient{
    fun get(url:String): String{

        val request = Request.Builder().url(url).build()
        val response = OkHttpClient().newCall(request).execute()
        val body = response.body()
        // body.toString() returns a string representing the object and not the body itself, probably
        // kotlins fault when using third party libraries. Use byteStream() and convert it to a String
        return body.string();
    }
}