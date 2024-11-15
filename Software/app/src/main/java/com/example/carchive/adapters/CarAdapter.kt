import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.carchive.R
import com.example.carchive.entities.Car
import com.example.carchive.helpers.MockDataLoader

class CarAdapter(
    private val cars: List<Car>,
    private val onDetailsClick: (View, Car) -> Unit // Updated callback to accept View and Car
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageCar: ImageView = itemView.findViewById(R.id.imageCar)
        private val textModel: TextView = itemView.findViewById(R.id.textModel)
        private val textRegistration: TextView = itemView.findViewById(R.id.textRegistration)
        private val textLocation: TextView = itemView.findViewById(R.id.textLocation)
        private val textKilometers: TextView = itemView.findViewById(R.id.textKilometers)
        private val textSellsRents: TextView = itemView.findViewById(R.id.textSellsRents)
        private val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        private val imgBtnDetails: ImageView = itemView.findViewById(R.id.imgBtnDetails)

        @SuppressLint("SetTextI18n")
        fun bind(car: Car) {
            // Set text fields
            textModel.text = "${car.marka} ${car.model}"
            textRegistration.text = car.registration
            textLocation.text = car.location
            textKilometers.text = "${car.kilometers} km"
            textSellsRents.text = if (car.rentSell) "U prodaji" else "U najmu"
            textPrice.text = "$${car.price}"

            // Set up listener for imgBtnDetails to pass both view and car to the callback
            imgBtnDetails.setOnClickListener { view ->
                onDetailsClick(view, car) // Pass both anchor view and car to the callback
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun getItemCount(): Int = cars.size

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }
}
