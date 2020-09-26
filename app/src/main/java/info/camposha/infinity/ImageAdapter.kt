package info.camposha.infinity

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ImageAdapter(private val images: ArrayList<String>, private val onClick: (String, View) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    fun addAll(data: List<String>) {
        if (data.isNotEmpty()) {
            val initial: Int = this.itemCount
            val after = initial + data.size
            for (d in data) {
                images.add(d)
            }
            notifyItemRangeInserted(initial, after)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) =
        holder.bind(images[position])

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(url: String) {
            val requestOptions = RequestOptions.placeholderOf(R.drawable.load_glass).error(R.drawable.gallery_roll)
                .dontTransform()
                .onlyRetrieveFromCache(false)

            Glide.with(itemView as ImageView).load(url).apply(requestOptions).into(itemView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.transitionName = url
            }
            //itemView.setOnClickListener { onClick(url, it) }
            itemView.setOnClickListener { onClick(url, it) }
        }
    }

}