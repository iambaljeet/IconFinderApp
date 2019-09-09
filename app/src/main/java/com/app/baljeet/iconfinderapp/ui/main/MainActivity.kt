package com.app.baljeet.iconfinderapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.app.baljeet.iconfinderapp.R


class MainActivity : AppCompatActivity() {

    lateinit var mainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_iconfinder_icon)
        supportActionBar?.title = getString(R.string.toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainFragment.searchIcons(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}
