package com.twisthenry8gmail.projectbarry.view.errors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.databinding.FragmentErrorBinding

abstract class FragmentError : Fragment() {

    abstract fun getErrorModel(): ErrorModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentErrorBinding.inflate(inflater, container, false)
        binding.error = getErrorModel()

        return binding.root
    }
}