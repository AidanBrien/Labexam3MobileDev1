package com.example.Servlet_Connection;

import android.app.Activity;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class main extends Activity {
    int[] numbers = new int[5];

    Intent intentCallService5;
    BroadcastReceiver receiver;

    final int MY_PREFS_PRIV_MODE = Activity.MODE_PRIVATE;
    final String MY_RandNum_FILE = "my_RandNums";

    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;
    // obtain an editor to add data to my SharedPreferences object
    SharedPreferences.Editor myEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        intentCallService5 = new Intent(this, MyService5Async.class);

        IntentFilter filter5 = new IntentFilter("Intent");

        receiver = new MyEmbeddedBroadcastReceiver();
        registerReceiver(receiver, filter5);

        // create a reference to the SharedPreferences file
        mySharedPreferences = getSharedPreferences(MY_RandNum_FILE, MY_PREFS_PRIV_MODE);
        // obtain an editor to add data to (my)SharedPreferences object
        myEditor = mySharedPreferences.edit();
        // has a Preferences file been already created?
        if (mySharedPreferences != null ) {
            applySavedPreferences();
        } else {
            Toast.makeText(getApplicationContext(),
                    "No Preferences found", Toast.LENGTH_SHORT).show();
        }

    }

    private void applySavedPreferences() {

    }

//    public void onClick(View v) {
//        // clear all previous selections
//        myEditor.clear();
//
//
//        if (v.getId() == R.id.btnStartService) {
//            Log.e("MAIN", "onClick: starting service5");
//            Toast.makeText(getBaseContext(), "Start service running from button start!", Toast.LENGTH_LONG).show();
//            startService(intentCallService5);
//        } else if (v.getId() == R.id.btnStopService) {
//            Log.e("MAIN", "onClick: stopping service5");
//            stopService(intentCallService5);
//            Toast.makeText(getBaseContext(), "Stop service running from button stop", Toast.LENGTH_LONG).show();
//        }
//    }// onClick


    public void sendNums (View v){

        JSONObject json = new JSONObject();
        try {
            TextView view1 = (TextView)findViewById(R.id.textView);
          //  view1.setText("Fallowing are set before send and given numbers are: "+numbers[0]+", "+numbers[1]+", "+numbers[2]);

            json.put("one",numbers[0]);
            json.put("two",numbers[1]);
            json.put("three",numbers[2]);
            json.put("four",numbers[3]);
            json.put("five",numbers[4]);
           String baseUrl = "http://10.3.4.69:8080/MinMax";
            new HttpAsyncTask().execute(baseUrl, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void Rand (View v) throws InterruptedException {

        //clear all previous selections
        myEditor.clear();

       // Toast.makeText(getBaseContext(), "Start service running!", Toast.LENGTH_LONG).show();
        startService(intentCallService5);

      //  Toast.makeText(getBaseContext(), "Stop service running", Toast.LENGTH_LONG).show();
        stopService(intentCallService5);

        //myEditor.putInt("one");


    }
    
   

//    public void Rand(View v){
//        if(v.getId()== R.id.btnStartService){
//
//            startService(intentCallService5);
//            Toast.makeText(getBaseContext(), "Start service running!", Toast.LENGTH_LONG).show();
//        }else if (v.getId() == R.id.btnStopService){
//            stopService(intentCallService5);
//            Toast.makeText(getBaseContext(), "Stop service running", Toast.LENGTH_LONG).show();
//        }
//    }
//    protected void onDestroy() {
//        try {
//            stopService(intentCallService5);
//
//            unregisterReceiver(receiver);
//        } catch (Exception e) {
//
//            Log.e("MAIN4-DESTROY>>>", e.getMessage());
//        }
//        Log.e("MAIN4-DESTROY>>>", "TestMyService4 - Destroyed");
//        super.onDestroy(); // must be last entry in the method
//    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String jsonString = "";

            try {
                jsonString = HttpUtils.urlContentPost(urls[0], "num", urls[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(jsonString);
            return jsonString;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            JSONObject jsonResult = null;
            try {
                jsonResult = new JSONObject(result);

                String num1 = jsonResult.getString("max");
                String num2 = jsonResult.getString("min");
                String num3 = jsonResult.getString("sum");

                TextView view1 = (TextView)findViewById(R.id.textView);

                view1.setText("Min is: "+num1);
                view1.append("\nMax is: "+num2);
                view1.append("\nSum is: "+num3);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

    public class MyEmbeddedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView txt2 = (TextView)findViewById(R.id.txtView);

            if (intent.getAction().equals("Intent")) {

                numbers = intent.getIntArrayExtra("array");
                txt2.setText("The Five random numbers are: "+numbers[0]+", "+numbers[1]+", "+numbers[2]+", "+numbers[3]+", "+numbers[4]);

            }
        }
    }

}
