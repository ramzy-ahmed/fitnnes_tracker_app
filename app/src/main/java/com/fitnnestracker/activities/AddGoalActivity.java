package com.fitnnestracker.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fitnnestracker.database.DatabaseHelper;
import com.fitnnestracker.models.Goal;
import com.fitnnestracker.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Calendar;
import java.util.Date;

public class AddGoalActivity extends AppCompatActivity {

    private AutoCompleteTextView goalTypeDropdown;
    private EditText goalNameEditText, targetValueEditText, currentValueEditText, dateEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private final Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupSpinner();
        setupDatePicker();
        setupSaveButton();
    }

    private void initViews() {
        goalTypeDropdown = findViewById(R.id.goal_type_dropdown);
        goalNameEditText = findViewById(R.id.goal_name_edit_text);
        targetValueEditText = findViewById(R.id.target_value_edit_text);
        currentValueEditText = findViewById(R.id.current_value_edit_text);
        dateEditText = findViewById(R.id.goal_date_edit_text);
        saveButton = findViewById(R.id.save_goal_button);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.goal_types, android.R.layout.simple_dropdown_item_1line);
        goalTypeDropdown.setAdapter(adapter);
    }

    private void setupDatePicker() {
        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        dateEditText.setText(getFormattedDate(selectedDate.getTime()));
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    dateEditText.setText(getFormattedDate(selectedDate.getTime()));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private String getFormattedDate(Date date) {
        return android.text.format.DateFormat.getDateFormat(this).format(date);
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveGoal());
    }

    private void saveGoal() {
        String name = goalNameEditText.getText().toString().trim();
        String type = goalTypeDropdown.getText().toString();
        String targetValue = targetValueEditText.getText().toString().trim();
        String currentValue = currentValueEditText.getText().toString().trim();

        if (name.isEmpty()) {
            goalNameEditText.setError("Please enter the target name");
            return;
        }

        if (targetValue.isEmpty()) {
            targetValueEditText.setError("Please enter the target value");
            return;
        }

        if (currentValue.isEmpty()) {
            currentValue = "0";
        }

        Goal goal = new Goal(name, type, targetValue, currentValue, selectedDate.getTime());
        long id = dbHelper.addGoal(goal);

        if (id != -1) {
            Toast.makeText(this, "The target was saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "An error occurred while saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}