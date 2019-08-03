package com.example.linda.databasewithlistexample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class add_country extends AppCompatActivity {
    private CountriesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    String strName = "";
    String strCode = "";
    String strUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);
        dbHelper = new CountriesDbAdapter(this);
        dbHelper.open();

        Button saveButton = (Button)findViewById(R.id.btnAdd);
        final EditText code = (EditText) findViewById(R.id.txtCode);
        final EditText name = (EditText)findViewById(R.id.txtName);
        final EditText uri = (EditText)findViewById(R.id.txtURL);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCode = code.getText().toString();
                strName = name.getText().toString();
                strUri = uri.getText().toString();

                if (dbHelper.getNumOfCountry(strCode, strName) > 0){
                    Toast.makeText(getBaseContext(), "Country already exists", Toast.LENGTH_LONG).show();
                } else {
                    dbHelper.createCountry(strCode, strName, strUri);
                    Toast.makeText(getBaseContext(), "Record Added", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(add_country.this, MainActivity.class));
                }
            }
        });
    }
}
