package com.example.linda.databasewithlistexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class delete_country extends AppCompatActivity {
    private CountriesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private EditText countryCode;
    String strCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_country);

        dbHelper = new CountriesDbAdapter(this);
        dbHelper.open();

        Button delete = (Button)findViewById(R.id.btnDelete);

        final EditText countryCode = (EditText)findViewById(R.id.txtCountry);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Before Delete", Toast.LENGTH_LONG);
                boolean deleted;
                strCode = countryCode.getText().toString();
                if (strCode.equals("")) {
                    Toast.makeText(getBaseContext(), "All fields must be entered", Toast.LENGTH_LONG).show();
                    countryCode.setFocusable(true);
                } else {
                    deleted = dbHelper.deleteOneCountry(strCode);
                    if (!deleted) {
                        Toast.makeText(getBaseContext(), "Country not found.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Country Deleted.", Toast.LENGTH_LONG).show();
                    }
                    startActivity(new Intent(delete_country.this, MainActivity.class));
                }
            }
        });
    }
}
