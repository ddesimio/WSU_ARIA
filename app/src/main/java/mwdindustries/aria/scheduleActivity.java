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


    private Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);

        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    } //end onCreate

    private void addListenerOnButton() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner6 = (Spinner) findViewById(R.id.spinner6);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // FROM HERE DOWN STILL NEED TO INCLUDE THE OTHER FIVE SPINNERS

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(scheduleActivity.this,
                        "OnClickListener : " + "\nSpinner 1 : "+ String.valueOf(spinner1.getSelectedItem())
                                             + "\nSpinner 2 : "+ String.valueOf(spinner2.getSelectedItem())
                                             + "\nSpinner 3 : "+ String.valueOf(spinner3.getSelectedItem())
                                             + "\nSpinner 4 : "+ String.valueOf(spinner4.getSelectedItem())
                                             + "\nSpinner 5 : "+ String.valueOf(spinner5.getSelectedItem())
                                             + "\nSpinner 6 : "+ String.valueOf(spinner6.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }

        });
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
