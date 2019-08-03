package com.example.linda.databasewithlistexample;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    private CountriesDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private EditText name;
    private EditText countryCode;
    private EditText uri;


    String strCountry = "";
    String strCode = "";
    String strCont = "";
    String strRegion = "";
    final static Integer flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button addCountry = (Button)findViewById(R.id.btnAdd);
        final Button deleteCountry = (Button)findViewById(R.id.btnDelete);
        final Button autoFill = (Button)findViewById(R.id.btnAuto);

        dbHelper = new CountriesDbAdapter(this);
        dbHelper.open();

    //  Clean all data
    //  dbHelper.deleteAllCountries();

    //  Add some data
    //  dbHelper.insertSomeCountries();

    //  Generate ListView from SQLite Database
        DisplayTheList();

        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_country.class));
            }
        });

        deleteCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, delete_country.class));
                }
        });

        autoFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAllCountries();
                dbHelper.insertSomeCountries();
                DisplayTheList();
            }
        });
    }



    private void DisplayTheList()
        {


        Cursor cursor = dbHelper.fetchAllCountries();

        // The desired columns to be bound
       String[] columns = new String[]{
                CountriesDbAdapter.KEY_CODE,
                CountriesDbAdapter.KEY_NAME,
                CountriesDbAdapter.KEY_URI,

       };

       // the XML defined views which the data will be bound to
       int[] to = new int[]{
                R.id.code,
                R.id.name,
                R.id.uri,
       };

       // create the adapter using the cursor pointing to the desired data
       //as well as the layout information
       dataAdapter = new SimpleCursorAdapter(
                this, R.layout.country_info,
                cursor,
                columns,
                to,
                0);

       ListView listView = (ListView) findViewById(R.id.listView1);
       // Assign adapter to ListView
       listView.setAdapter(dataAdapter);
       listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                /*
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                String code =
                        cursor.getString(cursor.getColumnIndexOrThrow("code"));
                Toast.makeText(getApplicationContext(), code, Toast.LENGTH_SHORT).show();
                */

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                String code = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String uri = cursor.getString(cursor.getColumnIndexOrThrow("uri"));

                Intent intent = new Intent(MainActivity.this, SingleView.class);
                intent.putExtra("code",code);
                intent.putExtra("name", name);
                intent.putExtra("uri", uri);

                startActivity(intent);
            }
        });


       EditText myFilter = (EditText) findViewById(R.id.myFilter);
       myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

           }

           public void beforeTextChanged(CharSequence s, int start,
                                         int count, int after) {
           }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

       dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
    }}






