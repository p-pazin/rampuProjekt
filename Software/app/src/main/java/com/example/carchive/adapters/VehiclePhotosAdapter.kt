import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carchive.R
import com.example.carchive.data.dto.VehiclePhotoDto

class VehiclePhotosAdapter(private val photoList: List<VehiclePhotoDto>) :
    RecyclerView.Adapter<VehiclePhotosAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.ivVehicleImageItem)

        fun bind(photo: VehiclePhotoDto) {
            Glide.with(itemView.context)
                .load(photo.link)
                .placeholder(R.drawable.ic_katalog_vozila)
                .error(R.drawable.ic_katalog_vozila)
                .into(imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size
}
