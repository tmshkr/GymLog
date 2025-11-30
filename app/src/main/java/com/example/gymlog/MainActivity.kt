package com.example.gymlog

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymlog.data.AppRepository
import com.example.gymlog.databinding.ActivityMainBinding
import com.example.gymlog.viewmodels.GymLogAdapter
import com.example.gymlog.viewmodels.GymLogViewModel
import com.example.gymlog.viewmodels.GymLogViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val repo by lazy { AppRepository.getInstance(this) }

    private lateinit var gymLogViewModel: GymLogViewModel

    private lateinit var sharedPref: SharedPreferences


    private var loggedInUserId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref =
            getSharedPreferences("GymLogPrefs", Context.MODE_PRIVATE)


        if (intent.hasExtra(USER_ID)) {
            loggedInUserId = intent.getIntExtra(USER_ID, -1)
            // save to shared preferences
            sharedPref.edit {
                putInt(USER_ID, loggedInUserId)
            }
        } else if (sharedPref.contains(USER_ID)) {
            loggedInUserId = sharedPref.getInt(USER_ID, -1)
        }

        if (loggedInUserId == -1) {
            val intent = LoginActivity.createIntent(this)
            startActivity(intent)
            return
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = GymLogAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        gymLogViewModel = GymLogViewModelFactory(repo, loggedInUserId)
            .create(GymLogViewModel::class.java)

        gymLogViewModel.allGymLogsForUser.observe(this) { gymLogs ->
            gymLogs.let { adapter.submitList(gymLogs) }
        }



        binding.LogButton.setOnClickListener {
            lifecycleScope.launch {
                insertGymLog()
            }
        }

        binding.LogoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        loggedInUserId = -1
        sharedPref.edit {
            remove(USER_ID)
        }
        val intent = LoginActivity.createIntent(this)
        startActivity(intent)
    }

    private suspend fun insertGymLog() {
        val exerciseName = binding.ExerciseInputEditText.text.toString()
        val weight = binding.WeightInputEditText.text.toString().toInt()
        val reps = binding.RepInputEditText.text.toString().toInt()

        repo.insertGymLog(
            com.example.gymlog.data.GymLog(
                exerciseName = exerciseName,
                userId = loggedInUserId,
                weight = weight,
                reps = reps,
                date = System.currentTimeMillis()
            )
        )

        val logMessage = "Exercise: $exerciseName, Reps: $reps, Weight: $weight"
        Toast.makeText(this, logMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        val USER_ID = "com.example.gymlog.USER_ID"
        fun createIntent(context: Context, userId: Int): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(USER_ID, userId)
            return intent
        }
    }
}
