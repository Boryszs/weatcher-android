package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity implements Runnable{

    private TextView textView;
    private TextView textTime;
    private TextView textTemp;
    private TextView textPressure;
    private TextView textHumidity;
    private TextView textTempMin;
    private TextView textTempMax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent=getIntent();
        String city=intent.getStringExtra("City");
        city+=",pl";
        textView=findViewById(R.id.name_city);
        textTime=findViewById(R.id.time);
        textTemp=findViewById(R.id.temp);
        textPressure=findViewById(R.id.pressure);
        textHumidity=findViewById(R.id.humidity);
        textTempMin=findViewById(R.id.tempMin);
        textTempMax=findViewById(R.id.tempMax);
        start();
        show(city);
    }

    public void showTime(){
        textTime.setText(getTime());
    }

    public void show(final String city){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI=retrofit.create(JsonPlaceholderAPI.class);
        Call call=jsonPlaceholderAPI.getCurrentWeatherData(city,"metric","749561a315b14523a8f5f1ef95e45864");

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(!response.isSuccessful()){
                    finish();
                    textView.setText("Code"+response.code());
                    return;
                }
                PostWeather postWeathers= (PostWeather) response.body();
                textView.setText(postWeathers.getName());
                textTemp.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp())+" <sup>o</sup>C"));
                textPressure.setText(Float.toString(postWeathers.getMain().getPressure())+" hPa");
                textHumidity.setText(Float.toString(postWeathers.getMain().getHumidity())+" %");
                textTempMin.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp_min())+" <sup>o</sup>C"));
                textTempMax.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp_max())+" <sup>o</sup>C"));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }
    public String getTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time=formatter.format(date);
        return  time;
    }


    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while (true) {
            showTime();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
