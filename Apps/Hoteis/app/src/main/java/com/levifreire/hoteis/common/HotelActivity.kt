package com.levifreire.hoteis.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.levifreire.hoteis.*
import com.levifreire.hoteis.databinding.ActivityHotelBinding
import com.levifreire.hoteis.details.HotelDetailsActivity
import com.levifreire.hoteis.details.HotelDetailsFragment
import com.levifreire.hoteis.form.HotelFormFragment
import com.levifreire.hoteis.list.HotelListFragment
import com.levifreire.hoteis.model.Hotel

class MainActivity : AppCompatActivity(), HotelListFragment.OnHotelClickListener,
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener,
    HotelFormFragment.OnHotelSavedListener, HotelListFragment.OnHotelDeletedListener {

    private var lastSearchTerm: String = ""
    private var searchView: SearchView? = null
    private val listFragment: HotelListFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment }
    private var hotelIdSelected: Long = -1
    private lateinit var binding: ActivityHotelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.fabAdd.setOnClickListener {
            listFragment.hideDeleteMode()
            HotelFormFragment.newInstance().open(supportFragmentManager)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(EXTRA_HOTEL_ID_SELECTED, hotelIdSelected)
        outState.putString(EXTRA_SEARCH_TERM, lastSearchTerm)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hotelIdSelected = savedInstanceState.getLong(EXTRA_HOTEL_ID_SELECTED)
        lastSearchTerm = savedInstanceState.getString(EXTRA_SEARCH_TERM) ?: ""
    }

    override fun onHotelClick(hotel: Hotel) {
        if (isTablet()) {
            hotelIdSelected = hotel.id
            showDetailsFragment(hotel.id)
        } else {
            showDetailsActivity(hotel.id)
        }
    }

    private fun showDetailsFragment(hotelId: Long) {
        searchView?.setOnQueryTextListener(null)
        val fragment = HotelDetailsFragment.newInstance(hotelId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

    private fun isTablet() = resources.getBoolean(R.bool.tablet)
    //private fun isSmartphone() = resources.getBoolean(R.bool.smartphone)

    private fun showDetailsActivity(hotelId: Long) {
        HotelDetailsActivity.open(this, hotelId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)
        if (lastSearchTerm.isNotEmpty()) {
            Handler().post {
                val query = lastSearchTerm
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_info ->
                AboutDialogFragment().show(supportFragmentManager, "sobre")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        lastSearchTerm = newText ?: ""
        listFragment.search(lastSearchTerm)
        return true
    }

    override fun onMenuItemActionExpand(item: MenuItem?) = true

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        lastSearchTerm = ""
        listFragment.clearSearch()
        return true
    }

    override fun onHotelSaved(hotel: Hotel) {
        listFragment.search(lastSearchTerm)
        val detailsFragment =
            supportFragmentManager.findFragmentByTag(HotelDetailsFragment.TAG_DETAILS) as? HotelDetailsFragment
        if (detailsFragment != null && hotel.id == hotelIdSelected) {
            showDetailsFragment(hotelIdSelected)
        }
    }

    override fun onHotelsDeleted(hotels: List<Hotel>) {
        if (hotels.find { it.id == hotelIdSelected } != null) {
            val fragment =
                supportFragmentManager.findFragmentByTag(HotelDetailsFragment.TAG_DETAILS)
            if (fragment != null) {
                supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            listFragment.search(lastSearchTerm)
        }
    }

    companion object {
        const val EXTRA_SEARCH_TERM = "lastSearch"
        const val EXTRA_HOTEL_ID_SELECTED = "lastSelectedID"
    }
}