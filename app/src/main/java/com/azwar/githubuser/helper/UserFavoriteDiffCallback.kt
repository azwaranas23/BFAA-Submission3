package com.azwar.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.azwar.githubuser.local.UserFavorite

class UserFavoriteDiffCallback(
    private val mOldFavoriteUserList: List<UserFavorite>,
    private val mNewFavoriteUserList: List<UserFavorite>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return mOldFavoriteUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavoriteUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavoriteUserList[oldItemPosition].id == mNewFavoriteUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavoriteUser = mOldFavoriteUserList[oldItemPosition]
        val newFavoriteUser = mNewFavoriteUserList[newItemPosition]
        return oldFavoriteUser.login == newFavoriteUser.login && oldFavoriteUser.avatar == newFavoriteUser.avatar
    }

}