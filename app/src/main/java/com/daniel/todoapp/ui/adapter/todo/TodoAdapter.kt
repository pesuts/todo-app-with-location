package com.daniel.todoapp.ui.adapter.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.todoapp.databinding.RowPlaceBinding
import com.daniel.todoapp.data.models.Place
import com.squareup.picasso.Picasso

class TodoAdapter (private val listData: ArrayList<Place>) : RecyclerView.Adapter<TodoAdapter.DataViewHolder>() {
    private lateinit var OnClickCallBack: onClickCallBack

    fun setOnClickCallBack(data: onClickCallBack){
        this.OnClickCallBack = data
    }

    interface onClickCallBack{
        fun onItemClicked(data: Place)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(RowPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val place = listData[position]
        val address = "${place.addressComponents?.get(0)}, ${place.addressComponents?.get(1)}, ${place.addressComponents?.get(2)}"
        holder.binding.placeName.text = place.name
        holder.binding.placeAddress.text = address
        holder.binding.ratingBar.setRating(place.rating!!.toFloat())
        Picasso.get().load(place.photos?.get(0))
            .into(holder.binding.placeImage)
        holder.itemView.setOnClickListener {
            OnClickCallBack.onItemClicked(listData[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listData.count()
    }

    class DataViewHolder(val binding: RowPlaceBinding): RecyclerView.ViewHolder(binding.root)
}