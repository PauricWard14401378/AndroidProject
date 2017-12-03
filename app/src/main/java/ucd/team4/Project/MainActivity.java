package ucd.team4.Project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static SQLite_Database db;
    public static SQLiteDatabase dbWritable;
    public static SQLiteDatabase dbReadable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        db = new SQLite_Database(getApplicationContext());

        if (!prefs.getBoolean("Time", false)) {


            // run your one time code
            System.out.println("yaya");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Time", true);
            editor.commit();
        }
        new CreateDatabaseTask().execute("");
        Button buttonOne = (Button) findViewById(R.id.button1);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityRun(v);
            }
        });
        Button buttonTwo = (Button) findViewById(R.id.button2);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityPedometer(v);
            }
        });
        Button buttonThree = (Button) findViewById(R.id.button3);
        buttonThree.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityRunHistory(v);
            }
        });
        Button buttonFour = (Button) findViewById(R.id.button4);
        buttonFour.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityUserProfile(v);
            }
        });

    }

    public void launchActivityRun(View v) {
        Intent intent = new Intent(this, Running.class);
        startActivity(intent);
    }
    public void launchActivityRunHistory(View v) {
        Intent intent = new Intent(this, RunHistoryActivity.class);
        startActivity(intent);
    }
    public void launchActivityPedometer(View v) {
        Intent intent = new Intent(this, onSensorChange.class);
        startActivity(intent);
    }
    public void launchActivityUserProfile(View v) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }
    public static class CreateDatabaseTask extends AsyncTask<String, Void, String> {
        protected  void onPreExecute(){
        }

        protected String doInBackground(String... s){
            System.out.println("here3");
            dbWritable=MainActivity.db.getWritableDatabase();
            dbReadable=MainActivity.db.getReadableDatabase();
           // db.onUpgrade(dbWritable, 0,1);
            return "";
        }
        protected  void onProgressUpdate(){

        }
        protected void onPostExecute(){
            System.out.println("created database");
        }

    }

}
