package com.example.rockstarapp.service


import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.AsyncTask
import android.util.Log
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rockstarapp.adapter.RockstarListViewAdapter
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Rockstar
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


class RockstarService() : AsyncTask<Context,String, ArrayList<Rockstar>>() {

    private val JSON_URL ="https://api.mocki.io/v1/d9d8585a"
    private var listRockstar:ArrayList<Rockstar> = ArrayList()

    override fun doInBackground(vararg params: Context): ArrayList<Rockstar> {
        try {
            val url = URL(JSON_URL)
            try {
                var data = JSONObject(url.readText())
                var test = data.getJSONArray("team")
                for(i in 0 until test.length()) {
                    var rockstarJSON = test.getJSONObject(i)
                    //val id = i+1
                    val status = rockstarJSON.optString("status")
                    val last_name = rockstarJSON.optString("last_name")
                    val first_name = rockstarJSON.optString("first_name")
                    val picture_url = rockstarJSON.optString("picture_url")
                    var rockstar = Rockstar(status,last_name,first_name,picture_url,false)
                    listRockstar.add(rockstar)
                }
                return listRockstar
            }catch (e:MalformedURLException){
                e.printStackTrace()
            }catch (e:IOException){
                e.printStackTrace()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ArrayList()
    }
}

