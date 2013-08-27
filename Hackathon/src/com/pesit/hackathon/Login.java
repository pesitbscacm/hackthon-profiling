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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	EditText email;
	EditText password;
	Button login_button;
	String[] split_result;
	final public String file = "login";
	static String result;
	String delim = "\\$@@\\$"; // why?? and why not \\$
	DatabaseHelper db;
	CursorFactory factory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login_button = (Button) findViewById(R.id.loginbutton);
		login_button.setOnClickListener(this);
		email = (EditText) findViewById(R.id.login_edittext);
		password = (EditText) findViewById(R.id.password_edittext);
		login_button.setOnClickListener(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		db = new DatabaseHelper(this, " ", factory, 1);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginbutton:
			BackgroundStuff stuff = new BackgroundStuff();
			stuff.execute();
			break;
		}
	}

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(Login.this);

		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading...");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			if (isOnline() == false) {
				return "No Internet";

			} else {
				final String login_final_string = "email="
						+ email.getEditableText() + "&pass="
						+ password.getEditableText();

				try{
					for(int i=0;i<12;i++)
						db.deleteDetails(i);
				}
				catch(Exception e){
					return "Logout first";
				}
				ServerCode LoginCode = new ServerCode();
				try {
					result = LoginCode.serverInteract(file, login_final_string);
					split_result = result.split(delim, -1);
					int i = 0;
					while (i < split_result.length) {

						System.out.println(split_result[i]);
						i++;
					}
				} catch (IOException e) {
					System.out.println("IOException");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("InterruptedException");
					e.printStackTrace();
				}

				if (split_result.length > 1) { // If the login is successful
					// Toast.makeText(Login.this, split_result.length+"",
					// 3000).show();
					split_result[11] = split_result[11].replaceAll("~", " ");
					split_result[3] = split_result[3].replaceAll("~", " ");
					split_result[2] = split_result[2].replaceAll("~", " ");
					split_result[6] = split_result[6].replaceAll("~", " ");
					
					db.addDetails(new Details(0, "id_user", split_result[0]));
					db.addDetails(new Details(1, "ticketid", split_result[1]));
					db.addDetails(new Details(2, "name", split_result[2]));
					db.addDetails(new Details(3, "classsec", split_result[3]));
					db.addDetails(new Details(4, "email", split_result[4]));
					db.addDetails(new Details(5, "pass", split_result[5]));
					db.addDetails(new Details(6, "teamname", split_result[6]));
					if (split_result[7].equalsIgnoreCase(null) || split_result[7].length()<5)
						 split_result[7]="http://cdn4.propakistani.pk/wp-content/uploads/2011/06/Fraud-Man.jpg";
					db.addDetails(new Details(7, "profilepic", split_result[7]));
					db.addDetails(new Details(8, "vegnonveg", split_result[8]));
					db.addDetails(new Details(9, "tshirtflag", split_result[9]));
					db.addDetails(new Details(10, "tshirtsize",
							split_result[10]));
					db.addDetails(new Details(11, "description",
							split_result[11]));
					for (int i = 0; i < 11; i++) {
						System.out.println(i+db.getDetails(i).getValue());
					}

				} else
					return "Invalid account";

			}
			return "All ok";
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing())
				dialog.dismiss();
			if (result.equalsIgnoreCase("No Internet")) {
				Toast.makeText(Login.this, "Check internet connectivity", 6000)
						.show();
			} else if (result.equalsIgnoreCase("invalid account"))
				Toast.makeText(Login.this, "Invalid Account", 6000).show();
			else if(result.equalsIgnoreCase("logout first")){
				Toast.makeText(Login.this, "Logout First", 3000).show();
			}
			else {
				Toast.makeText(Login.this, "Login Succesful", 3000).show();
				Intent i = new Intent(Login.this, Profile.class);
				startActivity(i);
			}
		}
	}
}