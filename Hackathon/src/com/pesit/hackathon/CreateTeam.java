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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTeam extends Activity implements OnClickListener {
	EditText name, password, desc, cpass;
	CursorFactory factory;
	Button update;
	String result;
	String NEWTEAM_STRING = "newteam";

	DatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createteam);
		name = (EditText) findViewById(R.id.nameedittext);
		password = (EditText) findViewById(R.id.passwordedittext);
		desc = (EditText) findViewById(R.id.descriptionedittext);
		update = (Button) findViewById(R.id.updatepoo);
		cpass = (EditText) findViewById(R.id.confirmpasswordedittext);
		update.setOnClickListener(this); 
		db = new DatabaseHelper(this, " ", factory, 1);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.updatepoo:
		BackgroundStuff stuff = new BackgroundStuff();
		stuff.execute();
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

		private ProgressDialog dialog = new ProgressDialog(CreateTeam.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Creating a new team...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (isOnline() == false) {
				return "Not connected to the internet!";

			} else {

				String s_email = db.getDetails(4).getValue();

				final String newteam_final_string = "email=" + s_email
						+ "&teamname=" + name.getEditableText().toString()
						+ "&pass=" + password.getEditableText().toString()
						+ "&cpass=" + cpass.getEditableText().toString()
						+ "&description=" + desc.getText().toString();

				ServerCode LoginCode = new ServerCode();

				try {
					result = LoginCode.serverInteract(NEWTEAM_STRING,
							newteam_final_string);

				} catch (IOException e) {
					System.out.println("IOException");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("InterruptedException");
					e.printStackTrace();
				}

				if (!result.equalsIgnoreCase("Team Added Successfully!")) {
					return result;
				}

				else {
					db.addDetails(new Details(6, "teamname", name
							.getEditableText().toString()));
					return "OK";
				}

			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (dialog.isShowing())
				dialog.dismiss();
			if (result.equalsIgnoreCase("OK")) {

				Toast.makeText(getBaseContext(), "Team Added Successfully!",
						10000).show();
				Log.d("CDA", "onBackPressed Called");
				   Intent intent = new Intent(CreateTeam.this,MainActivity.class);
				   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				   startActivity(intent);
			} else if (!result.equalsIgnoreCase("Team Added Successfully!")) {
				Toast.makeText(getBaseContext(), result, 10000).show();
			}
			
		}

	}

}