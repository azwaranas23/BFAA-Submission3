package com.azwar.githubuser.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.azwar.githubuser.local.UserDatabase
import com.azwar.githubuser.local.UserFavorite
import com.azwar.githubuser.local.UserFavoriteDAO

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var userDAO: UserFavoriteDAO?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDAO = userDB?.userFavoriteDAO()
    }

    fun getFavoriteUser(): LiveData<List<UserFavorite>>?{
        return userDAO?.getFavoriteUser()
    }
}