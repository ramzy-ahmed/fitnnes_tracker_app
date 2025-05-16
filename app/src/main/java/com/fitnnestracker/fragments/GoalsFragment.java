package com.fitnnestracker.fragments;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fitnnestracker.database.DatabaseHelper;
import com.fitnnestracker.models.Goal;
import com.fitnnestracker.R;
import com.fitnnestracker.activities.AddGoalActivity;
import com.fitnnestracker.adapters.GoalsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class GoalsFragment extends Fragment {

    private RecyclerView goalsRecyclerView;
    private GoalsAdapter adapter;
    private FloatingActionButton fabAddGoal;
    private DatabaseHelper dbHelper;
    private TextView emptyRecordsTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        dbHelper = new DatabaseHelper(getContext());
        goalsRecyclerView = view.findViewById(R.id.goals_recycler_view);
        fabAddGoal = view.findViewById(R.id.fab_add_goal);
        emptyRecordsTextView = view.findViewById(R.id.emptyRecordsTextView);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshGoalsList();
            swipeRefreshLayout.setRefreshing(false);
        });
        setupRecyclerView();
        setupAddButton();
        updateEmptyViewVisibility();
        return view;
    }

    private void setupRecyclerView() {
        List<Goal> goals = dbHelper.getAllGoals();
        adapter = new GoalsAdapter(goals);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateEmptyViewVisibility();
        goalsRecyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Goal goal = adapter.getGoalAtPosition(position);
                showCustomDialog(goal.getId(), position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);


                if (dX > 0) {
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
                } else if (dX < 0) {
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                }

                Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete);
                assert icon != null;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                if (dX > 0) {
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = iconLeft + icon.getIntrinsicWidth();
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconLeft = iconRight - icon.getIntrinsicWidth();
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconBottom = iconTop + icon.getIntrinsicHeight();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                }

                icon.draw(c);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(goalsRecyclerView);
    }

    private void setupAddButton() {
        fabAddGoal.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddGoalActivity.class);
            startActivity(intent);
            updateEmptyViewVisibility();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateEmptyViewVisibility();
        refreshGoalsList();
    }

    private void refreshGoalsList() {
        List<Goal> updatedGoals = dbHelper.getAllGoals();
        adapter = new GoalsAdapter(updatedGoals);
        goalsRecyclerView.setAdapter(adapter);
    }

    private void showCustomDialog(int goalId, int position) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.delete_item);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            dbHelper.deleteGoal(goalId);
            adapter.removeGoalAtPosition(position);
            updateEmptyViewVisibility();
            Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            refreshGoalsList();
        });
        dialog.show();
    }

    private void updateEmptyViewVisibility() {
        if (dbHelper.getAllGoals().isEmpty()) {
            emptyRecordsTextView.setVisibility(View.VISIBLE);
            goalsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyRecordsTextView.setVisibility(View.GONE);
            goalsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}