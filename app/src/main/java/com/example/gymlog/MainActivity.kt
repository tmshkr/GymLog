package com.example.gymlog

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gymlog.databinding.ActivityMainBinding
import com.example.gymlog.db.GymLogDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: GymLogDatabase
    private var loggedInUserId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (loggedInUserId == -1) {
            val intent = LoginActivity.createIntent(this)
            startActivity(intent)
            return
        }
        
        db = GymLogDatabase.getDatabase(this)

        binding.LogButton.setOnClickListener {
            lifecycleScope.launch {
                insertGymLog()
            }
        }
    }

    private suspend fun insertGymLog() {
        val exerciseName = binding.ExerciseInputEditText.text.toString()
        val weight = binding.WeightInputEditText.text.toString()
        val reps = binding.RepInputEditText.text.toString()

        db.gymLogDao().insert(
            com.example.gymlog.db.GymLog(
                exerciseName = exerciseName,
                weight = weight,
                reps = reps,
                date = System.currentTimeMillis()
            )
        )

        val logMessage = "Exercise: $exerciseName, Reps: $reps, Weight: $weight"
        Toast.makeText(this, logMessage, Toast.LENGTH_LONG).show()
    }
}
