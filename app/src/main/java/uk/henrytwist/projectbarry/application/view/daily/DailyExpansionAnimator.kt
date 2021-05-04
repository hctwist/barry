package uk.henrytwist.projectbarry.application.view.daily

import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.doOnEnd
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.kotlinbasics.findLastOfInstance
import uk.henrytwist.projectbarry.R

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

            val oldLayout = oldHolder.itemView as ConstraintLayout

            val newSet = ConstraintSet().apply { clone(oldLayout.context, if (preInfo.shouldExpand) R.layout.daily_row_expanded_blueprint else R.layout.daily_row) }

            val transition = AutoTransition().apply {

                doOnEnd {

                    dispatchChangeFinished(newHolder, false)
                }
            }

            TransitionManager.beginDelayedTransition(oldLayout.parent as RecyclerView, transition)
            newSet.applyTo(oldLayout)

            return false
        } else {

            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {

        return true
    }

    class ExpansionInfo(val shouldExpand: Boolean) : ItemHolderInfo()
}