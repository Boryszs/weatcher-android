package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textVe;
    private EditText editText;
    private TextView errMessage;
    private boolean isRunning;
    private Button button;
    Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Atribution items to variable
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText1);
        errMessage=findViewById(R.id.errMesage);
        errMessage.setVisibility(View.INVISIBLE);
        isRunning=true;
        start();

        //Load save date
        SharedPreferences sharedPreferences=getSharedPreferences("shared",MODE_PRIVATE);
        String data=sharedPreferences.getString("data","");

        editText.setText(data);

        //Service click in button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city=editText.getText().toString();

                Intent intent=new Intent(MainActivity.this, WeatherActivity.class);
                //Transmission date
                intent.putExtra("City",city);
                //Save date
                saveData(city);
                //Start activity
                startActivity(intent);

            }
        });

    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRunning) {
                    try {
                        Thread.sleep(3000);

                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // Write your code here to update the UI.
                                try {
                                    if(errMessage.getVisibility()==View.VISIBLE && errMessage.getText()=="Connected witch Internet"){
                                    errMessage.setVisibility(View.INVISIBLE);
                                    }

                                    isOnline();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    //Method save date
    private void saveData(String city) {
        SharedPreferences sharedPreferences=getSharedPreferences("shared",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("data",city);
        editor.apply();
    }

    private void isOnline() throws InterruptedException {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();


        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            errMessage.setBackgroundColor(Color.parseColor("#00FF00"));
            button.setEnabled(true);
            errMessage.setText("Connected witch Internet");

        } else {
            errMessage.setBackgroundColor(Color.parseColor("#FF0000"));
                errMessage.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                errMessage.setText("No connection Internet!!!");
        }

    }
}
