package com.fitnnestracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fitnnestracker.models.Goal;
import com.fitnnestracker.models.Workout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FitnessTracker.db";
    private static final int DATABASE_VERSION = 1;


    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_WORKOUT_ID = "id";
    private static final String COLUMN_WORKOUT_TYPE = "type";
    private static final String COLUMN_WORKOUT_DURATION = "duration";
    private static final String COLUMN_WORKOUT_CALORIES = "calories";
    private static final String COLUMN_WORKOUT_DATE = "date";


    private static final String TABLE_GOALS = "goals";
    private static final String COLUMN_GOAL_ID = "id";
    private static final String COLUMN_GOAL_NAME = "name";
    private static final String COLUMN_GOAL_TYPE = "type";
    private static final String COLUMN_GOAL_TARGET = "target_value";
    private static final String COLUMN_GOAL_CURRENT = "current_value";
    private static final String COLUMN_GOAL_DATE = "created_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + TABLE_WORKOUTS + "("
                + COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WORKOUT_TYPE + " TEXT,"
                + COLUMN_WORKOUT_DURATION + " TEXT,"
                + COLUMN_WORKOUT_CALORIES + " TEXT,"
                + COLUMN_WORKOUT_DATE + " INTEGER)";
        db.execSQL(CREATE_WORKOUTS_TABLE);

        db.execSQL("CREATE INDEX IF NOT EXISTS idx_workout_date ON " +
                TABLE_WORKOUTS + "(" + COLUMN_WORKOUT_DATE + ")");


        String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
                + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GOAL_NAME + " TEXT,"
                + COLUMN_GOAL_TYPE + " TEXT,"
                + COLUMN_GOAL_TARGET + " TEXT,"
                + COLUMN_GOAL_CURRENT + " TEXT,"
                + COLUMN_GOAL_DATE + " INTEGER)";
        db.execSQL(CREATE_GOALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    public long addWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_TYPE, workout.getType());
        values.put(COLUMN_WORKOUT_DURATION, workout.getDuration());
        values.put(COLUMN_WORKOUT_CALORIES, workout.getCalories());
        values.put(COLUMN_WORKOUT_DATE, workout.getDate().getTime());

        long id = db.insert(TABLE_WORKOUTS, null, values);
        db.close();
        return id;
    }

    public long addGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, goal.getName());
        values.put(COLUMN_GOAL_TYPE, goal.getType());
        values.put(COLUMN_GOAL_TARGET, goal.getTargetValue());
        values.put(COLUMN_GOAL_CURRENT, goal.getCurrentValue());
        values.put(COLUMN_GOAL_DATE, goal.getCreatedDate().getTime());

        long id = db.insert(TABLE_GOALS, null, values);
        db.close();
        return id;
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_WORKOUTS + " ORDER BY " + COLUMN_WORKOUT_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout();
                workout.setId(cursor.getInt(0));
                workout.setType(cursor.getString(1));
                workout.setDuration(cursor.getString(2));
                workout.setCalories(cursor.getString(3));
                workout.setDate(new Date(cursor.getLong(4)));

                workouts.add(workout);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return workouts;
    }

    public List<Goal> getAllGoals() {
        List<Goal> goals = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GOALS + " ORDER BY " + COLUMN_GOAL_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Goal goal = new Goal();
                goal.setId(cursor.getInt(0));
                goal.setName(cursor.getString(1));
                goal.setType(cursor.getString(2));
                goal.setTargetValue(cursor.getString(3));
                goal.setCurrentValue(cursor.getString(4));
                goal.setCreatedDate(new Date(cursor.getLong(5)));

                goals.add(goal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return goals;
    }

    public void deleteWorkout(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORKOUTS, COLUMN_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteGoal(int goalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, COLUMN_GOAL_ID + " = ?",
                new String[]{String.valueOf(goalId)});

        db.close();
    }

    public List<Workout> getLastWeekWorkouts() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Workout> workouts = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long lastWeekMillis = calendar.getTimeInMillis();

        String query = "SELECT * FROM " + TABLE_WORKOUTS +
                " WHERE " + COLUMN_WORKOUT_DATE + " >= ?" +
                " ORDER BY " + COLUMN_WORKOUT_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lastWeekMillis)});

        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout();
                workout.setId(cursor.getInt(0));
                workout.setType(cursor.getString(1));
                workout.setDuration(cursor.getString(2));
                workout.setCalories(cursor.getString(3));
                workout.setDate(new Date(cursor.getLong(4)));
                workouts.add(workout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return workouts;
    }

}
