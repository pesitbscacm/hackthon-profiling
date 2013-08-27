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
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class HackerProfile extends Activity implements OnClickListener {

	DatabaseHelper db;
	CursorFactory factory;
	String hackerdetails;
	String[] hacker_details_array;
	String delim = "\\$@@\\$";
	TextView name, ticketid, team, description, classsec;
	String PIC_URL;
	Bitmap bitmap;
	ImageButton profile_pic;
	ImageButton exit_popup;
	File file;
	PopupWindow pw;
	LayoutInflater inflater;
	Display display;
	int screen_height;
	int screen_width;
	int flag=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hacker_profile);
		db = new DatabaseHelper(this, " ", factory, 1);
		profile_pic = (ImageButton) findViewById(R.id.hprofile_photo);
		profile_pic.setOnClickListener(this);
		
		inflater = LayoutInflater.from(this);
		display = getWindowManager().getDefaultDisplay();
		screen_height = display.getHeight();
		screen_width = display.getWidth();
		BackgroundStuff stuff = new BackgroundStuff();
		stuff.execute();

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.hprofile_photo:
			pw = new PopupWindow(inflater.inflate(R.layout.image_popup,
					null, false), (int) (0.9 * screen_width),
					WindowManager.LayoutParams.WRAP_CONTENT, true);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.showAtLocation(v, Gravity.CENTER, 0, 0);
			View pwv=pw.getContentView();
			exit_popup = (ImageButton) pwv.findViewById(R.id.popup_profile);
			exit_popup.setOnClickListener(this);
			//while(flag!=1)
			//{
			//	Log.d("Waiting to load image", "Waiting a lot");
			//}
			exit_popup.setImageBitmap(bitmap);
			break;
		case R.id.popup_profile:
			if(pw.isShowing())
				pw.dismiss();
			break;
		}
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

	private InputStream OpenHttpConnection(String strURL) throws IOException {
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

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(HackerProfile.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading...");
			dialog.show();
			ticketid = (TextView) findViewById(R.id.hticket_num);
			name = (TextView) findViewById(R.id.hperson_name);
			team = (TextView) findViewById(R.id.hteam_name);
			description = (TextView) findViewById(R.id.huser_description);
			classsec = (TextView) findViewById(R.id.huser_class_text);
			
			
			try {

				hackerdetails = db.getDetails(12).getValue();
				System.out.println(hackerdetails + "are the hacker details?");
				hacker_details_array = hackerdetails.split(delim, -1);
				if (hacker_details_array.length < 3) {
					Toast.makeText(HackerProfile.this, "Server Error. Try again later", 4000).show();
				}
				hacker_details_array[2] = hacker_details_array[2]
						.replaceAll("~", " ");
				hacker_details_array[3] = hacker_details_array[3]
						.replaceAll("~", " ");
				hacker_details_array[6] = hacker_details_array[6]
						.replaceAll("~", " ");
				hacker_details_array[11] = hacker_details_array[11]
						.replaceAll("~", " ");
				ticketid.setText(hacker_details_array[1]);
				name.setText(hacker_details_array[2]);
				classsec.setText(hacker_details_array[3]);
				description.setText(hacker_details_array[11]);
				team.setText(hacker_details_array[6]);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(HackerProfile.this, "Server Errors!", 4000).show();
				finish();
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (isOnline() == false) {
				return "No Internet";
			} else {
				Log.d("Hacker Details", hacker_details_array[7]);
				if (hacker_details_array[7].equalsIgnoreCase("")
						|| hacker_details_array[7].length() < 5) {
					hacker_details_array[7] = "http://cdn4.propakistani.pk/wp-content/uploads/2011/06/Fraud-Man.jpg";
				}
				PIC_URL = hacker_details_array[7];

				String extStorageDirectory;
				extStorageDirectory = Environment.getExternalStorageDirectory()
						.toString();
				String path = "/sdcard/";
				int flag = 0;
				if (PIC_URL
						.equalsIgnoreCase("http://cdn4.propakistani.pk/wp-content/uploads/2011/06/Fraud-Man.jpg")) {
					file = new File(path, "Default pic.png");
				} else {
					file = new File(path, hacker_details_array[2] + " pic.png");
					try{
						
						file.deleteOnExit();
						System.out.print("File deleted on exit");
					}
					catch(Exception e){
						e.printStackTrace();
						file.delete();
						System.out.print("File deleted in catch");
					}
					System.out.println(file.lastModified() + "");
					// if(file.lastModified()<100000)

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
						bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
						outStream.flush();
						outStream.close();
						Log.d("Pics", "Saved");

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (Exception e) {
						Log.d("Pics", "Not Saved");
						e.printStackTrace();
						return "Failed to download profile picture";

					}
				} else {
					System.out.println("File already saved earlier"); 

				}
				if (PIC_URL
						.equalsIgnoreCase("http://cdn4.propakistani.pk/wp-content/uploads/2011/06/Fraud-Man.jpg")) {
					bitmap = BitmapFactory.decodeFile(path + "Default pic.png");
				} else {
					bitmap = BitmapFactory.decodeFile(path
							+ hacker_details_array[2] + " pic.png");
				}
				return "ok";
			}

		}

		@Override
		protected void onPostExecute(String result) {
			try{
				if (dialog.isShowing())
			
				dialog.dismiss();
			if (result.equalsIgnoreCase("No Internet")) {
				Toast.makeText(HackerProfile.this,
						"Check internet connectivity", 6000).show();
			} else if (result.equalsIgnoreCase("Server Error"))
				Toast.makeText(HackerProfile.this,
						" There was a server error. Try again later", 6000)
						.show();
			else if (result.equalsIgnoreCase("ok")) {
				profile_pic.setImageBitmap(bitmap);
				
			}
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(HackerProfile.this, "Server Errors. Please try again later", 4000).show();
			}
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
}
