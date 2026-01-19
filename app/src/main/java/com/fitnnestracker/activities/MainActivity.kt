package com.fitnnestracker.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitnnestracker.R
import com.fitnnestracker.adapters.WorkoutAdapter
import com.fitnnestracker.databinding.ActivityMainBinding
import com.fitnnestracker.models.Lesson
import com.fitnnestracker.models.Workout

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.getRoot())

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main)!!,
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            })

        binding!!.ExerciseList.setLayoutManager(
            LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        binding!!.ExerciseList.setAdapter(WorkoutAdapter(this.data))
    }

    private val data: ArrayList<Workout?>
        get() {
            val list = java.util.ArrayList<Workout?>()
            list.add(
                Workout(
                    "Running",
                    "You just woke up. It is a brand new day. The canvas is blank. How do you begin? Take 9 minutes to achieve your dream body",
                    "9",
                    "160",
                    "pic_1",
                    this.lesson1
                )
            )
            list.add(
                Workout(
                    "Stretching",
                    "You just woke up. It is a brand new day. The canvas is blank. How do you begin? Take 85 minutes to achieve your dream body",
                    "85",
                    "230",
                    "pic_2",
                    this.lesson2
                )
            )
            list.add(
                Workout(
                    "Yoga",
                    "You just woke up. It is a brand new day. The canvas is blank. How do you begin? Take 65 minutes to achieve your dream body",
                    "65",
                    "180",
                    "pic_3",
                    this.lesson3
                )
            )
            return list
        }

    private val lesson1: ArrayList<Lesson?>
        get() {
            val list = java.util.ArrayList<Lesson?>()
            list.add(Lesson("Lesson 1", "03:46 ", "pic_1_1", "HBPMvFkpNgE"))
            list.add(Lesson("Lesson 2", "03:41 ", "pic_1_2", "K6124WqiiPw"))
            list.add(Lesson("Lesson 3", "01:57 ", "pic_1_3", "Zc08v4YYOeA"))
            return list
        }

    private val lesson2: ArrayList<Lesson?>
        get() {
            val list = java.util.ArrayList<Lesson?>()
            list.add(Lesson("Lesson 1", "20:23 ", "pic_2_1", "L3eImBAXT7I"))
            list.add(Lesson("Lesson 2", "18:27 ", "pic_2_2", "47Exgz07FLU"))
            list.add(Lesson("Lesson 3", "32:25 ", "pic_2_3", "OmLx8tmaQ-4"))
            list.add(Lesson("Lesson 4", "07:52 ", "pic_2_4", "w86EaLEoFRY"))
            return list
        }

    private val lesson3: ArrayList<Lesson?>
        get() {
            val list = java.util.ArrayList<Lesson?>()
            list.add(Lesson("Lesson 1", "23:00 ", "pic_3_1", "v7AYKMP6rDE"))
            list.add(Lesson("Lesson 2", "27:00 ", "pic_3_2", "Eml2xnoLpYE"))
            list.add(Lesson("Lesson 3", "25:00 ", "pic_3_3", "v7SN-d4qXx0"))
            list.add(Lesson("Lesson 4", "21:00 ", "pic_3_4", "LqXZ628YNj4"))

            return list
        }
}