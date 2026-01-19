package com.fitnnestracker.adapters

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitnnestracker.databinding.LessonItemBinding
import com.fitnnestracker.models.Lesson
import androidx.core.net.toUri

class LessonAdapter(
    private val lessonList: ArrayList<Lesson?>

) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): LessonViewHolder {
        context = parent.context
        val binding = LessonItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LessonViewHolder(binding)
    }

    @SuppressLint("DiscouragedApi")
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int ) {
        holder.binding.Title.text = lessonList[position]!!.title
        holder.binding.WorkoutDuration.text = lessonList[position]!!.duration

        val resId = context!!.resources.getIdentifier(
            lessonList[position]!!.picUrl,
            "drawable",
            context!!.packageName
        )

        Glide.with(holder.itemView.context)
            .load(resId)
            .into(holder.binding.picUrl)

        holder.binding.StartWorkout.setOnClickListener {
            val  appIntent = Intent(Intent.ACTION_VIEW,("vnd.youtube" + lessonList[position]!!.link).toUri())
            val  webIntent = Intent(Intent.ACTION_VIEW,("https://www.youtube.com/watch?v="+lessonList[position]!!.link).toUri())

            try {
                context!!.startActivity(appIntent)
            }catch ( _ : ActivityNotFoundException){
                context!!.startActivity(webIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    class LessonViewHolder(val binding: LessonItemBinding) : RecyclerView.ViewHolder(binding.root)

}
