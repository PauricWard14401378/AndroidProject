package ucd.team4.Project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pauric on 30/11/2017.
 */

public class RunHistoryActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!RunHistory.noHistory) {
            setContentView(R.layout.activity_run_history);
        }else{
            setContentView(R.layout.activity_no_run_history);
        }
    }
}
