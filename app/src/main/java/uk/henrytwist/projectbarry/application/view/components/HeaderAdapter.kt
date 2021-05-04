package uk.henrytwist.projectbarry.application.view.components

import uk.henrytwist.androidbasics.recyclerview.SingleBindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.HeaderBinding

class HeaderAdapter(private val handler: Handler, var title: String? = null) : SingleBindingItemAdapter<HeaderBinding>(R.layout.header) {

    override fun onBind(binding: HeaderBinding) {

        binding.handler = handler
        binding.title = title
    }

    interface Handler {

        fun onClickBack()
    }
}