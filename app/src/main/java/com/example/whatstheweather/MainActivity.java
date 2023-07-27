package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
EditText editText;
TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editTextTextPersonName);
        resultTextView=findViewById(R.id.resultTextView);

    }
    public void getWeather(View view){

        DownloadTask task= new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+ editText.getText().toString()+"&appid=fce043535ed2e30da480776c73a14410");
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result= new StringBuilder();
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data =reader.read();

                while (data!=-1) {
                    char current=(char)data;
                    result.append(current);
                    data= reader.read();
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo= jsonObject.getString("weather");
                Log.i("weather",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                StringBuilder msg= new StringBuilder();
                for (int i=0;i< arr.length();i++)
                {
                    JSONObject jsonObject1=arr.getJSONObject(i);
                    String main=jsonObject1.getString("main");
                    String description=jsonObject1.getString("description");
                 if(!main.equals("") && !description.equals(""))
                 {
                     msg.append(main).append(": ").append(description).append("\r\n");
                 }

                }
                if(!msg.toString().equals("")){
                    resultTextView.setText(msg.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("error","Error");
            }
        }
    }
}