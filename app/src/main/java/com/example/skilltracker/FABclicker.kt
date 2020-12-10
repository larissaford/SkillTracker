package com.example.skilltracker

import android.view.View
import androidx.fragment.app.Fragment

/**
 * An interface with only one abstract method is called a functional interface,
 * or a Single Abstract Method (SAM) interface.
 * The functional interface can have several non-abstract members but only one abstract member.
 */
interface FABclicker {
    fun onFABClicked(view: View)
}