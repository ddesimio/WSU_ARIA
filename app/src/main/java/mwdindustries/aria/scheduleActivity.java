package mwdindustries.aria;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by dominicdesimio on 2/16/15.
 */
public class scheduleActivity extends Activity implements AdapterView.OnItemSelectedListener {


    private Spinner spinner1;
    private Button btnSubmit;

    //building list
    String[] buildingChoices = new String[]
            {
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
        setContentView(R.layout.activity_schedule);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();
    } //end onCreate

    private void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(scheduleActivity.this,
                        "OnClickListener : " +
                                "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /////// NEW CLASS ///////
    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}