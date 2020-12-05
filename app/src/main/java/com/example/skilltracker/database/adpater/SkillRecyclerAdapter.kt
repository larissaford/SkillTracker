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
import com.example.skilltracker.SkillFragmentDirections
import com.example.skilltracker.database.entity.Skill

/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
 * @param skills a list of previous skills for the SkillSet
 * @property layoutInflater for inflating the recycler view
 */
class SkillRecyclerAdapter (private val context: Context, private var skills: List<Skill>) :
    PagingDataAdapter<Skill, SkillRecyclerAdapter.ViewHolder>(SkillDiffCallBack()) {
    private val layoutInflater = LayoutInflater.from(context)

    /**
     * gets references to the views we want to use within the previous_order_list layout by
     * calling onCreateViewHolder multiple times.
     *
     * @param itemView the inflated previous_order_list layout
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val skillName: TextView = itemView.findViewById<TextView?>(R.id.skill_name)
        val skillCompleted: TextView = itemView.findViewById<TextView?>(R.id.skill_completed)
        val dateCreated: TextView = itemView.findViewById<TextView?>(R.id.skill_date_created)
        var skill: Skill? = null
    }

    /**
     * gets references to the views we want to use within the previous_order_list layout.
     *
     * @param parent is the ViewGroup
     * @param viewType is the type of view and an int
     * @return returns the reference to the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.skill_list, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * This method binds the views with the data
     *
     * @param holder the view holder we made that contains the references to the views
     * @param position the position of the previously ordered SkillSet in the previous orders list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skill = skills[position]
        holder.skillName.text = skill.skillName
        holder.skillCompleted.text = if (skill.completed)  "Yes" else "No"
        holder.dateCreated.text = skill.dateCreated.toLocalDate().toString()

        // Clicking on CardView navigates to Task Fragment
//        holder.itemView.setOnClickListener { view: View ->
//            view.findNavController().navigate(
//                // navigate to Task Fragment
////                SkillFragmentDirections.actionSkillFragmentToNewSkillFragment(skill.skillId)
//            )
//        }

        //Long Clicks allow for editing the Skill
        holder.itemView.setOnLongClickListener { view: View ->
            view.findNavController().navigate(
                SkillFragmentDirections.actionSkillFragmentToNewSkillFragment(null, skill)
            )
            true
        }
    }

    /**
     * This answers how much data overall will be displayed
     *
     * @return the size of the previous orders list
     */
    override fun getItemCount() = skills.size

    /**
     *  Boiler plate code for the Paging Data Adaptor to work, which is not really used yet but
     *  is for better practices when adding data to the database I think.
     */
    private class SkillDiffCallBack : DiffUtil.ItemCallback<Skill>() {
        override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem.skillId == newItem.skillId
        }

        override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
            return oldItem == newItem
        }
    }

}