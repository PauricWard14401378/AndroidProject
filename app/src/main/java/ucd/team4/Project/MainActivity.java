package ucd.team4.Project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static SQLite_Database db;
    public static SQLiteDatabase db2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CreateDatabaseTask().execute("");
        System.out.println("here");
        Button buttonOne = (Button) findViewById(R.id.button1);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityRun(v);
            }
        });
        Button buttonTwo = (Button) findViewById(R.id.button2);
        buttonTwo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityRun(v);
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
                launchActivityRun(v);
            }
        });
        db=new SQLite_Database(getApplicationContext());
    }

    public void launchActivityRun(View v) {
        Intent intent = new Intent(this, Running.class);
        startActivity(intent);
    }
    public void launchActivityRunHistory(View v) {
        Intent intent = new Intent(this, RunHistoryActivity.class);
        startActivity(intent);
    }
    public static class CreateDatabaseTask extends AsyncTask<String, Void, String> {
        protected  void onPreExecute(){
        }

        protected String doInBackground(String... s){
            System.out.println("here3");
            db2=MainActivity.db.getWritableDatabase();
            return "";
        }
        protected  void onProgressUpdate(){

        }
        protected void onPostExecute(){
            System.out.println("created database");
        }

    }

}
