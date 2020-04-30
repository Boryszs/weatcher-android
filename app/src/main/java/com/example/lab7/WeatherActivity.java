package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity{

    private String zoneT=new String("");
    private TextView textView;
    private ImageView imageView;
    private TextView textTime;
    private TextView textTemp;
    private TextView textPressure;
    private TextView textHumidity;
    private TextView textTempMin;
    private TextView textTempMax;
    private TextView errMesge;
    private SwipeRefreshLayout swipeRefresh;
    private  String city;
    boolean timeBoolean;
    boolean dateBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Receive date
        Intent intent=getIntent();
        city=intent.getStringExtra("City");
        timeBoolean=true;
        dateBoolean=true;
        //Atribution items to variable
        textView=findViewById(R.id.name_city);
        textTime=findViewById(R.id.time);
        textTemp=findViewById(R.id.temp);
        textPressure=findViewById(R.id.pressure);
        textHumidity=findViewById(R.id.humidity);
        textTempMin=findViewById(R.id.tempMin);
        textTempMax=findViewById(R.id.tempMax);
        imageView=findViewById(R.id.imageView2);



        //Strat Method
       show();
       start();

        swipeRefresh = findViewById(R.id.spread_inside);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                show();
                swipeRefresh.setRefreshing(false);
            }
        });
    }



    //Method show time  witch timezZone include a JSON information
    public void showTime(){
        textTime.setText(getTime(TimeZone.getTimeZone(zoneT)));
    }

    //Method show information about city
    public void show(){


        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceholderAPI jsonPlaceholderAPI=retrofit.create(JsonPlaceholderAPI.class);
        //Set Query
        Call call=jsonPlaceholderAPI.getCurrentWeatherData(city,"metric","749561a315b14523a8f5f1ef95e45864");

        call.enqueue(new Callback() {
            //Receive date
            @Override
            public void onResponse(Call call, Response response) {
                //Catch error Query link
               if(response.code()==404) {
                   textView.setText("Zle dane");
                   finish();
                   return;
               }
               //Show erro code witch Query http
                else if(!response.isSuccessful()){
                    textView.setText("Code"+response.code());
                    finish();
                    return;
                }
                //Show if Query is success
                PostWeather postWeathers= (PostWeather) response.body();
                checkZoneTime(postWeathers.getTimezone());
                textView.setText(postWeathers.getName());
                textTemp.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp())+" <sup>o</sup>C"));
                textPressure.setText(Float.toString(postWeathers.getMain().getPressure())+" hPa");
                textHumidity.setText(Float.toString(postWeathers.getMain().getHumidity())+" %");
                textTempMin.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp_min())+" <sup>o</sup>C"));
                textTempMax.setText(Html.fromHtml(Float.toString(postWeathers.getMain().getTemp_max())+" <sup>o</sup>C"));
                //Show picture witch url link
                Picasso.get().load("http://openweathermap.org/img/wn/"+postWeathers.getWeather().get(0).getIcon()+".png").into(imageView);
            }
            //Show error
            @Override
            public void onFailure(Call call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });

    }
    //Method convert TimeZone witch date JSON
    private void checkZoneTime(float Time) {
        int zone=(int)Time/3600;
        zoneT+="GMT";
        if(zone>0){
                zoneT+="+";
                zoneT+=Integer.toString(zone);
            }
            else{
                zoneT+=Integer.toString(zone);
            }
    }

    //Method get time for TimzeZone
    public String getTime(TimeZone time){
        Date date = new Date();
        DateFormat requiredFormat = new SimpleDateFormat("HH:mm:ss ");
        requiredFormat.setTimeZone(time);
        String strCurrentDay = requiredFormat.format(date).toUpperCase();
        return strCurrentDay;
    }

    //Method create and start Thread
    public void start() {

        //Refresh time clock whhat one second
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeBoolean) {

                    try {
                        Thread.sleep(100);
                        showTime();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Refresh date city what five minute
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (dateBoolean) {

                    try {
                        Thread.sleep(300000);
                        show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        thread1.start();
    }

}
