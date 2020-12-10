package com.example.skilltracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.skilltracker.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    //private lateinit var callback: OrderFragment.OnOrderSelectedListener
    private lateinit var skillTimer: SkillTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))

        skillTimer = SkillTimer(this.lifecycle)
        Timber.i("onCreate called")

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(navController, appBarConfiguration)

//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            // To-Do: make it so you don't need to say "... as certainFragment"
//            // val currentFragment = getCurrentFragment() as FABclicker
//            // currentFragment.onFABClicked(view)
//
//            when (val currentFragment = getCurrentFragment()) {
//                is SkillSetFragment -> {
//                    currentFragment.onFABClicked(view)
//                }
//                is SkillFragment -> currentFragment.onFABClicked(view)
//                is TaskFragment -> currentFragment.onFABClicked(view)
//            }
//
//            val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
//            if(navHostFragment != null) {
//                val childFragments = navHostFragment.childFragmentManager.fragments as List<FABclicker>
//                childFragments.forEach { fragment ->
//                    fragment.onFABClicked(view)
//                }
//            }
//
//
//        }
    }
    /**
     * sets up the nav Controller and the back button in the app bar.
     * @return Boolean
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        //return NavigationUI.navigateUp(drawerLayout, navController)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getCurrentFragment(): Fragment?{
        //val navFrag = supportFragmentManager.findFragmentById(R.id.myNavHostFragment)
        //return navFrag?.childFragmentManager?.fragments?.get(0)
        val navHostFragment = supportFragmentManager.primaryNavigationFragment
        return navHostFragment!!.childFragmentManager.fragments[0]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Closes the user's keyboard if it is currently open
     * @param activity The activity that is being interacted with
     * @param fragment The fragment the user is viewing
     */
    fun closeKeyboardFromFragment(activity: Activity, fragment: Fragment) {
        Timber.i("Hiding keyboard")
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view: View = fragment.view?.rootView as View

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Sets the FAB's visibility to visible
     */
    fun showFAB() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.visibility = View.VISIBLE
    }

    /**
     * Sets the FAB's visibility to invisible
     */
    fun hideFAB() {
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.visibility = View.INVISIBLE
    }
}