package uk.henrytwist.projectbarry.application.view.components

import uk.henrytwist.androidbasics.recyclerview.BindingItemAdapter
import uk.henrytwist.androidbasics.recyclerview.SingleBindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.HeaderBinding

class HeaderAdapter : SingleBindingItemAdapter<HeaderBinding>(R.layout.header) {

    lateinit var handler: Handler

    override fun onBind(binding: HeaderBinding) {

        binding.handler = handler
    }

    interface Handler {

        fun onClickBack()
    }
}