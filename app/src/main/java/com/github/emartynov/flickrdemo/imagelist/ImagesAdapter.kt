package com.github.emartynov.flickrdemo.imagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.github.emartynov.flickrdemo.R
import com.github.emartynov.flickrdemo.imagelist.data.ImageData

class ImagesAdapter : BaseAdapter() {
    private val images = mutableListOf<ImageData>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null) {
            val layoutInflater = LayoutInflater.from(parent.context)
            layoutInflater.inflate(R.layout.image_item, parent, false)
        } else {
            convertView
        }

        val imageView = view.findViewById<ImageView>(R.id.image_view)
        val textView = view.findViewById<TextView>(R.id.title_view)

        imageView.visibility = View.GONE
        textView.visibility = View.VISIBLE
        textView.text = images[position].title

        return view
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
