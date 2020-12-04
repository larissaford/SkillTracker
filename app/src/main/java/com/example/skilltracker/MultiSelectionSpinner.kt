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
    var skills: ArrayList<Skill>? = null
    var adapter: ArrayAdapter<*>
    private var selection: BooleanArray? = null

    constructor(context: Context) : super(context) {
        adapter = ArrayAdapter<Skill>(context,
            R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        adapter = ArrayAdapter<Skill>(context,
            R.layout.simple_spinner_item)
        super.setAdapter(adapter)
    }

    /**
     * Handles when the user clicks on a skill to select or un-select it
     *
     * @param dialog The dialog that is being viewed
     * @param idx The index of the skill that was clicked
     * @param isChecked If the skill is checked or not
     */
    override fun onClick(dialog: DialogInterface?, idx: Int, isChecked: Boolean) {
        if (selection != null && idx < selection!!.size) {
            selection!![idx] = isChecked
            adapter.clear()
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
        val itemNames = arrayOfNulls<String>(skills!!.size)
        for (i in skills!!.indices) {
            itemNames[i] = skills!![i].skillName
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
    fun setItems(items: ArrayList<Skill>) {
        this.skills = items
        selection = BooleanArray(this.skills!!.size)
        adapter.clear()
        Arrays.fill(selection!!, false)
    }

    /**
     * Used to set which options will be initially selected before the user clicks on the spinner
     */
    fun setSelection(selection: Array<Skill>) {
        for (i in 0 until this.selection!!.size) {
            this.selection!![i] = false
        }
        for (sel in selection) {
            for (j in 0 until skills!!.size) {
                if (skills!!.get(j).skillId.equals(sel.skillId)) {
                    this.selection!![j] = true
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
    fun getSelectedItems(): ArrayList<Skill> {
        val selectedItems: ArrayList<Skill> = ArrayList()
        for (i in 0 until skills!!.size) {
            if (selection!![i]) {
                selectedItems.add(skills!![i])
            }
        }
        return selectedItems
    }
}