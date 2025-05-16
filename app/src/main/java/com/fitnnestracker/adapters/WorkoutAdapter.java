package com.fitnnestracker.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitnnestracker.R;
import com.fitnnestracker.models.Workout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final List<Workout> workouts;

    public WorkoutAdapter(List<Workout> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);

        holder.workoutType.setText(workout.getType());
        holder.workoutDuration.setText(workout.getDuration() + " min");
        holder.workoutCalories.setText(workout.getCalories() + " Kcal");
        holder.workoutDate.setText(getFormattedDate(workout.getDate()));


        int iconRes = getIconForWorkoutType(workout.getType());
        holder.workoutIcon.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    private String getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        return sdf.format(date);
    }

    private int getIconForWorkoutType(String type) {
        switch (type) {
            case "Running": return R.drawable.ic_running;
            case "Walking": return R.drawable.ic_walking;
            case "Cycling": return R.drawable.ic_cycling;
            case "Swimming": return R.drawable.ic_swimming;
            case "Strength": return R.drawable.ic_strength;
            case "Yoga": return R.drawable.ic_yoga;
            case "Cardio": return R.drawable.ic_cardio;
            default: return R.drawable.ic_exercise;
        }
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView workoutIcon;
        TextView workoutType, workoutDuration, workoutCalories, workoutDate;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutIcon = itemView.findViewById(R.id.workout_icon);
            workoutType = itemView.findViewById(R.id.workout_type);
            workoutDuration = itemView.findViewById(R.id.workout_duration);
            workoutCalories = itemView.findViewById(R.id.workout_calories);
            workoutDate = itemView.findViewById(R.id.workout_date);
        }
    }
    public Workout getWorkoutAtPosition(int position) {
        return workouts.get(position);
    }

    public void removeWorkoutAtPosition(int position) {
        workouts.remove(position);
        notifyItemRemoved(position);
    }

}