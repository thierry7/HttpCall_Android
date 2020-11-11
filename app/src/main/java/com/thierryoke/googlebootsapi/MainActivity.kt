package com.thierryoke.googlebootsapi

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.CharArrayReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chechNetwork()
//        deserializeJsonResponse("title")
    }
    fun convertIsToString(stream: InputStream?, len: Int): String {

        var output : String = ""

        var reader: Reader? = null
        reader = InputStreamReader(stream, "UTF-8")


        while (true){

            val buffer = CharArray(len)


            val bytes_read = reader.read(buffer)
            if (bytes_read > 0){
                val charArrayReader :CharArrayReader = CharArrayReader(buffer)
                val charArrayOutput =  CharArray(bytes_read)
                charArrayReader.read(charArrayOutput,0, bytes_read)
                output += String(charArrayOutput)
            }
            else{
                break;
            }

        }
        return output
    }

    fun deserializeJsonResposne(jsonResponse: String) : BooksResponse{

        var myList : MutableList<ItemsDescription> = mutableListOf<ItemsDescription>()

        val jsonResponse : JSONObject = JSONObject(jsonResponse)
        val jsonArrayItems : JSONArray = jsonResponse.getJSONArray("items")


        for (index in 0 until jsonArrayItems.length()){


            val item: JSONObject = jsonArrayItems[index] as JSONObject;
            val volume: JSONObject? = item.getJSONObject("volumeInfo") as JSONObject

            //we have all the info here. Lets create teh object
            volume?.let {
//                create volume info object
                try {


                    val volumeInfo =
                        VolumeInfo(volume.getString("title"), volume.getString("subtitle"))

                    //create item description object
                    val itemDescirption = ItemsDescription(volumeInfo)

                    //add to the list
                    myList.add(itemDescirption)
                } catch (e: Exception) {
                    print("Skipping the Item")
                }


            }

        }
        return BooksResponse(myList)
    }


    fun deserializeJsonResponse(input: String){

        val booksResponse: BooksResponse
        var booksItemDescription: ItemsDescription
        val booksListItemsDescription = mutableListOf<ItemsDescription>()
        val jsonResponse = JSONObject(input)
        val jsonArrayItems = jsonResponse.getJSONArray("items")
        for(index in 0 until jsonArrayItems.length())
        {
            val jsonElement = jsonArrayItems[index] as JSONObject
            val jsonVolumeInfo = jsonElement.getJSONObject("volumeInfo")
            val booksItemsInfo = VolumeInfo(jsonVolumeInfo.getString("title"),
                                    jsonVolumeInfo.getString("subtitle"))
            booksItemDescription = ItemsDescription(booksItemsInfo)
            booksListItemsDescription.add(booksItemDescription)


        }
        booksResponse = BooksResponse(booksListItemsDescription)
        println(booksResponse.toString())
    }


    fun chechNetwork(){
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val netWorkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        netWorkInfo?.let {
            if(netWorkInfo.isConnected) {

                val netWork = NetWork()
                Thread(Runnable { netWork.configureNetWorkConnection() }).start()

            }
            else
                Toast.makeText(this, "NO Connection", Toast.LENGTH_SHORT).show()
        }
    }
}