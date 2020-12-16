package com.example.skilltracker.database.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skilltracker.R
import com.example.skilltracker.SkillSetFragmentDirections
import com.example.skilltracker.database.entity.SkillSet
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
 * @param skillSets a list of previous SkillSets
 * @property layoutInflater for inflating the recycler view
 */
class SkillSetRecyclerAdapter(private val context: Context, private var skillSets: List<SkillSet>) :
    PagingDataAdapter<SkillSet, SkillSetRecyclerAdapter.ViewHolder>(SkillSetDiffCallBack()) {

    private val layoutInflater = LayoutInflater.from(context)

    /**
     * gets references to the views we want to use within the previous_order_list layout by
     * calling onCreateViewHolder multiple times.
     *
     * @param itemView the inflated previous_order_list layout
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val SkillSetName = itemView.findViewById<TextView?>(R.id.skill_set_name)
        val skillsetDescription = itemView.findViewById<TextView?>(R.id.description)
        val dateCreated = itemView.findViewById<TextView?>(R.id.date_created)
        var SkillSet: SkillSet? = null
    }

    /**
     * gets references to the views we want to use within the previous_order_list layout.
     *
     * @param parent is the ViewGroup
     * @param viewType is the type of view and an int
     * @return returns the reference to the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.skill_set_list, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * This method binds the views with the data
     *
     * @param holder the view holder we made that contains the references to the views
     * @param position the position of the previously ordered SkillSet in the previous orders list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skillSet = skillSets[position]
        holder.SkillSetName?.text = skillSet.name
        holder.skillsetDescription?.text = skillSet.description

        // Format date and set it to the viewHolder
        var formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        holder.dateCreated?.text = skillSet.dateCreated.format(formatter).toString()

        // Clicking on CardView navigates to Skills Fragment
        holder.itemView.setOnClickListener { view: View->
            view.findNavController().navigate(
                // Pass Long of SkillSet for Skill to find Join data
                SkillSetFragmentDirections.actionSkillSetFragmentToSkillFragment(skillSet.name, skillSet)
            )
        }

        // Long Clicks on CardView Navigate to Update it
        holder.itemView.setOnLongClickListener{ view: View->
            view.findNavController().navigate(
                // Go to New SkillSetFragment
                SkillSetFragmentDirections.actionSkillSetFragmentToNewSkillSetFragment(skillSet)
            )
            // needed for this type of listener
            true
        }
    }

    /**
     * This answers how much data overall will be displayed
     *
     * @return the size of the previous orders list
     */
    override fun getItemCount() = skillSets.size

}

    /**
     *  Boiler plate code for the Paging Data Adaptor to work, which is not really used yet but
     *  is for better practices when adding data to the database I think.
     */
    private class SkillSetDiffCallBack : DiffUtil.ItemCallback<SkillSet>() {
        override fun areItemsTheSame(oldItem: SkillSet, newItem: SkillSet): Boolean {
            return oldItem.skillSetId == newItem.skillSetId
        }

        override fun areContentsTheSame(oldItem: SkillSet, newItem: SkillSet): Boolean {
            return oldItem == newItem
        }
}