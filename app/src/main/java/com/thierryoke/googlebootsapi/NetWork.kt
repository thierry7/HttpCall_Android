package com.thierryoke.googlebootsapi

import android.net.Uri
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

//https://www.googleapis.com/books/v1/volumesq=pride+prejudice&maxResults=5&printType=books

const val  BASE_URL = "https://www.googleapis.com/"
const val ENDPOINT = "books/v1/volumes?"
const val Q_PARAM = "q"
const val MAX_RESULTS_PARAM = "maxResults"
const val PRINT_TYPE_PARAM = "printType"
class NetWork {
    private val URI = Uri.parse("$BASE_URL$ENDPOINT").buildUpon()
        .appendQueryParameter(Q_PARAM, "bible")
        .appendQueryParameter(MAX_RESULTS_PARAM, "5")
        .appendQueryParameter(PRINT_TYPE_PARAM, "books")
        .build()

    private val url: URL = URL(URI.toString())


    fun configureNetWorkConnection(){
        val httpUrlConnection = url.openConnection() as HttpURLConnection
        httpUrlConnection.readTimeout = 10000
        httpUrlConnection.connectTimeout = 10000
        httpUrlConnection.requestMethod = "GET"
        httpUrlConnection.doInput = true
        httpUrlConnection.connect()

        val inputStream = httpUrlConnection.inputStream
        val responseCode = httpUrlConnection.responseCode
        println("Response Code $responseCode")
        val jsonResponse = convertIsToString(inputStream, 1024)
        println(jsonResponse)
    }


    fun convertIsToString(stream: InputStream?, len: Int): String? {
        var reader: Reader? = null
        reader = InputStreamReader(stream, "UTF-8")
        val buffer = CharArray(len)
        reader.read(buffer)
        return String(buffer)
    }



}