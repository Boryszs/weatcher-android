package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Atribution items to variable
        Button button=findViewById(R.id.button);
        editText=findViewById(R.id.editText1);

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
    //Method save date
    private void saveData(String city) {
        SharedPreferences sharedPreferences=getSharedPreferences("shared",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("data",city);
        editor.apply();
    }
}
