import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carchive.CarchiveActivity
import com.example.carchive.adapters.CarAdapter
import com.example.carchive.databinding.OfferDetailsBinding
import com.example.carchive.viewmodels.OfferViewModel

class OfferDetailsFragment : Fragment() {
    private var _binding: OfferDetailsBinding? = null
    private val binding get() = _binding!!
    private val offerViewModel: OfferViewModel by viewModels()
    private lateinit var carAdapter: CarAdapter
    private var offerId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = OfferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

        arguments?.let { bundle ->
            offerId = bundle.getInt("id", 0)
            binding.offerTitle.text = "${bundle.getString("title", "Naslov")}"
            binding.offerPrice.text = "${bundle.getDouble("price", 0.0)}"
            binding.offerDate.text = "${bundle.getString("dateOfCreation", "2025-12-12")}"
        }


        binding.recyclerVehicles.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
