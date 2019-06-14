package com.github.emartynov.flickrdemo.imagelist.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.emartynov.flickrdemo.common.async.Async
import com.github.emartynov.flickrdemo.common.http.FlickApi
import com.github.emartynov.flickrdemo.common.http.Http
import com.github.emartynov.flickrdemo.common.http.Success
import com.github.emartynov.flickrdemo.common.image.BitmapScale
import com.github.emartynov.flickrdemo.common.image.Cache
import com.github.emartynov.flickrdemo.imagelist.data.ImageData
import java.util.concurrent.Callable

class ImagesAdapter(
    private val http: Http,
    private val async: Async,
    private val bitmapScale: BitmapScale,
    private val cache: Cache
) : BaseAdapter() {
    private val images = mutableListOf<ImageData>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            layoutInflater.inflate(com.github.emartynov.flickrdemo.R.layout.image_item, parent, false)
        } else {
            convertView
        }

        val imageView = view.findViewById<ImageView>(com.github.emartynov.flickrdemo.R.id.image_view)
        val textView = view.findViewById<TextView>(com.github.emartynov.flickrdemo.R.id.title_view)

        imageView.visibility = View.GONE
        textView.visibility = View.VISIBLE
        textView.text = images[position].title

        async.cancel(view)
        async.queue(
            job = Callable {
                val url = FlickApi.getImageUrl(images[position])
                val destWidth = view.measuredWidth
                val destHeight = view.measuredHeight
                val key = Triple(url, destWidth, destHeight)
                cache.get(key) ?: loadFromServer(key)
            },
            callback = {
                if (it is Success) {
                    imageView.setImageBitmap(it.data)
                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                } else {
                    // ignore image loading errors for now
                }
            },
            tag = view
        )

        return view
    }

    private fun loadFromServer(key: Triple<String, Int, Int>): Bitmap {
        val data = http.get(key.first)
        val bitmap =
            bitmapScale.scaleCenterCrop(BitmapFactory.decodeByteArray(data, 0, data.size), key.second, key.third)
        cache.put(key, bitmap)
        return bitmap
    }

    override fun getItem(position: Int) = images[position]

    override fun getItemId(position: Int) = 0L

    override fun getCount() = images.size

    fun setImages(images: MutableList<ImageData>) {
        this.images.clear()
        this.images.addAll(images)

        notifyDataSetChanged()
    }
}
