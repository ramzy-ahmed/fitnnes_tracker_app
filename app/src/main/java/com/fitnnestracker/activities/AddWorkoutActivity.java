package com.fitnnestracker.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fitnnestracker.database.DatabaseHelper;
import com.fitnnestracker.R;
import com.fitnnestracker.models.Workout;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Calendar;
import java.util.Date;

public class AddWorkoutActivity extends AppCompatActivity {

    private AutoCompleteTextView exerciseTypeDropdown;
    private EditText durationEditText, caloriesEditText, dateEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private final Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupDropdown();
        setupDatePicker();
        setupSaveButton();
    }

    private void initViews() {
        exerciseTypeDropdown = findViewById(R.id.exercise_type_dropdown);
        durationEditText = findViewById(R.id.duration_edit_text);
        caloriesEditText = findViewById(R.id.calories_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        saveButton = findViewById(R.id.save_button);
    }

    private void setupDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exercise_types, android.R.layout.simple_dropdown_item_1line);
        exerciseTypeDropdown.setAdapter(adapter);
    }

    private void setupDatePicker() {
        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        dateEditText.setText(getFormattedDate(selectedDate.getTime()));
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate.set(year, month, dayOfMonth);
            dateEditText.setText(getFormattedDate(selectedDate.getTime()));
        }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private String getFormattedDate(Date date) {
        return android.text.format.DateFormat.getDateFormat(this).format(date);
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveWorkout());
    }

    private void saveWorkout() {
        String type = exerciseTypeDropdown.getText().toString();
        String duration = durationEditText.getText().toString().trim();
        String calories = caloriesEditText.getText().toString().trim();

        if (duration.isEmpty()) {
            durationEditText.setError("Please enter the duration");
            return;
        }

        if (calories.isEmpty()) {
            calories = "0";
        }
        Workout workout = new Workout(type, duration, calories, selectedDate.getTime());
        long id = dbHelper.addWorkout(workout);

        if (id != -1) {
            Toast.makeText(this, "The exercise has been successfully saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "An error occurred while saving.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
