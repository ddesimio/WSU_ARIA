package mwdindustries.aria;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static junit.runner.BaseTestRunner.savePreferences;


public class POIActivity extends Activity {

    private static final String TAG = "POIActivity CLass";// for debug

    //for access to a list of currently selected POI's
    ArrayList<String> outsidePOIList = new ArrayList<String>();

    //building list
    String[] pointsOfInterest = new String[]
            {
                    "Art Work",
                    "Allyn Hall",
                    "Brehm Laboratory",
                    "Biological Sciences",
                    "Creative Arts Center",
                    "Campus Services",
                    "Diggs Laboratory",
                    "Dunbar Library",
                    "Fine Arts Building",
                    "Fawcett Hall",
                    "Hamilton Hall",
                    "Health Sciences",
                    "Joshi Research Center",
                    "Library Annex",
                    "Math and Micro",
                    "Millet Hall",
                    "Medical Sciences",
                    "Oelman Hall",
                    "Russ Engineering Center",
                    "Rike Hall",
                    "Student Union",
                    "University Hall",
                    "White Hall"
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_poi);

//        SharedPreferences indoors = getSharedPreferences("Indoor", Context.MODE_PRIVATE);
//        Boolean exits = indoors.getBoolean("exits",false);

        //force screen orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        // The checkbox for the each item is specified by the layout android.R.layout.simple_list_item_multiple_choice
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, pointsOfInterest)
        {
            @Override //this whole damn thing just for black text in the listview
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        // Getting the reference to the listview object of the layout
        final ListView listViewPOI = (ListView) findViewById(R.id.buildingListview);

        // Setting adapter to the listview
        listViewPOI.setAdapter(adapter);

        //call update function every time checkboxlist is clicked
        listViewPOI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayToTextView(listViewPOI);
            }
        });


    } //end onCreate

    //public final ArrayList<String> currentSelections = new ArrayList<String>();

    //method called by every checkboxlist click to update current selections
    public void displayToTextView(ListView theList)
    {
        //get checked items
        final ArrayList<String> currentSelections = new ArrayList<String>();
        int len = theList.getCount();
        SparseBooleanArray checked = theList.getCheckedItemPositions();
        for (int i = 0; i < len; i++) {
            if (checked.get(i))
            {
                String item = (String) theList.getItemAtPosition(i);

                //add item to currentSelections list
                currentSelections.add(item);
                outsidePOIList.add(item);
            }

        }

        TextView tv = (TextView) findViewById(R.id.currentSelections_textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        String toDisplay = "";

        //if selections list not empty -> add them to string to display
        if(!currentSelections.isEmpty())
        {
            for (int i = 0; i < currentSelections.size(); i++){
                toDisplay+=(currentSelections.get(i) +"\n");
            }
        }

        tv.setText(toDisplay);

    }//end displayToTextView

    //return selected list --- to be called from main class
    public ArrayList getInsideSelections()
    {
        return outsidePOIList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

}// end class
