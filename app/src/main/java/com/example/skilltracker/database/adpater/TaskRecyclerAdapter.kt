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
import com.example.skilltracker.TaskFragmentDirections
import com.example.skilltracker.database.entity.Task

/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
 * @param skills a list of previous skills for the SkillSet
 * @property layoutInflater for inflating the recycler view
 */
class TaskRecyclerAdapter (private val context: Context, private var tasks: List<Task>) :
    PagingDataAdapter<Task, TaskRecyclerAdapter.ViewHolder>(TaskDiffCallBack()) {
    private val layoutInflater = LayoutInflater.from(context)

    /**
     * gets references to the views we want to use within the previous_order_list layout by
     * calling onCreateViewHolder multiple times.
     *
     * @param itemView the inflated previous_order_list layout
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById<TextView?>(R.id.task_name)
        val taskCompleted: TextView = itemView.findViewById<TextView?>(R.id.task_completed)
        val dateCreated: TextView = itemView.findViewById<TextView?>(R.id.task_date_created)
        var task: Task? = null
    }

    /**
     * gets references to the views we want to use within the previous_order_list layout.
     *
     * @param parent is the ViewGroup
     * @param viewType is the type of view and an int
     * @return returns the reference to the view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.task_list, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * This method binds the views with the data
     *
     * @param holder the view holder we made that contains the references to the views
     * @param position the position of the previously ordered SkillSet in the previous orders list
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.taskName
        holder.taskCompleted.text = if (task.taskCompleted)  "Yes" else "No"
        holder.dateCreated.text = task.taskDateCreated.toLocalDate().toString()

        //Long Clicks allow for editing the Skill
        holder.itemView.setOnLongClickListener { view: View ->
            view.findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(task, null)
            )
            true
        }
    }

    /**
     * This answers how much data overall will be displayed
     *
     * @return the size of the previous orders list
     */
    override fun getItemCount() = tasks.size

    /**
     *  Boiler plate code for the Paging Data Adaptor to work, which is not really used yet but
     *  is for better practices when adding data to the database I think.
     */
    private class TaskDiffCallBack : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

}