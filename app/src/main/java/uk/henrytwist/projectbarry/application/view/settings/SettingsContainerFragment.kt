package uk.henrytwist.projectbarry.application.view.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uk.henrytwist.projectbarry.R

class SettingsContainerFragment : Fragment(R.layout.settings_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<View>(R.id.settings_back).setOnClickListener {

            findNavController().popBackStack()
        }
    }
}