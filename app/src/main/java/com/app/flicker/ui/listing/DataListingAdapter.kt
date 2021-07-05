package com.app.flicker.ui.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aghajari.zoomhelper.ZoomHelper
import com.app.flicker.R
import com.app.flicker.pojo.Photo
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.photo_list_item_layout.view.*

class DataListingAdapter(var list: MutableList<Photo>) :  RecyclerView.Adapter<DataListingAdapter.ViewHolder>() {

    inner class ViewHolder(private val containerView: View) :
        RecyclerView.ViewHolder(containerView) {

        fun bind(photo: Photo) {
            if(photo.title.isBlank()) {
                itemView.textViewTitle.text = "TITLE NOT FOUND"
            } else itemView.textViewTitle.text = photo.title
            val photoUrl = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_m.jpg"
            Glide.with(itemView.context)
                .load(photoUrl)
                .error(R.drawable.error_image)
                .into(itemView.imageViewBanner)
            ZoomHelper.addZoomableView(itemView.imageViewBanner)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.photo_list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(dataList: MutableList<Photo>) {
        dataList.removeAll(list)
        list.addAll(dataList)
        notifyDataSetChanged()
    }
}