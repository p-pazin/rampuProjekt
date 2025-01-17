package com.example.carchive.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.carchive.CarchiveActivity
import com.example.carchive.R
import com.example.carchive.data.dto.ContactStatusStatsDto
import com.example.carchive.data.dto.YearlyInfoDto

import com.example.carchive.databinding.FragmentStatsBinding
import com.example.carchive.viewmodels.StatsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var contactCreationLineChart: LineChart
    private lateinit var invoiceCreationChart: LineChart
    private val viewModel : StatsViewModel by viewModels()
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.contact_status_pie_chart)
        contactCreationLineChart = view.findViewById(R.id.contact_creation_line_chart)
        invoiceCreationChart = view.findViewById(R.id.invoice_creation_line_chart)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.contactStatusStatsData.collect { data ->
                data?.let { showContactStatusStatsChart(pieChart, it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.contactCreationStatsData.collect { data ->
                data?.let { showYearlyInfoChart(contactCreationLineChart, it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.invoiceCreationStatsData.collect { data ->
                data?.let { showYearlyInfoChart(invoiceCreationChart, it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchContactStatusStats()
            viewModel.fetchContactCreationStats()
            viewModel.fetchInvoiceCreationStats()
        }

        binding.sidebarLogo.drawerToggleButton.setOnClickListener {
            (activity as? CarchiveActivity)?.toggleDrawer()
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? CarchiveActivity)?.setDrawerEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? CarchiveActivity)?.setDrawerEnabled(false)
    }

    private fun showContactStatusStatsChart(pieChart: PieChart, data: ContactStatusStatsDto) {
        val activePercentage = (data.activeCount.toFloat() / data.total.toFloat()) * 100f
        val inactivePercentage = (data.inactiveCount.toFloat() / data.total.toFloat()) * 100f

        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(activePercentage, "Aktivan"))
            add(PieEntry(inactivePercentage, "Neaktivan"))
        }

        val pieDataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#F65B43"), Color.parseColor("#D5412A"))
            sliceSpace = 2f
            valueTextSize = 10f
            valueTextColor = Color.WHITE
        }

        pieChart.data = PieData(pieDataSet)
        pieChart.description.isEnabled = false
        val legend = pieChart.legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            setDrawInside(false)
            textSize = 11f
        }
        pieChart.invalidate()
    }

    private fun showYearlyInfoChart(lineChart: LineChart, yearlyInfo: YearlyInfoDto) {
        lineChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val value = e.y
                    val index = e.x
                    val month = listOf(
                        getString(R.string.Sijecanj), getString(R.string.Veljaca),
                        getString(R.string.Ozujak), getString(R.string.Travanj),
                        getString(R.string.Svibanj), getString(R.string.Lipanj),
                        getString(R.string.Srpanj), getString(R.string.Kolovoz),
                        getString(R.string.Rujan), getString(R.string.Listopad),
                        getString(R.string.Studeni), getString(R.string.Prosinac)
                    )[index.toInt()]
                    Toast.makeText(
                        requireContext(),
                        "${getString(R.string.Mjesec)}: $month, ${getString(R.string.Vrijednost)}: $value",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onNothingSelected() {
            }
        })

        val dataValues = listOf(
            yearlyInfo.jan, yearlyInfo.feb, yearlyInfo.mar, yearlyInfo.apr,
            yearlyInfo.may, yearlyInfo.jun, yearlyInfo.jul, yearlyInfo.aug,
            yearlyInfo.sep, yearlyInfo.oct, yearlyInfo.nov, yearlyInfo.dec
        )

        val entries = ArrayList<Entry>()
        for (i in dataValues.indices) {
            entries.add(Entry(i.toFloat(), dataValues[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "")
        dataSet.setDrawFilled(true)
        dataSet.setDrawValues(false)
        dataSet.setFillColor(ContextCompat.getColor(requireContext(), R.color.accentColorDark))
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.accentColorDark))
        dataSet.setFillAlpha(120)
        dataSet.setDrawCircles(false)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val yAxis = lineChart.axisLeft
        yAxis.setGranularity(5f)
        yAxis.axisMinimum = 0f
        lineChart.axisRight.isEnabled = false

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(
            listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        )
        xAxis.granularity = 1f
        xAxis.labelCount = 12
        xAxis.textSize = 12f

        lineChart.description.isEnabled = false
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.animateX(200)
        lineChart.setDrawBorders(true)
        lineChart.setBorderColor(Color.BLACK)
        lineChart.setBorderWidth(2f)

        lineChart.invalidate()
    }
}