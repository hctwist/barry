package uk.henrytwist.projectbarry.application.view.daily

import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.transition.doOnEnd
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.kotlinbasics.findLastOfInstance

class DailyExpansionAnimator : DefaultItemAnimator() {

    override fun recordPreLayoutInformation(state: RecyclerView.State, viewHolder: RecyclerView.ViewHolder, changeFlags: Int, payloads: MutableList<Any>): ItemHolderInfo {

        val expansion = payloads.findLastOfInstance<DailyDiff.Expansion>()
        if (expansion != null) {

            val info = ExpansionInfo(expansion.expand)
            info.setFrom(viewHolder)
            return info
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preInfo: ItemHolderInfo, postInfo: ItemHolderInfo): Boolean {

        if (preInfo is ExpansionInfo) {

            val transition = AutoTransition().apply {

                doOnEnd {

                    dispatchChangeFinished(newHolder, false)
                }
            }

            TransitionManager.beginDelayedTransition((oldHolder.itemView as ConstraintLayout).parent as RecyclerView, transition)
            (oldHolder as DailyAdapter.Holder).setExpanded(preInfo.shouldExpand)

            return false
        } else {

            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>): Boolean {

        return if (payloads.findLastOfInstance<DailyDiff.Expansion>() != null) true else super.canReuseUpdatedViewHolder(viewHolder, payloads)
    }

    class ExpansionInfo(val shouldExpand: Boolean) : ItemHolderInfo()
}