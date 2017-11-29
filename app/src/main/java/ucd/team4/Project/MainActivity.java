package ucd.team4.Project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                launchActivityRun(v);
            }
        });
        Button buttonFour = (Button) findViewById(R.id.button4);
        buttonFour.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                launchActivityRun(v);
            }
        });
    }
    public void launchActivityRun(View v) {
        Intent intent = new Intent(this, Maps.class);
        startActivity(intent);
    }

}
