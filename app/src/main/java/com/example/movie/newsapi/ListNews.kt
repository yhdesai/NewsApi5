package com.example.movie.newsapi

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.support.v7.widget.LinearLayoutManager
import com.example.movie.newsapi.Common.Common
import com.example.movie.newsapi.Interface.NewsService
import com.example.movie.newsapi.Model.News
import com.example.movie.newsapi.ViewHolder.ListNewsAdapter
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListNews : AppCompatActivity() {

    var source=""
    var webHotUrl:String?=""
    lateinit var dialog: AlertDialog
    lateinit var mService: NewsService
    lateinit var adapter: ListNewsAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        //init view
        mService= Common.newsService
        dialog = SpotsDialog(this)

        swipe_to_refresh.setOnRefreshListener { loadNews(source,true) }

        diagonalLayout.setOnClickListener{
            val detail = Intent(baseContext,NewsDetail::class.java)
            detail.putExtra("webURL",webHotUrl)
            startActivity(detail)

        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager=LinearLayoutManager(this)

        if(intent!= null)
        {
            source=intent.getStringExtra("source")
            if(!source.isEmpty())
                loadNews(source,false)
        }
    }

    private fun loadNews(source: String?, isRefreshing: Boolean) {

        if(isRefreshing)
        {
            dialog.show()
            mService.getNewsFromSource(Common.getNewsApi(source!!)).enqueue(object :Callback<News>{
                override fun onFailure(call: Call<News>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    dialog.dismiss()

                    //get first article to how news

                    Picasso.with(baseContext).load(response.body()!!.articles!![0].urlToImage)
                        .into(top_image)

                    top_title.text = response.body()!!.articles!![0].title
                    top_author.text = response.body()!!.articles!![0].author

                    webHotUrl = response.body()!!.articles!![0].url

                    //load all remain articles

                    val removeFirstItem = response!!.body()!!.articles
                    //First item is above to hot news
                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(removeFirstItem!!,baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }
            })
        }
        else
        {

            swipe_to_refresh.isRefreshing =true

            mService.getNewsFromSource(Common.getNewsApi(source!!)).enqueue(object :Callback<News>{
                override fun onFailure(call: Call<News>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    swipe_to_refresh.isRefreshing = false

                    //get first article to how news

                    Picasso.with(baseContext).load(response.body()!!.articles!![0].urlToImage)
                        .into(top_image)

                    top_title.text = response.body()!!.articles!![0].title
                    top_author.text = response.body()!!.articles!![0].author

                    webHotUrl = response.body()!!.articles!![0].url

                    //load all remain articles

                    val removeFirstItem = response!!.body()!!.articles
                    //First item is above to hot news
                    removeFirstItem!!.removeAt(0)

                    adapter = ListNewsAdapter(removeFirstItem!!,baseContext)
                    adapter.notifyDataSetChanged()
                    list_news.adapter = adapter
                }
            })
        }
    }
}
