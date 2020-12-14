package com.example.skilltracker

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.Task
import timber.log.Timber
import java.util.*

/**
 * Custom multi-select spinner class
 *
 * @property skills All of the skills that will be displayed in the spinner
 * @property adapter An ArrayAdapter for the spinner
 * @property selection A boolean array that stores whether each skill in the skills array is checked or not
 */
class MultiSelectionSpinner : AppCompatSpinner, OnMultiChoiceClickListener {
    var values: ArrayList<Any>? = null
    var adapter: ArrayAdapter<*>
    var selectionChanged: Boolean = false
    private var selection: BooleanArray? = null

    constructor(context: Context) : super(context) {
        adapter = ArrayAdapter<Any>(context,
            R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        adapter = ArrayAdapter<Any>(context,
            R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    /**
     * Handles when the user clicks on a value to select or un-select it
     *
     * @param dialog The dialog that is being viewed
     * @param idx The index of the value that was clicked
     * @param isChecked If the value is checked or not
     */
    override fun onClick(dialog: DialogInterface?, idx: Int, isChecked: Boolean) {
        if (selection != null && idx < selection!!.size) {
            selection!![idx] = isChecked
            adapter.clear()
            selectionChanged = true
        } else {
            throw IllegalArgumentException(
                "'idx' is out of bounds.")
        }
    }

    /**
     * Called when the user clicks on the spinner initially. Opens the dialog
     *  containing the multi-select functionality
     */
    override fun performClick(): Boolean {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val itemNames = arrayOfNulls<String>(values!!.size)

        for (i in values!!.indices) {
            if (values!![i] is Skill) {
                val skill = values!![i] as Skill
                itemNames[i] = skill.skillName
            }
            else if (values!![i] is Task) {
                val task = values!![i] as Task
                itemNames[i] = task.taskName
            }
        }

        builder.setMultiChoiceItems(itemNames, selection, this)
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { arg0, arg1 ->
            // Do nothing
        })
        builder.show()
        return true
    }

    /**
     * Not being used for now, so just throw an error
     */
    override fun setAdapter(adapter: SpinnerAdapter?) {
        throw RuntimeException(
            "setAdapter is not supported by MultiSelectSpinner.")
    }

    /**
     * Sets the skills that will be shown to the user in the spinner
     */
    fun setItems(items: ArrayList<Any>) {
        this.values = items
        selection = BooleanArray(this.values!!.size)
        adapter.clear()
        Arrays.fill(selection!!, false)
    }

    /**
     * Used to set which options will be initially selected before the user clicks on the spinner
     */
    fun setSelection(selection: ArrayList<Any>) {
        for (i in 0 until this.selection!!.size) {
            this.selection!![i] = false
        }

        if (selection.size > 0) {
            if (selection[0] is Skill) {
                for (sel in selection) {
                    val selectedSkill = sel as Skill
                    for (j in 0 until values!!.size) {
                        var skill = values!![j] as Skill
                        if (skill.skillId == selectedSkill.skillId) {
                            this.selection!![j] = true
                        }
                    }
                }
            }
            else if (selection[0] is Task) {
                for (sel in selection) {
                    val selectedTask = sel as Task
                    for (j in 0 until values!!.size) {
                        val task = values!![j] as Task
                        if (task.taskId == selectedTask.taskId) {
                            this.selection!![j] = true
                        }
                    }
                }
            }
        }

        adapter.clear()
    }

//    /**
//     * Builds a string of the selected items info
//     */
//    private fun buildSelectedItemString(): String? {
//        val sb = StringBuilder()
//        var foundOne = false
//        for (i in 0 until skills!!.size) {
//            if (selection!![i]) {
//                if (foundOne) {
//                    sb.append(", ")
//                }
//                foundOne = true
//                sb.append(skills!![i].skillName)
//            }
//        }
//        return sb.toString()
//    }

    /**
     * Returns a list selected/checked items/skills
     * @return A list of all of the skills the user selected
     */
    fun getSelectedItems(): ArrayList<Any> {
        val selectedItems: ArrayList<Any> = ArrayList()
        for (i in 0 until values!!.size) {
            if (selection!![i]) {
                selectedItems.add(values!![i])
            }
        }
        return selectedItems
    }
}