package ucd.team4.Project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by pauric on 30/11/2017.
 */
public class RunHistory extends Fragment {

    public static boolean noHistory=true;
    private ArrayAdapter<String> mForecastAdapter;

    public RunHistory() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        //Allow the fragement to access menu items.
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.run_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.clear_history) {
            MainActivity.db.onUpgrade(MainActivity.dbWritable, 0, 1);
            noHistory=true;
            Intent intent = new Intent(getActivity(), RunHistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.run_history_layout, container, false);

        String[] projection={
                "id",
                "date",
                "time",
                "distance",
                "calories",
                "steps",
                "duration"
        };
        Cursor c= MainActivity.dbReadable.query(
                "runHistory",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<String> runHistory=new ArrayList<String>();
        if(c.getCount()!=0){
            c.moveToFirst();
            int index=Integer.parseInt(c.getString(0))-1;
            String date=c.getString(1);
            String time=c.getString(2);
            String distance=c.getString(3);
            String calories=c.getString(4);
            String steps=c.getString(5);
            String duration= c.getString(6);
            runHistory.add("Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps+" Duration: "+duration);
            while (c.moveToNext()) {
                index=Integer.parseInt(c.getString(0))-1;
                date=c.getString(1);
                time=c.getString(2);
                distance=c.getString(3);
                calories=c.getString(4);
                steps=c.getString(5);
                duration= c.getString(6);
                runHistory.add("Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps+" Duration: "+duration);
            }
        }


        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_run_history, // The name of the layout ID.
                        R.id.list_item_run_history, // The ID of the textview to populate.
                        runHistory);


        ListView listView = (ListView) rootView.findViewById(R.id.listview_runHistory);
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                CharSequence forecast = mForecastAdapter.getItem(position);
                Intent newIntent = new Intent(getActivity(), RunDetail.class);
                newIntent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(newIntent);

            }

        });

        return rootView;


    }



}
