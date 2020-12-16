package com.example.skilltracker.database.adpater

import android.content.Context
import android.graphics.Color
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
import com.example.skilltracker.TaskFragmentDirections
import com.example.skilltracker.database.entity.Task
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

/**
 * This is the adaptor to take the views and put them into the layout, and then take the data and
 * fill in those views within the layout.
 *
 * @param context a private context
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
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskActive: TextView = itemView.findViewById(R.id.task_is_active)
        val taskCompleted: TextView = itemView.findViewById(R.id.task_completed)
        val dateCompleted: TextView = itemView.findViewById(R.id.task_date_completed)
        val dateCreated: TextView = itemView.findViewById(R.id.task_date_created)
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
        holder.taskDescription.text = task.taskDescription
        holder.taskActive.text = if (task.active) " Yes" else " No"
        holder.taskCompleted.text = if (task.taskCompleted)  "Yes" else "No"

        // Format date and set it to the viewHolder
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        holder.dateCreated.text = task.taskDateCreated.format(formatter).toString()

        // If the task does not have a description, hide the text view
        if (task.taskDescription.isNullOrBlank()) {
            holder.taskDescription.visibility = View.GONE
        }

        // If the task is completed, hide the active text views & add the date completed to the view holder.
        if (task.taskCompleted) {

            holder.taskActive.visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.active_label).visibility = View.GONE
            holder.dateCompleted.text = task.taskDateCompleted!!.format(formatter).toString()

            holder.itemView.findViewById<CardView>(R.id.card_view).setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_green))
        }
        else { // Otherwise hide the date completed on text views
            holder.dateCompleted.visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.task_completed_on_label).visibility = View.GONE
            holder.itemView.findViewById<CardView>(R.id.card_view).setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        // If the task is active, it can't be completed, so hide the completed on text views
        if (task.active) {
            holder.taskCompleted.visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.completed_label).visibility = View.GONE
            holder.dateCompleted.visibility = View.GONE
            holder.itemView.findViewById<TextView>(R.id.task_completed_on_label).visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            task.taskCompleted = !task.taskCompleted
            notifyItemChanged(position)
        }

        //Long Clicks allow for editing the Skill
        holder.itemView.setOnLongClickListener { view: View ->
            view.findNavController().navigate(
                TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(task, null)
            )
            true
        }


    }

    private fun makeColored(view: View){

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