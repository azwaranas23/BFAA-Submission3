package com.azwar.githubuser.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azwar.githubuser.api.RetrofitClient
import com.azwar.githubuser.local.UserDatabase
import com.azwar.githubuser.local.UserFavorite
import com.azwar.githubuser.local.UserFavoriteDAO
import com.azwar.githubuser.model.DetailUsersResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUsersResponse>()

    private var userDAO: UserFavoriteDAO?
    private var userDB: UserDatabase?

    init {
        userDB = UserDatabase.getDatabase(application)
        userDAO = userDB?.userFavoriteDAO()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstace
            .getDetailUsers(username)
            .enqueue(object : Callback<DetailUsersResponse> {
                override fun onResponse(
                    call: Call<DetailUsersResponse>,
                    response: Response<DetailUsersResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUsersResponse>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUsersResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatar: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = UserFavorite(
                username,
                id,
                avatar
            )
            userDAO?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDAO?.checkUser(id)

    fun removeUser(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDAO?.removeUser(id)
        }
    }
}