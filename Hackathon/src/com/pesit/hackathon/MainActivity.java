package com.pesit.hackathon;

import java.io.IOException;
import java.sql.Date;
import java.util.GregorianCalendar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	ImageButton profile;
	ImageButton about;
	ImageButton people;
	ImageButton projects;
	ImageButton faq;
	ImageButton photos;
	DatabaseHelper db;
	CursorFactory factory;
	Long dtMili;
	int c = 0;
	long remain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DatabaseHelper(this, " ", factory, 1);
		profile = (ImageButton) findViewById(R.id.tile_profile);
		profile.setOnClickListener(this);
		people = (ImageButton) findViewById(R.id.tile_people);
		people.setOnClickListener(this);
		projects = (ImageButton) findViewById(R.id.tile_projects);
		photos = (ImageButton) findViewById(R.id.tile_photos);
		photos.setOnClickListener(this);
		about = (ImageButton) findViewById(R.id.tile_about);
		about.setOnClickListener(this);
		projects.setOnClickListener(this);
		faq = (ImageButton) findViewById(R.id.tile_faq);
		faq.setOnClickListener(this);

		BackgroundStuff stuff = new BackgroundStuff();
		stuff.execute();

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public String timeCalculate(long ttime) {
		long days, hours, minutes, seconds;
		String daysT = "", restT = "";

		days = (Math.round(ttime) / 86400);
		hours = (Math.round(ttime) / 3600) - (days * 24);
		minutes = (Math.round(ttime) / 60) - (days * 1440) - (hours * 60);
		seconds = Math.round(ttime) % 60;

		if (days == 1)
			daysT = String.format("%02d   day", days);
		else if (days > 1)
			daysT = String.format("%02d   days", days);
		else
			daysT = "Time left";
		restT = String.format("\n%02d : %02d : %02d", hours, minutes, seconds);

		return daysT + restT;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.menu_about:
			Intent i = new Intent(MainActivity.this, About.class);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tile_profile:
			if (db.getDetails(4) == null) {
				Intent i = new Intent(MainActivity.this, RegLogIn.class);
				startActivity(i);
				break;
			} else {
				Intent j = new Intent(MainActivity.this, Profile.class);
				startActivity(j);
				break;
			}
		case R.id.tile_projects:
			Intent j = new Intent(MainActivity.this, Projects.class);
			startActivity(j);
			break;
		case R.id.tile_faq:
			Intent k = new Intent(MainActivity.this, FAQ.class);
			startActivity(k);
			break;
		case R.id.tile_people:
			try {
				if (isOnline()) {
					Intent l = new Intent(MainActivity.this, People.class);
					startActivity(l);
				} else
					Toast.makeText(this, "Check internet connectivity", 6000)
							.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.tile_photos:
			Intent m = new Intent(MainActivity.this, Photos.class);
			startActivity(m);
			break;
		case R.id.tile_about:
			Intent n = new Intent(MainActivity.this, About.class);
			startActivity(n);
			break;
		}
	}

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {

			if (isOnline() == true) {
				ServerCode LoginCode = new ServerCode();
				final String TIME_STRING = "timemilli";
				String result;
				try {
					result = LoginCode.serverInteract(TIME_STRING, "");
					dtMili = Long.parseLong(result);
					dtMili *= 1000;

				} catch (IOException e) {
					System.out.println("IOException");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("InterruptedException");
					e.printStackTrace();
				}

			} else {

				dtMili = System.currentTimeMillis();
				GregorianCalendar date = new GregorianCalendar(2012, 9, 7, 8,
						0, 0);
				Date dateNow = new Date(dtMili);
				remain = date.getTimeInMillis() - dateNow.getTime();
				return "No Internet";
			}

			GregorianCalendar date = new GregorianCalendar(2012, 9, 7, 8, 0, 0);
			Date dateNow = new Date(dtMili);
			remain = date.getTimeInMillis() - dateNow.getTime();
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equalsIgnoreCase("No Internet"))
				Toast.makeText(
						getBaseContext(),
						"Internet not connected,countdown timer set according to system time",
						6000).show();
			new CountDownTimer(remain, 1000) {
				@Override
				public void onFinish() {

					TextView tv = (TextView) findViewById(R.id.countdown);
					tv.setText("Timer has Finished");
					c = 1;
					newtimer();
				}

				@Override
				public void onTick(long millisUntilFinished) {
					TextView tv = (TextView) findViewById(R.id.countdown);
					tv.setText(timeCalculate(millisUntilFinished / 1000));
				}
			}.start();

		}

	}

	void newtimer() {
		if (c == 1) {
			long dtMili = 1349533800000L;
		}
		Date dateNow = new Date(dtMili);
		GregorianCalendar hackfinish = new GregorianCalendar(2012, 9, 7, 20, 0,
				0);
		long remain = hackfinish.getTimeInMillis() - dateNow.getTime();
		new CountDownTimer(remain, 1000) {
			@Override
			public void onFinish() {
				// Action for when the timer has finished.
				TextView tv = (TextView) findViewById(R.id.countdown);
				tv.setText("Hackathon completed");
			}

			@Override
			public void onTick(long millisUntilFinished) {

				TextView tv = (TextView) findViewById(R.id.countdown);
				tv.setText(timeCalculate(millisUntilFinished / 1000));
			}
		}.start();
	}
}