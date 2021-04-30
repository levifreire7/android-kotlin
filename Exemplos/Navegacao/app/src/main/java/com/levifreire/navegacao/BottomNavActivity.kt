package com.levifreire.navegacao

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavActivity : AppCompatActivity() {
    private val tabTitles: Array<String> by lazy { resources.getStringArray(R.array.sections) }
    private val bgColors: TypedArray by lazy { resources.obtainTypedArray(R.array.bg_colors) }
    private val textColors: TypedArray by lazy { resources.obtainTypedArray(R.array.text_colors) }
    private val tabsIds = listOf(R.id.action_favorites, R.id.action_clock, R.id.action_people)
    private var currentTabIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        currentTabIndex = savedInstanceState?.getInt(TAB_SELECTED) ?: 0
        showFragment(currentTabIndex)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val index = tabsIds.indexOf(item.itemId)
            showFragment(index)
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_SELECTED, currentTabIndex)
    }

    private fun showFragment(position: Int) {
        val oldTag = "aba_$currentTabIndex"
        val newTag = "aba_$position"
        currentTabIndex = position
        val transaction = supportFragmentManager.beginTransaction()
        val oldFragment = supportFragmentManager.findFragmentByTag(oldTag)
        if (oldFragment != null) {
            transaction.hide(oldFragment)
        }
        var fragment = supportFragmentManager.findFragmentByTag(newTag)
        if (fragment == null) {
            fragment = SecondLevelFragment.newInstance(
                tabTitles[position],
                bgColors.getColor(position, 0),
                textColors.getColor(position, 0)
            )
            transaction.add(R.id.container, fragment, newTag)
        }
        val rootView = findViewById<LinearLayout>(R.id.rootView)
        rootView.setBackgroundColor(bgColors.getColor(currentTabIndex, 0))
        transaction.show(fragment).commit()
    }

    companion object {
        const val TAB_SELECTED = "tabSelected"
    }
}