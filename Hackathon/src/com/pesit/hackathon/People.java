package com.pesit.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.pesit.hackathon.utils.StringMatcher;
import com.pesit.hackathon.widget.IndexableListView;

public class People extends Activity implements OnItemClickListener {
	public ArrayList<String> names;
	public ArrayList<String> unsorted_names;
	final String ALL_USER_STRING = "alluser";
	final String USER_DETAILS_STRING = "userdetails";
	final String BLANK_STRING = "";
	public ArrayList<String> email;
	public IndexableListView mListView;
	public int position_of_click;
	String delimiter = "\\$@@\\$";
	String[] result_values;
	ServerCode HackerCode;
	DatabaseHelper db;
	CursorFactory factory;
	ContentAdapter adapter;
	String result;
	int flag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.people);
		db = new DatabaseHelper(this, " ", factory, 1);
		HackerCode = new ServerCode();
		names = new ArrayList<String>();
		unsorted_names = new ArrayList<String>();
		email = new ArrayList<String>();
		result = " ";
		mListView = (IndexableListView) findViewById(R.id.listview);
		GetStuffAsync stuffs = new GetStuffAsync();
		stuffs.execute();
		/*while (flag == 0) {
			Log.d("Waiting scenes", "Still Waiting");
		}*/

		mListView.setFastScrollEnabled(true);
		mListView.setOnItemClickListener(this);

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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position_of_click = position;
		BackgroundStuff stuffing = new BackgroundStuff();
		stuffing.execute();

	}

	private class ContentAdapter extends ArrayAdapter<String> implements
			SectionIndexer {

		private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		public ContentAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public int getPositionForSection(int section) {

			for (int i = section; i >= 0; i--) {
				for (int j = 0; j < getCount(); j++) {
					if (i == 0) {
						// For numeric section
						for (int k = 0; k <= 9; k++) {
							if (StringMatcher.match(
									String.valueOf(getItem(j).charAt(0)),
									String.valueOf(k)))
								return j;
						}
					} else {
						if (StringMatcher.match(
								String.valueOf(getItem(j).charAt(0)),
								String.valueOf(mSections.charAt(i))))
							return j;
					}
				}
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}
	}

	private class BackgroundStuff extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(People.this);

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
				final String selectedEmail = "&email="
						+ email.get(unsorted_names.indexOf(names
								.get(position_of_click)));
				String profile_details = " ";
				try {
					profile_details = HackerCode.serverInteract(
							USER_DETAILS_STRING, selectedEmail);
					if(profile_details.equalsIgnoreCase(""))
					{
						return "Errors";
					}
					try {
						if (db.getDetails(12) == null)

							db.addDetails(new Details(12, "hackerdetails",
									profile_details));
						else {
							db.deleteDetails(12);
							db.addDetails(new Details(12, "hackerdetails",
									profile_details));
						}
					} catch (Exception e) {
						return "Errors";
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing())
				dialog.dismiss();
			if (!result.equalsIgnoreCase("Errors")) {
				Log.d("Testings", "here is good");
				Intent i = new Intent(People.this, HackerProfile.class);
				startActivity(i);
			} else
				Toast.makeText(People.this, "No Internet", 4000).show();
		}

	}

	private class GetStuffAsync extends AsyncTask<String, Integer, String> {

		private ProgressDialog dialog = new ProgressDialog(People.this);

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
				try {
					result = HackerCode.serverInteract(ALL_USER_STRING, "");
				} catch (Exception e) {
					e.printStackTrace();
					return "Error";

				}

				result_values = result.split(delimiter);

				Log.d("Test", "Hi 1");
				Log.d("Test", " " + result_values.length);
				Log.d("Test", " " + result_values[1]);
				int i = 1;

				while (i < result_values.length) {
					if (i % 2 != 0) {
						result_values[i] = result_values[i].substring(0, 1)
								.toUpperCase() + result_values[i].substring(1);
						result_values[i] = result_values[i]
								.replaceAll("~", " ");
						names.add(result_values[i]);
						unsorted_names.add(result_values[i]);

					} else
						email.add(result_values[i]);
					i++;
				}
				Collections.sort(names);
				adapter = new ContentAdapter(People.this,
						android.R.layout.simple_list_item_1, names);
			}
			return "ok";

		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog.isShowing())
				dialog.dismiss();
			if (result.equalsIgnoreCase("Error"))
				Toast.makeText(People.this,
						"Error getting the list of users. Please try again",
						3000).show();
			else if (result.equalsIgnoreCase("No Internet"))
				Toast.makeText(People.this, "Check internet connectivity", 3000)
						.show();
			else
				mListView.setAdapter(adapter);
			flag = 1;

		}

	}
}
