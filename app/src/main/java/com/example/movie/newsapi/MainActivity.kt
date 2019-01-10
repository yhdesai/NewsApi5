package com.example.movie.newsapi

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.movie.newsapi.Adapter.ViewHolder.ListSourceAdapter
import com.example.movie.newsapi.Common.Common
import com.example.movie.newsapi.Interface.NewsService
import com.example.movie.newsapi.Model.Website
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.util.logging.Logger
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init cache
        Paper.init(this)

        //init service
        mService = Common.newsService

        //init View
        swipe_to_refresh.setOnRefreshListener {
            loadWebsiteSource(true)
        }

        recycler_view_source_news.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recycler_view_source_news.layoutManager = layoutManager

        dialog = SpotsDialog(this)

        loadWebsiteSource(false)
    }

    private fun loadWebsiteSource(isrefresh: Boolean) {
        if(!isrefresh)
        {
            val cache = Paper.book().read<String>("cache")
            if (cache != null && !cache.isBlank() && cache!="null" )
            {
                //read cache
                val webSite = Gson().fromJson<Website>(cache,Website::class.java)
                adapter = ListSourceAdapter(baseContext,webSite)
                adapter.notifyDataSetChanged()
                recycler_view_source_news.adapter = adapter
            }
            else
            {
                //load website and write cache
                dialog.show()
                //fetch new data
                mService.sources.enqueue(object:retrofit2.Callback<Website>{
                    override fun onFailure(call: Call<Website>, t: Throwable) {
                        Toast.makeText(baseContext,"Failed",Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Website>, response: Response<Website>) {
                        adapter = ListSourceAdapter(baseContext,response.body()!!)
                        adapter.notifyDataSetChanged()
                        recycler_view_source_news.adapter = adapter

                        //save to cache
                        Paper.book().write("cache", Gson().toJson(response.body()!!))

                        dialog.dismiss()
                    }
                })
            }
        }
        else
        {
            swipe_to_refresh.isRefreshing=true
            //fetch new data
            mService.sources.enqueue(object:retrofit2.Callback<Website>{
                override fun onFailure(call: Call<Website>, t: Throwable) {
                    Toast.makeText(baseContext,"Failed",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Website>, response: Response<Website>) {
                    adapter = ListSourceAdapter(baseContext,response.body()!!)
                    adapter.notifyDataSetChanged()
                    Logger.getLogger(MainActivity::class.java.name).warning("heree"+ response.toString())
                    recycler_view_source_news.adapter = adapter

                    //save to cache
                    Paper.book().write("cache", Gson().toJson(response.body()!!))

                    swipe_to_refresh.isRefreshing=false
                }
            })
        }
    }


}



