package com.twisthenry8gmail.projectbarry.view.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.databinding.FragmentDailyBinding

class FragmentDaily : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDailyBinding.inflate(inflater, container, false)

        return binding.root
    }
}