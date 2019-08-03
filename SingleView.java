package com.example.linda.databasewithlistexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class SingleView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);

        TextView intCode = (TextView)findViewById(R.id.txtCode);
        TextView intName = (TextView)findViewById(R.id.txtName);
        Button intUri = (Button)findViewById(R.id.btnSite);

        Intent intent = getIntent();
        final String FLAGS_URL = "http://www.sciencekids.co.nz/images/pictures/flags680/" + getIntent().getStringExtra("name").replace(" ", "_") + ".jpg";
        intCode.setText(getIntent().getStringExtra("code"));
        intName.setText(getIntent().getStringExtra("name"));

        //Since you said this was extra credit, I wanted to try something
        //a little different for the flag displaying on the screen.
        //basically using a little URL magic to check the name of your
        //country and then using the name to get the images from the
        //website you provided. Basic Checklist for this method is to
        //use the picasso library which handles the downloading and
        //garbage collection for us so we don't have to code all that.
        //Then allow internet access in the manifest and then simply
        //add the URL like you see above and wah-lah!

        ImageView CountryFlag = (ImageView)findViewById(R.id.CountryFlag);
        Picasso.get()
                .load(FLAGS_URL)
                .error(R.drawable.error)
                .into(CountryFlag);
        intUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(getIntent().getStringExtra("uri"))) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getStringExtra("uri"))));
                } else {
                    Toast.makeText(getBaseContext(), "Invalid URL was entered.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
