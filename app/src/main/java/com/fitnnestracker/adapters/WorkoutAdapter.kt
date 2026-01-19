package com.fitnnestracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitnnestracker.activities.WorkoutActivity
import com.fitnnestracker.databinding.WorkoutItemBinding
import com.fitnnestracker.models.Workout

class WorkoutAdapter(private val workoutList: ArrayList<Workout?>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder?>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        context = parent.context
        val binding =
            WorkoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.binding.Title.text = workoutList[position]!!.title
        holder.binding.WorkoutDuration.text =workoutList[position]!!.duration + "min"
        holder.binding.CaloriesCount.text = workoutList[position]!!.calories + "Kal"
        holder.binding.ExerciseCount.text = workoutList[position]!!.lesson.size.toString() + "Exercise"


        val resId = context!!.resources.getIdentifier(
            workoutList[position]!!.picUrl,
            "drawable",
            context!!.packageName
        )

        Glide.with(holder.itemView.context)
            .load(resId)
            .into(holder.binding.picUrl)

        holder.binding.root.setOnClickListener { v: View? ->
            val intent = Intent(context, WorkoutActivity::class.java)
            intent.putExtra("object", workoutList[position])
            context!!.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(val binding: WorkoutItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}
