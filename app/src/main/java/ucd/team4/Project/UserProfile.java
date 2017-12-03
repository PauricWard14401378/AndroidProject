package ucd.team4.Project;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by pauric on 02/12/2017.
 */

public class UserProfile extends AppCompatActivity {
    public TextView BMI;
    public static String gender="male";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText name= (EditText) findViewById(R.id.name);

        final RadioButton genderF= (RadioButton) findViewById(R.id.radioF);
        final RadioButton genderM= (RadioButton) findViewById(R.id.radioM);
        final EditText dob= (EditText) findViewById(R.id.dob);
        final EditText weight= (EditText) findViewById(R.id.weight);
        final EditText height= (EditText) findViewById(R.id.height);
        BMI=(TextView) findViewById(R.id.bmi_calculator);
        retrieveUserProfile(name, dob, height, weight);
        if(gender.equals("male")){
            System.out.println("hahaha"+gender);
            genderM.setChecked(true);
        }else{
            genderF.setChecked(true);
        }


        dob.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(!dob.getText().toString().equals("") && !weight.getText().toString().equals("") && !height.getText().toString().equals("")){
                    calculateBMI(dob, weight, height);
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });
        height.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(!height.getText().toString().equals("") && !weight.getText().toString().equals("") && !dob.getText().toString().equals("")){
                    calculateBMI(dob, weight, height);
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });
        weight.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(!weight.getText().toString().equals("") && !height.getText().toString().equals("") && !dob.getText().toString().equals("")){
                    calculateBMI(dob, weight, height);
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String temp=String.valueOf(myCalendar.get(Calendar.DAY_OF_MONTH))+"/"+
                        String.valueOf(myCalendar.get(Calendar.MONTH))+"/"+
                        String.valueOf(myCalendar.get(Calendar.YEAR));
                System.out.println("hellothere"+temp);
                dob.setText(temp);
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UserProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Button save = (Button) findViewById(R.id.send);
        save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(genderM.isChecked()){
                    gender="male";
                }else{
                    gender="female";
                }

                saveUserProfile(name, gender, dob, height, weight);
            }
        });
    }

    public void saveUserProfile(EditText name, String gender, EditText dob, EditText height, EditText weight){
        ContentValues values= new ContentValues();
        values.put("id", 1);
        values.put("name", name.getText().toString());
        values.put("gender", gender);
        values.put("dob", dob.getText().toString());
        values.put("height", height.getText().toString());
        values.put("weight", weight.getText().toString());
        values.put("bmi", BMI.getText().toString());
        MainActivity.dbWritable.replace("userProfile", null, values);
        System.out.println(BMI.getText().toString());
        Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show();
    }

    public void calculateBMI(EditText dob, EditText weight, EditText height){
        Toast.makeText(this, "Calculate BMI", Toast.LENGTH_SHORT).show();
        //kg/height2
        double bmi=Double.parseDouble(weight.getText().toString())/(Math.pow(Double.parseDouble(height.getText().toString())/100,2));
        bmi=(double) Math.round(bmi * 100d) / 100d;
        String bmi_range;
        if(bmi<18.5){
            bmi_range="(Underweight)";
        }if(bmi>25.0){
            bmi_range="(Overweight)";
        }else{
            bmi_range="(Normal)";
        }
        BMI.setText((String.valueOf(bmi)+" kg/m2 "+bmi_range));


    }

    public void retrieveUserProfile(EditText name, EditText dob, EditText height, EditText weight) {
        String[] projection = {
                "id",
                "name",
                "gender",
                "dob",
                "height",
                "weight",
                "bmi"
        };
        Cursor c = MainActivity.dbReadable.query(
                "userProfile",
                projection,
                "id = 1",
                null,
                null,
                null,
                null
        );
        if (c.moveToFirst()) {
            String index=c.getString(0);
            name.setText(c.getString(1));
            gender = c.getString(2);
            System.out.println("hahahaha"+gender);
            dob.setText(c.getString(3));
            height.setText(c.getString(4));
            weight.setText(c.getString(5));
            BMI.setText(c.getString(6));
        }
    }

}
