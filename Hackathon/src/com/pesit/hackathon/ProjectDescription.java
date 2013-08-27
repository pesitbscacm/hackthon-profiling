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

public class ProjectDescription extends Activity implements OnClickListener{
    DatabaseHelper db;
    CursorFactory factory;
    final String UPDATEPROJECT_STRING = "addproject";
    Button update;
    String result;
    EditText projname,projdesc;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_description);
        update=(Button) findViewById(R.id.update);
        projname=(EditText)findViewById(R.id.projectname);
        projdesc=(EditText)findViewById(R.id.projectdesc);
        update.setOnClickListener(this);
        db = new DatabaseHelper(this, " ", factory, 1);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.update:
            
            BackgroundStuff stuff = new BackgroundStuff();
            stuff.execute();
            break;
        }
        
        
    }
    private class BackgroundStuff extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(ProjectDescription.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (isOnline() == false) {
                return "Not connected to the internet!";

            } else {
                
                final String updateproject_final_string ="&teamname="
                        +db.getDetails(6).getValue() + "&project_name="
                        +projname.getEditableText().toString()+"&project_desc="
                        + projdesc.getEditableText().toString();

                ServerCode Update_project = new ServerCode();

                try {
                    result = Update_project.serverInteract(UPDATEPROJECT_STRING,
                            updateproject_final_string);
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
                    .equalsIgnoreCase("Project Added Successfully!")) {
                Toast.makeText(getBaseContext(), result, 3000).show();
               // db.addDetails(new Details(14, "project_name", projname.getEditableText().toString()));
                
                    Log.d("CDA", "onBackPressed Called");
                       Intent intent = new Intent(ProjectDescription.this,MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                        }

            else
                Toast.makeText(ProjectDescription.this, result, 3000).show();

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