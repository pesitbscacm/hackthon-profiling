package com.pesit.hackathon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Activity implements OnClickListener {
	DatabaseHelper db;
	CursorFactory factory;
	TextView name_tv, description, class_sec_tv, food_tv, team_tv,
			ticketnum_tv, tshirt_tv;
	Button edit_profile, logout, editteam;
	ImageButton profile;
	ImageButton exit_popup;
	String PIC_URL;
	String path = "/sdcard/";
	Bitmap bitmap;
	PopupWindow pw;
	LayoutInflater inflater;
	Display display;
	int screen_height;
	int screen_width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		db = new DatabaseHelper(this, " ", factory, 1);
		name_tv = (TextView) findViewById(R.id.person_name);
		description = (TextView) findViewById(R.id.user_description);
		class_sec_tv = (TextView) findViewById(R.id.user_class_text);
		food_tv = (TextView) findViewById(R.id.user_food_text);
		team_tv = (TextView) findViewById(R.id.team_name);
		ticketnum_tv = (TextView) findViewById(R.id.ticket_num);
		tshirt_tv = (TextView) findViewById(R.id.user_tshirt_text);
		profile = (ImageButton) findViewById(R.id.profile_photo);
		profile.setOnClickListener(this);
		name_tv.setText(db.getDetails(2).getValue());
		ticketnum_tv.setText(db.getDetails(1).getValue());
		inflater = LayoutInflater.from(this);
		display = getWindowManager().getDefaultDisplay();
		screen_height = display.getHeight();
		screen_width = display.getWidth();
		if (db.getDetails(6) == null
				|| db.getDetails(6).getValue().length() == 0)
			team_tv.setText("Not set");
		else
			team_tv.setText(db.getDetails(6).getValue());

		if (db.getDetails(11) == null) {

		} else
			description.setText(db.getDetails(11).getValue());
		class_sec_tv.setText(db.getDetails(3).getValue());
		if (db.getDetails(8).getValue().equalsIgnoreCase("1")) {
			food_tv.setText("Non-veg");
		} else
			food_tv.setText("veg");
		System.out.println("tshirtflagis"
				+ db.getDetails(9).getValue().toString());
		if (db.getDetails(9).getValue().equalsIgnoreCase("0")) {
			tshirt_tv.setText("No");
		} else
			tshirt_tv.setText("Yes");

		edit_profile = (Button) findViewById(R.id.edit_profile_button);
		logout = (Button) findViewById(R.id.logout_button);
		edit_profile.setOnClickListener(this);
		logout.setOnClickListener(this);
		editteam = (Button) findViewById(R.id.editteam);
		editteam.setOnClickListener(this);
		BackgroundStuff stuff = new BackgroundStuff();
		stuff.execute();
	}

	@Override
	public void onBackPressed() {
		Log.d("CDA", "onBackPressed Called");
		Intent intent = new Intent(Profile.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_profile_button: {

			Intent i = new Intent(Profile.this, Register2.class);
			startActivity(i);
			break;
		}

		case R.id.logout_button:
			try{
						
			for (int i = 0; i < 12; i++) {
				db.deleteDetails(i);
			}
			}catch(Exception e)
			{
				Toast.makeText(Profile.this, "Error logging out. Try again", 3000).show();
			}

			Intent j = new Intent(Profile.this, MainActivity.class);
			startActivity(j);
			break;
		case R.id.editteam:
			Intent k = new Intent(Profile.this, Team.class);
			startActivity(k);
			break;
		case R.id.profile_photo:
			pw = new PopupWindow(inflater.inflate(R.layout.image_popup, null,
					false), (int) (0.9 * screen_width),
					WindowManager.LayoutParams.WRAP_CONTENT, true);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(v, Gravity.CENTER, 0, 0);
			View pwv = pw.getContentView();
			exit_popup = (ImageButton) pwv.findViewById(R.id.popup_profile);
			exit_popup.setOnClickListener(this);

			exit_popup.setImageBitmap(bitmap);
			break;
		case R.id.popup_profile:
			if (pw.isShowing())
				pw.dismiss();
			break;
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(Profile.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading...");
			dialog.show();
			PIC_URL = db.getDetails(7).getValue();
		}

		@Override
		protected String doInBackground(String... arg0) {
			int flag = 0;
			if (isOnline() == false) {
				return "No Internet";

			} else {
				try {
					File file = new File(path, db.getDetails(2).getValue()
							+ " pic.png");
					if (file.exists()) {
						boolean deleted = file.delete();
					}
					if (!file.exists()) {
						Bitmap bm;
						BitmapFactory.Options bmOptions;
						bmOptions = new BitmapFactory.Options();
						bmOptions.inSampleSize = 1;
						bm = LoadImage(PIC_URL, bmOptions);
						OutputStream outStream = null;
						try {
							outStream = new FileOutputStream(file);
							bm.compress(Bitmap.CompressFormat.PNG, 100,
									outStream);
							outStream.flush();
							outStream.close();
							Log.d("Pics", "Saved");
							flag = 1;

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {

							e.printStackTrace();
							Log.d("Pics", "Not Saved");
						}
					} else {
						System.out.println("File already saved earlier");
						flag = 1;
					}
					if (flag == 1) {
						bitmap = BitmapFactory.decodeFile(path
								+ db.getDetails(2).getValue() + " pic.png");
						return flag + "";
					}
				}

				catch (Exception e) {
					return "Failed";
				}
				return PIC_URL;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			profile.setImageBitmap(bitmap);
			if (dialog.isShowing())
				dialog.dismiss();
			if(result.equalsIgnoreCase("Failed"))
				Toast.makeText(Profile.this,
						"Failed to load details to phone. Try again", 6000)
						.show();
		}

		private Bitmap LoadImage(String URL, BitmapFactory.Options options) {
			Bitmap bitmap = null;
			InputStream in = null;
			try {
				in = OpenHttpConnection(URL);
				bitmap = BitmapFactory.decodeStream(in, null, options);
				in.close();
			} catch (IOException e1) {
			}
			return bitmap;
		}

		private InputStream OpenHttpConnection(String strURL)
				throws IOException {
			InputStream inputStream = null;
			URL url = new URL(strURL);
			URLConnection conn = url.openConnection();

			try {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setRequestMethod("GET");
				httpConn.connect();

				if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					inputStream = httpConn.getInputStream();
				}
			} catch (Exception ex) {
			}
			return inputStream;
		}
	}

}