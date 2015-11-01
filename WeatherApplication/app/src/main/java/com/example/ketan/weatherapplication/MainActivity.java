package com.example.ketan.weatherapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utility.Request;


public class MainActivity extends ActionBarActivity {

    private EditText editTextLocation;
    private Button btnShow;
    private TextView txtdegree;
    private TextView txtname;
    private TextView txtcloudness;
    private TextView txtpressure;
    private TextView txthumidity;
    private TextView txtsunrise;
    private TextView txtsunset;
    private TextView txtgeo;
    private LinearLayout lnrdata;
    private ProgressDialog progressDialog;
    private static final String appid="7aad6602395bd9330de553a4d63ed260";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextLocation=(EditText)findViewById(R.id.location);
        txtname=(TextView)findViewById(R.id.name);
        txtcloudness=(TextView)findViewById(R.id.cloudness);
        txtpressure=(TextView)findViewById(R.id.Pressure);
        txthumidity=(TextView)findViewById(R.id.humidity);
        txtsunrise=(TextView)findViewById(R.id.sunrise);
        txtsunset=(TextView)findViewById(R.id.sunset);
        txtgeo=(TextView)findViewById(R.id.geo);
        txtdegree=(TextView)findViewById(R.id.degree);
        btnShow=(Button)findViewById(R.id.show);
        lnrdata=(LinearLayout)findViewById(R.id.data);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new request().execute(editTextLocation.getText().toString());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class request extends AsyncTask<String,Void,String>
    {
        String response=null;
        private List<NameValuePair> params;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Fetching data","Connecting....",true);
        }

        @Override
        protected String doInBackground(String... param) {
            try {
                params=new ArrayList<>();
                //add parameter for url request
                params.add(new BasicNameValuePair("q",param[0]));
                params.add(new BasicNameValuePair("appid",appid));

                //Call function for request url
                response=Request.httpGet("",params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                try {
                    lnrdata.setVisibility(View.VISIBLE);
                    //Catch data in JSON
                    JSONObject jsonObject = new JSONObject(s);
                    String name=jsonObject.getString("name");
                    JSONArray jsonweather=jsonObject.getJSONArray("weather");
                    JSONObject jsonweatherobj=jsonweather.getJSONObject(0);
                    String description=jsonweatherobj.getString("description");
                    JSONObject jsonmain=jsonObject.getJSONObject("main");
                    String temp=jsonmain.getString("temp");
                    String pressure=jsonmain.getString("pressure");
                    String humidity=jsonmain.getString("humidity");
                    JSONObject jsonsys=jsonObject.getJSONObject("sys");
                    String sunrise=jsonsys.getString("sunrise");
                    String sunset=jsonsys.getString("sunset");
                    long sunriselong=Long.parseLong(sunrise);
                    long sunsetlong=Long.parseLong(sunset);

                    JSONObject jsoncoord=jsonObject.getJSONObject("coord");
                    String lon=jsoncoord.getString("lon");
                    String lat=jsoncoord.getString("lat");


                    //Assign data to android widget
                    txtname.setText(name);
                    txtcloudness.setText(description);
                    txtpressure.setText(pressure);
                    txthumidity.setText(humidity);
                    float tempfloat=Float.parseFloat(temp)-273;
                    txtdegree.setText(Float.toString(tempfloat));
                    txtsunrise.setText(longTOHour(sunriselong)+":"+longTOMinute(sunriselong));
                    txtsunset.setText(longTOHour(sunsetlong)+":"+longTOMinute(sunsetlong));
                    txtgeo.setText("["+lon+","+lat+"]");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
        }
    }
    public long longTOMinute(long m)
    {
        return (m / (1000 * 60)) % 60;
    }
    public long longTOHour(long m)
    {
        return (m / (1000 * 60 * 60)) % 24;
    }
}
