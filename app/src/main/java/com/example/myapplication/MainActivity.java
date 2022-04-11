package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String City = " ";
    String Key = "2d8c6eeadfa6053904573957e21bc937";
    String nameIcon = "10d";
    EditText editText;

    Button btnloading;

    ImageView Img;

    RelativeLayout relativeLayout1;
    RelativeLayout relativeLayout2;
    TextView textViewCity,textViewTime,Value,Value2,ValueHumidity,ValueVision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(view -> logout());

        editText = findViewById(R.id.edt_input);

        textViewCity = findViewById(R.id.txtCity);

        textViewTime = findViewById(R.id.txtTime);

        Value = findViewById(R.id.txtValue);

        Value2 = findViewById(R.id.txtTitleFeelLike);

        ValueHumidity = findViewById(R.id.txtValueHumidity);

        ValueVision = findViewById(R.id.txtValueVision);

      Img = findViewById(R.id.imgIcon);

        btnloading = findViewById(R.id.btnLoading);

        relativeLayout2 = findViewById(R.id.rlWeather);

        relativeLayout1= findViewById(R.id.rlMain_Ac);
    }


    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }




    private void logout() {

        FirebaseAuth.getInstance().signOut();
        startActivity( new Intent(MainActivity.this,Login.class));
    }

    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    public static class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            StringBuilder result = new StringBuilder();

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result.append((char) data);

                    data = inputStreamReader.read();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result.toString();
        }
    }


    @SuppressLint("SetTextI18n")
    public void loadWeather(View view) {

        editText.setVisibility(View.INVISIBLE);
        btnloading.setVisibility(View.INVISIBLE);
        relativeLayout2.setVisibility(View.VISIBLE);
        relativeLayout1.setBackgroundColor(Color.parseColor("#E6E6E6"));

        City = editText.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + City +"&units=metric&appid=" + Key;

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result;

            result = downloadTask.execute(url).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);

            JSONObject main = jsonObject.getJSONObject("main");

            String temp = main.getString("temp");

            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            Log.i("Name Icon",nameIcon);

            textViewCity.setText(City);
            Value.setText(temp + "°");
            ValueVision.setText(visibility);
            ValueHumidity.setText(humidity);
            Value2.setText(feel_like);

            DownloadImage downloadImage = new DownloadImage();

            String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            Bitmap bitmap = downloadImage.execute(urlIcon).get();

            Img.setImageBitmap(bitmap);

        } catch (ExecutionException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }

    }



}

