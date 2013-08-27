package com.pesit.hackathon;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinTeam extends Activity implements OnClickListener {
	DatabaseHelper db;
	CursorFactory factory;
	EditText teamname, teampass;
	final String JOINTEAM_STRING = "jointeam";
	String result = "Undefined Error";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jointeam);
		db = new DatabaseHelper(this, " ", factory, 1);
		Button update = (Button) findViewById(R.id.update);
		update.setOnClickListener(this);
		teamname = (EditText) findViewById(R.id.teamname);
		teampass = (EditText) findViewById(R.id.teampassword);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.update:
			BackgroundStuff stuff = new BackgroundStuff();
			stuff.execute();
			break;
		}
	}

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(JoinTeam.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Joining Team...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (isOnline() == false) {
				return "Not connected to the internet!";

			} else {
				final String jointeam_final_string = "email="
						+ db.getDetails(4).getValue() + "&teamname="
						+ teamname.getEditableText().toString() + "&pass="
						+ teampass.getEditableText().toString();

				ServerCode joinTeam = new ServerCode();

				try {
					result = joinTeam.serverInteract(JOINTEAM_STRING,
							jointeam_final_string);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing())
				dialog.dismiss();
			if (result
					.equalsIgnoreCase("You have joined the team successfully!")) {
				Toast.makeText(JoinTeam.this,
						"You have joined team successfully", 3000).show();
				
					Log.d("CDA", "onBackPressed Called");
					   Intent intent = new Intent(JoinTeam.this,MainActivity.class);
					   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					   startActivity(intent);
				

				try {
					db.updateDetails(new Details(6, "teamname", teamname
							.getEditableText().toString()));
				} catch (Exception e) {
					db.addDetails(new Details(6, "teamname", teamname
							.getEditableText().toString()));
				}

			}

			else
				Toast.makeText(JoinTeam.this, result, 3000).show();

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
}