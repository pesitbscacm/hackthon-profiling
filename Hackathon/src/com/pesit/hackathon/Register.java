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
import android.widget.ImageButton;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {
    EditText ticketid, name, email, confirmemail, password, confirmpassword;
    String sticketid, sname, semail, spassword;
    Button register_button;
    ImageButton scan;
    final public String REGISTER_STRING = "register";
    static String result;
    DatabaseHelper db;
    CursorFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        register_button = (Button) findViewById(R.id.registerbutton);
        ticketid = (EditText) findViewById(R.id.ticketid);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        confirmemail = (EditText) findViewById(R.id.confirmemail);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        register_button.setOnClickListener(this);
        scan = (ImageButton) findViewById(R.id.scanbutton);
        scan.setOnClickListener(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        System.out.println("REQUEST CODE=" + requestCode);
        // if (requestCode == 0) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, intent);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            String format = scanResult.getFormatName();
            ticketid.setText(contents);
            System.out.println("CONTENTS=" + contents);
            System.out.println("FORMATS=" + format);
            // handle scan result
        } else {
            Log.v("Harshit Scanner", "Daym Dude");
            System.out.println("hello 2");

        }
        System.out.println("hello 3");

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.registerbutton:
            BackgroundStuff stuff = new BackgroundStuff();
            stuff.execute();
         
            break;

        case R.id.scanbutton:
            IntentIntegrator integrator = new IntentIntegrator(Register.this);
            integrator.initiateScan();
            break;
        }
    }
 private class BackgroundStuff extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(Register.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading ...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (isOnline() == false) {
                return "Not connected to the internet!";

            } else {
       
                String register_final_string = "name=" + name.getEditableText()
                        + "&email=" + email.getEditableText() + "&cemail="
                        + confirmemail.getEditableText() + "&pass="
                        + password.getEditableText() + "&cpass="
                        + confirmpassword.getEditableText() + "&ticketid="
                        + ticketid.getEditableText();

                ServerCode LoginCode = new ServerCode();

                try {
                    result = LoginCode.serverInteract(REGISTER_STRING,
                            register_final_string);
                    System.out.println("hello");

                } catch (IOException e) {
                    System.out.println("IOException");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("InterruptedException");
                    e.printStackTrace();
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (dialog.isShowing())
                dialog.dismiss();
            if (!result
                    .equalsIgnoreCase("Registration Successful!!You may now login with your credentials..")) 
            {
                Toast.makeText(getBaseContext(), result, 6000).show();
            }
                else
                {
                    Toast.makeText(getBaseContext(), result, 3000).show();
                    try{
                    db.addDetails(new Details(1, "ticketid", ticketid
                            .getEditableText().toString()));
                    db.addDetails(new Details(2, "name", name.getEditableText()
                            .toString()));
                    db.addDetails(new Details(4, "email", email
                            .getEditableText().toString()));
                    db.addDetails(new Details(5, "pass", password
                            .getEditableText().toString()));
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getBaseContext(), "Could not add details to database. Try again later", 3000).show();
                    }
                    Intent i = new Intent(Register.this, Register2.class);
                    startActivity(i);

                }
                    
            }

        }
}