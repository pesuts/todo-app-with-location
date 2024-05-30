package com.daniel.todoapp.ui.adapter.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniel.todoapp.databinding.ItemImageBinding
import com.squareup.picasso.Picasso

class ImageSliderAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

//    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
//        return ImageViewHolder(view)
        return ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val imageUrl = imageUrls[position]
//        Picasso.get().load(imageUrl).into(holder.imageView)
        val imageUrl = imageUrls[position]
        Picasso.get().load(imageUrl)
            .into(holder.binding.imageView)
//                        .into(binding.detailImage, object: Callback{
//                    override fun onSuccess() {
//                        viewModel.setIsLoading(false)
////                        binding.detailName.setText(data!!.name)
////                        binding.detailAddress.setText(data!!.address)
//                    }
//                    override fun onError(e: Exception?) {
//                        viewModel.setIsLoading(false)
//                    }
//                });
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

//    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.image_view)
//    }
    class ImageViewHolder(val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root)
}
