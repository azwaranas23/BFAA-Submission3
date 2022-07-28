package com.azwar.githubuser.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azwar.githubuser.R
import com.azwar.githubuser.databinding.ActivityMainBinding
import com.azwar.githubuser.detail.DetailActivity
import com.azwar.githubuser.favorite.FavoriteActivity
import com.azwar.githubuser.model.Users
import com.azwar.githubuser.settings.SettingPreferences
import com.azwar.githubuser.themes.ThemeViewModel
import com.azwar.githubuser.themes.ThemeViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel : MainViewModel
    private lateinit var adapter: ListUserAdapter
    private lateinit var themeViewModel: ThemeViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun OnItemClick(data: Users) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            ivSearch.setOnClickListener{
                searchUser()
            }

            etQuery.setOnKeyListener { view, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUser().observe(this, {
            if (it!= null){
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun searchUser(){
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val themeSwitchButton = menu.findItem(R.id.theme_switch_button)?.actionView as SwitchMaterial
        val pref = SettingPreferences.newInstance(dataStore)

        themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(ThemeViewModel::class.java)
        themeViewModel.getThemeMode().observe(this, { isNightModeActive: Boolean ->
            if (isNightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themeSwitchButton.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themeSwitchButton.isChecked = false
            }
        })

        themeSwitchButton.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.setThemeMode(isChecked)
        }

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also{
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}