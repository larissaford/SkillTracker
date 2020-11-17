package com.example.skilltracker.database.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skilltracker.R
import com.example.skilltracker.database.entity.SkillSet


/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
 * @param orders a list of previous orders
 * @property layoutInflater for inflating the recycler view
 */
class SkillRecyclerAdapter(private val context: Context, private var orders: List<SkillSet>) :
    PagingDataAdapter<SkillSet, SkillRecyclerAdapter.ViewHolder>(SkillSetDiffCallBack()) {

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
        val SkillSet = orders[position]
        holder.SkillSetName?.text = SkillSet.name
        holder.skillsetDescription?.text = SkillSet.description
        holder.dateCreated?.text = SkillSet.dateCreated.toLocalDate().toString()
        }

    /**
     * This answers how much data overall will be displayed
     *
     * @return the size of the previous orders list
     */
    override fun getItemCount() = orders.size

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