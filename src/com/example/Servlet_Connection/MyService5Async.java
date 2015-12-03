package com.example.Servlet_Connection;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.util.Random;

public class MyService5Async extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new GetNumbers().execute();
	}

	public class GetNumbers extends AsyncTask <Integer, Integer, int[]> {
		
		@Override
		protected int[] doInBackground(Integer... params) {
			Random rand = new Random();

			int one = rand.nextInt(10);
			int two = rand.nextInt(10);
			int three = rand.nextInt(10);
			int four = rand.nextInt(10);
			int five = rand.nextInt(10);

			int[] array = new int[5];
			array[0] = one;
			array[1] = two;
			array[2] = three;
			array[3] = four;
			array[4] = five;

			return array;

		}

		@Override
		protected void onPostExecute(int[] ints) {
			super.onPostExecute(ints);

			Intent intentFilter5 = new Intent("Intent");
			intentFilter5.putExtra("array",ints);
			sendBroadcast(intentFilter5);
		}

	}
}

