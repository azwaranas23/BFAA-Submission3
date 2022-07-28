package com.azwar.githubuser.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azwar.githubuser.R
import com.azwar.githubuser.databinding.ActivityFavoriteBinding
import com.azwar.githubuser.detail.DetailActivity
import com.azwar.githubuser.local.UserFavorite
import com.azwar.githubuser.main.ListUserAdapter
import com.azwar.githubuser.model.Users

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_favorite)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun OnItemClick(data: Users) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USER, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavoriteUser.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this, {
            if (it!=null){
                val list = mapList(it)
                adapter.setList(list)
            }
        })
    }

    private fun mapList(users: List<UserFavorite>): ArrayList<Users> {
        val listUsers = ArrayList<Users>()
        for (user in users){
            val userMapped = Users(
                user.login,
                user.id,
                user.avatar
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}