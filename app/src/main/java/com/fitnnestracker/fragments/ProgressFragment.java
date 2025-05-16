package com.fitnnestracker.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fitnnestracker.database.DatabaseHelper;
import com.fitnnestracker.models.Goal;
import com.fitnnestracker.R;
import com.fitnnestracker.models.Workout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class ProgressFragment extends Fragment {
    private TextView weeklyAvg, bestDay;
    private BarChart progressChart;
    private TextView totalWorkouts, totalCalories, achievedGoals;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        dbHelper = new DatabaseHelper(getContext());
        progressChart = view.findViewById(R.id.progress_chart);
        totalWorkouts = view.findViewById(R.id.total_workouts);
        totalCalories = view.findViewById(R.id.total_calories);
        achievedGoals = view.findViewById(R.id.achieved_goals);
        weeklyAvg = view.findViewById(R.id.weekly_avg);
        bestDay = view.findViewById(R.id.best_day);

        setupStats();
        setupChart();
        setupAdditionalStats();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupStats() {
        int workoutsCount = dbHelper.getAllWorkouts().size();
        int goalsCount = dbHelper.getAllGoals().size();

        totalWorkouts.setText(String.valueOf(workoutsCount));
        totalCalories.setText(calculateTotalCalories() + " kcal");
        achievedGoals.setText(calculateAchievedGoals() + "/" + goalsCount);
    }

    private int calculateTotalCalories() {
        int total = 0;
        for (Workout workout : dbHelper.getAllWorkouts()) {
            try {
                total += Integer.parseInt(workout.getCalories());
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        return total;
    }

    private int calculateAchievedGoals() {
        int achieved = 0;
        for (Goal goal : dbHelper.getAllGoals()) {
            if (goal.getProgressPercentage() >= 100) {
                achieved++;
            }
        }
        return achieved;
    }

    private void setupChart() {
        List<BarEntry> entries = new ArrayList<>();
        List<Workout> workouts = dbHelper.getAllWorkouts();

        for (int i = 0; i < Math.min(7, workouts.size()); i++) {
            Workout workout = workouts.get(i);
            try {
                entries.add(new BarEntry(i, Float.parseFloat(workout.getCalories())));
            } catch (NumberFormatException e) {
                entries.add(new BarEntry(i, 0));
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "calories burned");
        dataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
        BarData barData = new BarData(dataSet);
        progressChart.setData(barData);
        progressChart.getDescription().setEnabled(false);
        progressChart.animateY(1000);
        progressChart.invalidate();
    }

    @SuppressLint("SetTextI18n")
    private void setupAdditionalStats() {
        weeklyAvg.setText(calculateWeeklyAverage() + " minutes/day");
        bestDay.setText(getBestDay());
    }

    private String calculateWeeklyAverage() {
        List<Workout> workouts = dbHelper.getLastWeekWorkouts();
        if (workouts.isEmpty()) return "0";

        int totalMinutes = 0;
        for (Workout w : workouts) {
            try {
                totalMinutes += Integer.parseInt(w.getDuration());
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(totalMinutes / 7);
    }

    private String getBestDay() {
        return "Friday"; //example
    }
}