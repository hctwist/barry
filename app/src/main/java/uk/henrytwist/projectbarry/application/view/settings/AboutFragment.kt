package uk.henrytwist.projectbarry.application.view.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.htpcore.about.AboutAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter

class AboutFragment : Fragment(R.layout.about_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view as RecyclerView

        val headerAdapter = HeaderAdapter()
        headerAdapter.handler = object : HeaderAdapter.Handler {

            override fun onClickBack() {

                findNavController().popBackStack()
            }
        }

        val aboutAdapter = AboutAdapter()

        aboutAdapter.build(requireContext()) {

            addTitle(R.string.settings_category_forecast)
            addItem(R.string.settings_about_open_weather_title, R.string.settings_about_open_weather_body, null, "https://openweathermap.org/our-initiatives")

            addTitle(R.string.settings_about_art_title)
            addItem(R.string.settings_about_abi_title, R.string.settings_about_abi_body, null, "https://www.linkedin.com/in/abigail-hruzik-869151203/")

            addTitle(R.string.settings_about_icons_title)
            addItem(R.string.settings_about_icons8_title, R.string.settings_about_icons8_body, null, "https://icons8.com")
        }

        recycler.run {

            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(headerAdapter, aboutAdapter)
        }
    }
}