package com.github.sdw8001.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.sdw8001.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ERROR_MSG = "Very very very long error message to get scrolling or multiline animation when the error button is clicked";
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

    private ArrayAdapter<String> adapter;

    private MaterialSpinner spinner_NoUnderline;
    private MaterialSpinner spinner1;
    private MaterialSpinner spinner2;
    private MaterialSpinner spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        initSpinnerNoUnderline();
        initSpinnerFloating();
        initSpinnerErrorSingleLine();
        initSpinnerErrorMultiLine();
    }

    private void initSpinnerNoUnderline() {
        spinner_NoUnderline = (MaterialSpinner) findViewById(R.id.Spinner_NoUnderline);
        spinner_NoUnderline.setAdapter(adapter);
    }

    private void initSpinnerFloating() {
        spinner1 = (MaterialSpinner) findViewById(R.id.Spinner_Floating);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), "onItemClick" + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getBaseContext(), "onNothingSelected" + adapterView.getItemAtPosition(adapterView.getSelectedItemPosition()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSpinnerErrorSingleLine() {
        spinner2 = (MaterialSpinner) findViewById(R.id.Spinner_ErrorSingleLine);
        spinner2.setAdapter(adapter);
    }

    private void initSpinnerErrorMultiLine() {
        spinner3 = (MaterialSpinner) findViewById(R.id.Spinner_ErrorMultiLine);
        spinner3.setAdapter(adapter);
    }

    private boolean shown = false;
    public void activateError(View view) {
        if (!shown) {
            spinner2.setError(ERROR_MSG);
            spinner3.setError(ERROR_MSG);
        } else {
            spinner2.setError(null);
            spinner3.setError(null);
        }
        shown = !shown;

    }
    public void selectionChange(View view) {
        spinner_NoUnderline.setSelection(2);
        spinner1.setSelection(2);
    }
}
