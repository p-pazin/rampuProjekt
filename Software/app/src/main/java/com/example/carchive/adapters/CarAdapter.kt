import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.Vehicle

class CarAdapter(
    private var vehicles: List<Vehicle>,
    private val onDetailsClick: (View, Vehicle) -> Unit,
    private val onCarCardClick: (View, Vehicle) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val carCard: LinearLayout = itemView.findViewById(R.id.carCard)
        private val imageCar: ImageView = itemView.findViewById(R.id.imageCar)
        private val textModel: TextView = itemView.findViewById(R.id.textModel)
        private val textRegistration: TextView = itemView.findViewById(R.id.textRegistration)
        private val textLocation: TextView = itemView.findViewById(R.id.textLocation)
        private val textKilometers: TextView = itemView.findViewById(R.id.textKilometers)
        private val textSellsRents: TextView = itemView.findViewById(R.id.textSellsRents)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val imgBtnDetails: ImageView = itemView.findViewById(R.id.imgBtnDetails)

        @SuppressLint("SetTextI18n")
        fun bind(vehicle: Vehicle) {
            // Set text fields
            textModel.text = "${vehicle.brand} ${vehicle.model}"
            textRegistration.text = vehicle.registration
            textLocation.text = vehicle.location
            textKilometers.text = "${vehicle.kilometers} km"
            textSellsRents.text = if (vehicle.rentSell) "U prodaji" else "U najmu"
            textPrice.text = "$${vehicle.price}"

            // Set up listener for imgBtnDetails to pass both view and car to the callback
            imgBtnDetails.setOnClickListener { view ->
                onDetailsClick(view, vehicle) // Pass both anchor view and car to the callback
            }
            carCard.setOnClickListener { view ->
                onCarCardClick(view, vehicle) // Pass both anchor view and car to the callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int = vehicles.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(vehicles[position])
    }

    fun updateItems(list: List<Vehicle>){
        vehicles = list
    }
}
