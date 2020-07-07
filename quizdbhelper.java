package com.example.myquiz;
import util;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myquiz.quizcontract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class quizdbhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myquiz.db";
    private static final int DATABASE_Version = 1;
    private SQLiteDatabase db;

    public quizdbhelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase; //but we can directly use sqLiteDatabase  Auto increament apne ko dena hota hai we need to create key for it
        final String SQL_CREATE_QUESTION_TABLE = "CREATE Table " +
                questionstable.TABLE_NAME + "(" +
                questionstable._ID + "INTEGAR PRIMARY KEY," +
                questionstable.COLUMN_QUESTION + " TEXT," +
                questionstable.COLUMN_OPTION1 + " TEXT," +
                questionstable.COLUMN_OPTION2 + " TEXT," +
                questionstable.COLUMN_OPTION3 + " TEXT," +
                questionstable.COLUMN_ANSWER_NR + " INTEGER, " +
                questionstable.COLUMN_DIFFICULTY + "TEXT" +
                ")";
        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        fillQustionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + questionstable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQustionsTable() {
        question q1 = new question("EASY: A is correct",
                "A", "B", "C", 1, question.DIFFICULTY_EASY);
        addQuestion(q1);
        question q2 = new question("MEDIUM: B is correct",
                "A", "B", "C", 2, question.DIFFICULTY_MEDIUM);
        addQuestion(q2);
        question q3 = new question("MEDIUM: c is correct",
                "A", "B", "C", 3, question.DIFFICULTY_MEDIUM);
        addQuestion(q3);
        question q4 = new question("HARD: A is correct",
                "A", "B", "C", 1, question.DIFFICULTY_HARD);
        addQuestion(q4);
        question q5 = new question("HARD: B is correct",
                "A", "B", "C", 2, question.DIFFICULTY_HARD);
        addQuestion(q5);
        question q6 = new question("HARD: c is correct",
                "A", "B", "C", 3, question.DIFFICULTY_HARD);
        addQuestion(q6);

    }

    private void addQuestion(question Question) {
        ContentValues cv = new ContentValues();
        cv.put(questionstable.COLUMN_QUESTION, Question.getQuestion());
        cv.put(questionstable.COLUMN_OPTION1, Question.getOption1());
        cv.put(questionstable.COLUMN_OPTION2, Question.getOption2());
        cv.put(questionstable.COLUMN_OPTION3, Question.getOption3());
        cv.put(questionstable.COLUMN_ANSWER_NR, Question.getAnswerNr());
        cv.put(questionstable.COLUMN_DIFFICULTY, Question.getDifficulty());
        db.insert(questionstable.TABLE_NAME, null, cv);
    }

    public ArrayList<question> getAllquestions() {
        ArrayList<question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + questionstable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                question Question = new question();
                Question.setQuestion(c.getString(c.getColumnIndex(questionstable.COLUMN_QUESTION)));
                Question.setOption1(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION1)));
                Question.setOption2(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION2)));
                Question.setOption3(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION3)));
                Question.setAnswerNr(c.getInt(c.getColumnIndex(questionstable.COLUMN_ANSWER_NR)));
                Question.setDifficulty(c.getString(c.getColumnIndex(questionstable.COLUMN_DIFFICULTY)));
                questionList.add(Question);


            } while (c.moveToNext());

        }

        c.close();
        return questionList;
    }

    public ArrayList<question> getquestions(String difficulty) {
        ArrayList<question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + questionstable.TABLE_NAME +
                " WHERE " + questionstable.COLUMN_DIFFICULTY+ " = ?", selectionArgs);
        if (c.moveToFirst()) {
            do {
                question Question = new question();
                Question.setQuestion(c.getString(c.getColumnIndex(questionstable.COLUMN_QUESTION)));
                Question.setOption1(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION1)));
                Question.setOption2(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION2)));
                Question.setOption3(c.getString(c.getColumnIndex(questionstable.COLUMN_OPTION3)));
                Question.setAnswerNr(c.getInt(c.getColumnIndex(questionstable.COLUMN_ANSWER_NR)));
                Question.setDifficulty(c.getString(c.getColumnIndex(questionstable.COLUMN_DIFFICULTY)));
                questionList.add(Question);


            } while (c.moveToNext());

        }

        c.close();
        return questionList;

    }
}


