package ucd.team4.Project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pauric on 30/11/2017.
 */

public class SQLite_Database extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="AppDatabase.db";
    public SQLite_Database (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        // SQL statement to create book table
        String CREATE_RUN_TABLE = "CREATE TABLE runHistory ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "time TEXT, " +
                "distance INTEGER, "+
                "calories INTEGER, "+
                "steps INTEGER)";
        String CREATE_USER_TABLE = "CREATE TABLE userProfile ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "gender TEXT," +
                "dob TEXT, " +
                "height TEXT, "+
                "weight TEXT, "+
                "bmi TEXT)";

        // create books table
        db.execSQL(CREATE_RUN_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + "runHistory");
        db.execSQL("DROP TABLE IF EXISTS " + "userProfile");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
