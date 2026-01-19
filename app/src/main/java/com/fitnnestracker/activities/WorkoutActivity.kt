package com.fitnnestracker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fitnnestracker.R
import com.fitnnestracker.adapters.LessonAdapter
import com.fitnnestracker.databinding.ActivityWorkoutBinding
import com.fitnnestracker.models.Workout

class WorkoutActivity : AppCompatActivity() {
    var binding: ActivityWorkoutBinding? = null
    var workout : Workout? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityWorkoutBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main)!!,
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            })

        binding!!.lessonList.setLayoutManager(
            LinearLayoutManager(
                this@WorkoutActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        getData()
        binding!!.lessonList.setAdapter(LessonAdapter(workout!!.lesson))

        binding!!.backBtn.setOnClickListener {
            finish()
        }

        val resId = resources.getIdentifier(workout!!.picUrl,"drawable",packageName)

        Glide.with(this@WorkoutActivity)
            .load(resId)
            .into(binding!!.picUrl)

        binding!!.title.text = workout!!.title
        binding!!.WorkoutDuration.text = workout!!.duration+" min"
        binding!!.CaloriesCount.text = workout!!.calories+" Kal"
        binding!!.ExerciseCount.text = workout!!.lesson.size.toString()+" exercise"
        binding!!.description.text = workout!!.discretion
    }

    private fun getData(){
        workout = intent.getSerializableExtra("object") as Workout?
    }
}