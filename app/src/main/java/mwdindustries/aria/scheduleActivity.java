package mwdindustries.aria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dominicdesimio on 2/16/15.
 */
public class scheduleActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6;
    private Button btnSubmit;
    EditText roomOne, roomTwo, roomThree, roomFour, roomFive, roomSix;


    //SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();

    //arrays to hole list of strings
    ArrayList<String> roomNumberList = new ArrayList<String>();
    ArrayList<String> buildingsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);

        //force screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

       final Intent intent = new Intent(this, ARView.class);

        //create room number edit text boxes
    roomOne = (EditText) findViewById(R.id.Room1);
    roomTwo = (EditText) findViewById(R.id.Room2);
    roomThree = (EditText) findViewById(R.id.Room3);
    roomFour = (EditText) findViewById(R.id.Room4);
    roomFive = (EditText) findViewById(R.id.Room5);
    roomSix = (EditText) findViewById(R.id.Room6);

//        //when submit button clicked
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //clear current list of rooms
//                roomNumberList.clear();
//
//                //clear current list of buildings
//                buildingsList.clear();
//
////                //get room numbers entered
//                String roomOneSelection = roomOne.getText().toString();
//                String roomTwoSelection = roomTwo.getText().toString();
//                String roomThreeSelection = roomThree.getText().toString();
//                String roomFourSelection = roomFour.getText().toString();
//                String roomFiveSelection = roomFive.getText().toString();
//                String roomSixSelection = roomSix.getText().toString();
//
//                //store room numbers in array
//                roomNumberList.add(roomOneSelection);
//                roomNumberList.add(roomTwoSelection);
//                roomNumberList.add(roomThreeSelection);
//                roomNumberList.add(roomFourSelection);
//                roomNumberList.add(roomFiveSelection);
//                roomNumberList.add(roomSixSelection);
//
//                //add spinner values to array
//                buildingsList.add(String.valueOf(spinner1.getSelectedItem()));
//                buildingsList.add(String.valueOf(spinner2.getSelectedItem()));
//                buildingsList.add(String.valueOf(spinner3.getSelectedItem()));
//                buildingsList.add(String.valueOf(spinner4.getSelectedItem()));
//                buildingsList.add(String.valueOf(spinner5.getSelectedItem()));
//                buildingsList.add(String.valueOf(spinner6.getSelectedItem()));
//
////                editor.putString("room1", roomOne.getText().toString());
////                editor.putString("room2", roomTwo.getText().toString());
////                editor.putString("room3", roomThree.getText().toString());
////                editor.putString("room4", roomFour.getText().toString());
////                editor.putString("room5", roomFive.getText().toString());
////                editor.putString("room6", roomSix.getText().toString());
//
//                //end activity
//                //moveTaskToBack(true);
//
//                //startActivity(intent);
//                //finish();
//            }
//});


    } //end onCreate

    @Override
    public void onBackPressed() {

                //clear current list of rooms
                roomNumberList.clear();

                //clear current list of buildings
                buildingsList.clear();

                //get room numbers entered
                String roomOneSelection = roomOne.getText().toString();
                String roomTwoSelection = roomTwo.getText().toString();
                String roomThreeSelection = roomThree.getText().toString();
                String roomFourSelection = roomFour.getText().toString();
                String roomFiveSelection = roomFive.getText().toString();
                String roomSixSelection = roomSix.getText().toString();

                //store room numbers in array
                roomNumberList.add(roomOneSelection);
                roomNumberList.add(roomTwoSelection);
                roomNumberList.add(roomThreeSelection);
                roomNumberList.add(roomFourSelection);
                roomNumberList.add(roomFiveSelection);
                roomNumberList.add(roomSixSelection);

                //add spinner values to array
                buildingsList.add(String.valueOf(spinner1.getSelectedItem()));
                buildingsList.add(String.valueOf(spinner2.getSelectedItem()));
                buildingsList.add(String.valueOf(spinner3.getSelectedItem()));
                buildingsList.add(String.valueOf(spinner4.getSelectedItem()));
                buildingsList.add(String.valueOf(spinner5.getSelectedItem()));
                buildingsList.add(String.valueOf(spinner6.getSelectedItem()));
        
//            this.moveTaskToBack(false);
    }

//    //save activity state
//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//        savedInstanceState.putString("room1", roomOne.getText().toString());
//        savedInstanceState.putString("room2", roomTwo.getText().toString());
//        savedInstanceState.putString("room3", roomThree.getText().toString());
//        savedInstanceState.putString("room4", roomFour.getText().toString());
//        savedInstanceState.putString("room5", roomFive.getText().toString());
//        savedInstanceState.putString("room6", roomSix.getText().toString());
//
//        savedInstanceState.putInt("building1", spinner1.getSelectedItemPosition());
//        savedInstanceState.putInt("building2", spinner2.getSelectedItemPosition());
//        savedInstanceState.putInt("building3", spinner3.getSelectedItemPosition());
//        savedInstanceState.putInt("building4", spinner4.getSelectedItemPosition());
//        savedInstanceState.putInt("building5", spinner5.getSelectedItemPosition());
//        savedInstanceState.putInt("building6", spinner6.getSelectedItemPosition());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        roomOne.setText(savedInstanceState.getString("room1"));
//        roomTwo.setText(savedInstanceState.getString("room2"));
//        roomThree.setText(savedInstanceState.getString("room3"));
//        roomFour.setText(savedInstanceState.getString("room4"));
//        roomFive.setText(savedInstanceState.getString("room5"));
//        roomSix.setText(savedInstanceState.getString("room6"));
//
//        spinner1.setSelection(savedInstanceState.getInt("building1"));
//        spinner2.setSelection(savedInstanceState.getInt("building2"));
//        spinner3.setSelection(savedInstanceState.getInt("building3"));
//        spinner4.setSelection(savedInstanceState.getInt("building4"));
//        spinner5.setSelection(savedInstanceState.getInt("building5"));
//        spinner6.setSelection(savedInstanceState.getInt("building6"));
//    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    //creates listeners for each spinner to activate when clicked
    private void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner6 = (Spinner) findViewById(R.id.spinner6);

        //btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    private void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner5.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spinner6 = (Spinner) findViewById(R.id.spinner6);
        spinner6.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    //return list of selected room numbers
    public ArrayList getRoomNumbers()
    {
        return roomNumberList;
    }

    //return list of selected buildings
    public ArrayList getBuildings()
    {
        return buildingsList;
    }



    /////// NEW CLASS ///////
    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }// end custom listener



}//end class
