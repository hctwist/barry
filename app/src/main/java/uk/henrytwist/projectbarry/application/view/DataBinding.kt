package uk.henrytwist.projectbarry.application.view

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration

object DataBinding {

    @JvmStatic
    @BindingAdapter("invisibleUnless")
    fun invisibleUnless(view: View, boolean: Boolean) {

        view.visibility = if (boolean) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("goneUnless")
    fun goneUnless(view: View, boolean: Boolean) {

        view.visibility = if (boolean) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("itemMargins")
    fun setItemMargins(recyclerView: RecyclerView, margin: Float) {

        recyclerView.addItemDecoration(MarginItemDecoration(margin))
    }
}