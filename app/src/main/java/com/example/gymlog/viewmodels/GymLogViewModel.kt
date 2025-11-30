package com.example.gymlog.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.gymlog.data.AppRepository
import com.example.gymlog.data.GymLog

class GymLogViewModel(private val repo: AppRepository, private val userId: Int) : ViewModel() {

    val allGymLogsForUser: LiveData<List<GymLog>> = repo.getGymLogByUserId(userId).asLiveData()

}

class GymLogViewModelFactory(private val repository: AppRepository, private val userId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GymLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GymLogViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}