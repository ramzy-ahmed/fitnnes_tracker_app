package com.fitnnestracker.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitnnestracker.database.DatabaseHelper;
import com.fitnnestracker.R;
import com.fitnnestracker.models.Workout;
import com.fitnnestracker.activities.AddWorkoutActivity;
import com.fitnnestracker.adapters.WorkoutAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class WorkoutsFragment extends Fragment {
    private TextView emptyRecordsTextView;
    private RecyclerView workoutsRecycler;
    private WorkoutAdapter adapter;
    private FloatingActionButton fabAddWorkout;
    private DatabaseHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        dbHelper = new DatabaseHelper(getContext());
        workoutsRecycler = view.findViewById(R.id.workouts_recycler);
        fabAddWorkout = view.findViewById(R.id.fab_add_workout);
        emptyRecordsTextView =view.findViewById(R.id.emptyRecordsTextView);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshWorkoutsList();
            swipeRefreshLayout.setRefreshing(false);
        });
        setupRecyclerView();
        setupFabButton();
        updateEmptyViewVisibility();
        return view;
    }

    private void setupRecyclerView() {
        List<Workout> workouts = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(workouts);

        workoutsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        workoutsRecycler.setAdapter(adapter);
    }

    private void setupFabButton() {
        fabAddWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddWorkoutActivity.class);
            startActivity(intent);
            updateEmptyViewVisibility();
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Workout workout = adapter.getWorkoutAtPosition(position);
                showCustomDialog(workout.getId(), position);
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
        itemTouchHelper.attachToRecyclerView(workoutsRecycler);

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshWorkoutsList();
        updateEmptyViewVisibility();
    }

    private void refreshWorkoutsList() {
        List<Workout> updatedWorkouts = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(updatedWorkouts);
        workoutsRecycler.setAdapter(adapter);
    }

    private void showCustomDialog(int goalId, int position) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.delete_item);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(v -> {
            dbHelper.deleteWorkout(goalId);
            adapter.removeWorkoutAtPosition(position);
            updateEmptyViewVisibility();
            Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            refreshWorkoutsList();
        });
        dialog.show();
    }


    private void updateEmptyViewVisibility() {
        if (dbHelper.getAllWorkouts().isEmpty()) {
            emptyRecordsTextView.setVisibility(View.VISIBLE);
            workoutsRecycler.setVisibility(View.GONE);
        } else {
            emptyRecordsTextView.setVisibility(View.GONE);
            workoutsRecycler.setVisibility(View.VISIBLE);
        }
    }
}