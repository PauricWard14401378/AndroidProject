package ucd.team4.Project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pauric on 30/11/2017.
 */
public class RunHistory extends Fragment {

    boolean yea=false;
    private ArrayAdapter<String> mForecastAdapter;

    public RunHistory() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        //Allow the fragement to access menu items.
        setHasOptionsMenu(true);

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // Inflate the menu items for use in the action bar
//        inflater.inflate(R.menu.forecastfragment, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_refresh) {
//            FetchWeatherTask weatherTask = new FetchWeatherTask();
//            weatherTask.execute("7778677");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.run_history_layout, container, false);



        System.out.println("here2"+yea);
        ContentValues values= new ContentValues();
        values.put("id", 1);
        values.put("date", "08/04/2017");
        values.put("time", "19:05");
        values.put("distance", 3);
        values.put("calories", 400);
        values.put("steps", 1000);
        // Create some dummy data for the ListView.  Here's a sample weekly forecast
//        System.out.println("done"+MainActivity.dbWritable);
//        long done=MainActivity.dbWritable.insert("runHistory", null, values);

        String[] projection={
                "id",
                "date",
                "time",
                "distance",
                "calories",
                "steps"
        };
        Cursor c= MainActivity.dbReadable.query(
                "runHistory",
                projection,
                "calories = 400",
                null,
                null,
                null,
                null
        );
        String[] runHistory=new String[7];
        if(c!=null){

            c.moveToFirst();
            int index=Integer.parseInt(c.getString(0))-1;
            String date=c.getString(1);
            String time=c.getString(2);
            String distance=c.getString(3);
            String calories=c.getString(4);
            String steps=c.getString(5);


            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            runHistory[index++]= "Date: "+date+" Time: "+time+" Distance: "+distance+" Calories: "+calories+" Steps: "+steps;
            System.out.println("helpingman"+runHistory.length);

//            while (c.moveToNext()) {
//                index=Integer.parseInt(c.getString(0))-1;
//                runHistory[index]= "Date: "+c.getString(1);
//                runHistory[index]+="\nTime: "+c.getString(2);
//                runHistory[index]+="\nDistance: "+c.getString(3);
//                runHistory[index]+="\nCalories: "+c.getString(4);
//                runHistory[index]+="\nSteps: "+c.getString(5);
//            }
        }


        String[] forecastArray = {
                "Mon 6/23 - Sunny - bcvbxcvb31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };

        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(runHistory));
        System.out.println("tttttt"+runHistory[0]);


        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_run_history, // The name of the layout ID.
                        R.id.list_item_run_history, // The ID of the textview to populate.
                        weekForecast);


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
