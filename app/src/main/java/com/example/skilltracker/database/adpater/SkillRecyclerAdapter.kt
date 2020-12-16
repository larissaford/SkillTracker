package com.example.skilltracker.database.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.skilltracker.R
import com.example.skilltracker.SkillFragmentDirections
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillWithTasks
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle


/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
 * @param skillsWithTasks a list of previous skills with tasks for the specific SkillSet
 * @property layoutInflater for inflating the recycler view
 */
class SkillRecyclerAdapter (private val context: Context, private var skillsWithTasks: List<SkillWithTasks>) :
    PagingDataAdapter<SkillWithTasks, SkillRecyclerAdapter.ViewHolder>(SkillDiffCallBack()) {
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
        var skillWithTasks: SkillWithTasks? = null
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
        // Gather and Calculate Data for View
        val skillWithTasks: SkillWithTasks = skillsWithTasks[position]
        val skill = skillWithTasks.skill
        var tasksCompletedPercentage = 0.0 // assume percentage is 0 for no tasks case
        // If there are tasks, count all the completed tasks for % completed
        if(skillWithTasks.tasks.isNotEmpty()) {
            var tasksCompletedCount = 0
            for(task in skillWithTasks.tasks) {
                if(task.taskCompleted) {
                    tasksCompletedCount++
                }
            }
            tasksCompletedPercentage =
                ((tasksCompletedCount/skillWithTasks.tasks.size) * 100).toDouble()
            if(tasksCompletedPercentage == 100.0){
                holder.itemView.findViewById<CardView>(R.id.card_view).setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
            }
        }

//        if(skill.completed){
//            holder.itemView.findViewById<CardView>(R.id.card_view).setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
//        }

        holder.skillName.text = skill.skillName
        holder.skillCompleted.text = String.format("%.2f", tasksCompletedPercentage) + "%"

        // Format date and set it to viewHolder
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        holder.dateCreated.text = skill.dateCreated.format(formatter).toString()

        // Clicking on CardView navigates to Task Fragment
        holder.itemView.setOnClickListener { view: View ->
            view.findNavController().navigate(
                SkillFragmentDirections.actionSkillFragmentToTaskFragment(skill)
            )
        }

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
    override fun getItemCount() = skillsWithTasks.size

    /**
     *  Boiler plate code for the Paging Data Adaptor to work, which is not really used yet but
     *  is for better practices when adding data to the database I think.
     */
    private class SkillDiffCallBack : DiffUtil.ItemCallback<SkillWithTasks>() {
        override fun areItemsTheSame(oldItem: SkillWithTasks, newItem: SkillWithTasks): Boolean {
            return (oldItem.skill == newItem.skill) && (oldItem.tasks == newItem.tasks)
        }

        override fun areContentsTheSame(oldItem: SkillWithTasks, newItem: SkillWithTasks): Boolean {
            return (oldItem.skill == newItem.skill) && (oldItem.tasks == newItem.tasks)
        }
    }

}