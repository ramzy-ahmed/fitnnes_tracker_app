package com.fitnnestracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitnnestracker.models.Goal;
import com.fitnnestracker.R;

import java.util.List;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalViewHolder> {
    private List<Goal> goals;

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;

    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goals.get(position);

        holder.goalName.setText(goal.getName());
        holder.goalType.setText(goal.getType());
        holder.progressText.setText(String.format("%s/%s", goal.getCurrentValue(), goal.getTargetValue()));

        try {
            float current = Float.parseFloat(goal.getCurrentValue());
            float target = Float.parseFloat(goal.getTargetValue());
            int progress = (int) ((current / target) * 100);
            holder.progressBar.setProgress(Math.min(progress, 100));
        } catch (NumberFormatException e) {
            holder.progressBar.setProgress(0);
        }

    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        TextView goalName, goalType, progressText;
        ProgressBar progressBar;

        public GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            goalName = itemView.findViewById(R.id.goal_name);
            goalType = itemView.findViewById(R.id.goal_type);
            progressText = itemView.findViewById(R.id.progress_text);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public Goal getGoalAtPosition(int position) {
        return goals.get(position);
    }

    public void removeGoalAtPosition(int position) {
        goals.remove(position);
        notifyItemRemoved(position);
    }
}
