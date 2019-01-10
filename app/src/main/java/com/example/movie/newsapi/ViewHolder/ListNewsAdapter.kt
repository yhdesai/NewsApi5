package com.example.movie.newsapi.ViewHolder


import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movie.newsapi.Adapter.ViewHolder.ListNewsViewHolder
import com.example.movie.newsapi.Common.ISO8601Parser
import com.example.movie.newsapi.Interface.ItemClickListener
import com.example.movie.newsapi.Model.Article
import com.example.movie.newsapi.NewsDetail
import com.example.movie.newsapi.R
import com.google.gson.internal.bind.util.ISO8601Utils
import com.squareup.picasso.Picasso
import java.text.ParseException
import java.util.*
import java.util.logging.Logger


class ListNewsAdapter(val articleList:MutableList<Article>, private val context:Context):RecyclerView.Adapter<ListNewsViewHolder>()
{
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListNewsViewHolder {
        val inflater = LayoutInflater.from(p0!!.context)
        val itemView = inflater.inflate(R.layout.news_layout,p0,false)
        return ListNewsViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ListNewsViewHolder, position: Int) {
        Picasso.with(context).load(articleList[position]!!.urlToImage).to(holder.article_image)
        if(articleList[position].title!!.length>65)
        {
            holder.article_title.text=articleList[position].title!!.substring(0 ,65)+"..."
        }
        else
        {
            holder.article_title.text=articleList[position].title!!
        }

        if(articleList[position].publishedAt !=null)
        {
            var date: Date?=null
            try{
                    date = ISO8601Parser.parse(articleList[position].publishedAt!!)
            }catch (ex:ParseException) {
                ex.printStackTrace()
            }
            Logger.getLogger(ListNewsAdapter::class.java.name).warning("datee"+ date.toString())
            //    holder.article_time.setReferenceTime(date!!.time)

        }

        holder.setItemClickListener(object :ItemClickListener{
            override fun onClick(view: View, position: Int) {
                val detail = Intent(context,NewsDetail::class.java)
                detail.putExtra("webURL",articleList[position].url)
                context.startActivity(detail)
            }
        })


    }

}