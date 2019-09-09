package com.app.baljeet.iconfinderapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.baljeet.iconfinderapp.R
import com.app.baljeet.iconfinderapp.callbacks.OnRecyclerViewItemClickListener
import com.app.baljeet.iconfinderapp.models.Icon
import com.app.baljeet.iconfinderapp.utils.ImageUtility
import kotlinx.android.synthetic.main.icons_grid_layout_list_item.view.*

class IconsGridAdapter(val onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener)
    : RecyclerView.Adapter<IconsGridAdapter.IconsViewHolder>() {

    private var data: MutableList<Icon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        return IconsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.icons_grid_layout_list_item,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) =
        holder.bind(data[position], onRecyclerViewItemClickListener)

    fun newDataInserted(data: MutableList<Icon>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun getItemByPosition(position: Int): Icon {
        return data[position]
    }

    fun clearAll() {
        data.clear()
        notifyDataSetChanged()
    }

    class IconsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Icon,
            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
        ) = with(itemView) {
            val rasterSize = item.raster_sizes
            val formats = rasterSize?.get(rasterSize.size - 1)?.formats
            val format = formats?.get(formats.size - 1)
            val previewUrl = format?.preview_url

            if (item.is_premium != null && !item.is_premium) {
                itemView.imageButtonDownload.visibility = View.VISIBLE
                itemView.imageViewPaidIcon.visibility = View.GONE
                itemView.textViewIconPrice.visibility = View.GONE
            } else {
                itemView.imageButtonDownload.visibility = View.GONE
                itemView.imageViewPaidIcon.visibility = View.VISIBLE
                itemView.textViewIconPrice.visibility = View.VISIBLE

                if (item.prices != null && item.prices[0].price != null) {
                    val price = item.prices[0]
                    itemView.textViewIconPrice.text = "$ ${price.price}"
                }
            }

            ImageUtility.loadImageFromNetwork(context, previewUrl, itemView.imageViewIcon)

            itemView.imageButtonDownload.setOnClickListener { view ->
                onRecyclerViewItemClickListener.onItemClick(itemView.imageButtonDownload, adapterPosition)
            }
        }
    }
}