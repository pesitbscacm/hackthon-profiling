package com.pesit.hackathon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register2 extends Activity implements OnClickListener {

    ImageButton profilePic;
    Button gallery;
    Button camera;
    EditText classsec;
    EditText desc;
    String pathToOurFile;
    LayoutInflater inflater;
    PopupWindow pw;
    String dept, sem, sec;
    String result;
    String link;
    String filePath;
    Display display;
    String HACKERDETAILS_STRING = "hackerdetails";
    int screen_height;
    String hackerdetails_final_string;
    int screen_width;
    int tshirtflag, vegnonvegflag;
    String tshirtsizeflag;
    CursorFactory factory;
    DatabaseHelper db;
    RadioGroup veg_nonveg;
    RadioButton veg_radio;
    RadioGroup tshirt;
    RadioGroup tshirtsize;
    RadioButton tshirt_small_radio;
    RadioButton tshirt_medium_radio;
    RadioButton tshirt_large_radio;
    RadioButton tshirt_yes_radio;
    RadioButton tshirt_no_radio;
    RadioButton nonveg_radio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
        veg_nonveg = (RadioGroup) findViewById(R.id.food_radiogroup);
        veg_radio= (RadioButton) findViewById(R.id.veg_radio);
        tshirt = (RadioGroup) findViewById(R.id.tshirt_radiogroup);
        tshirtsize = (RadioGroup) findViewById(R.id.tshirtsize_radiogroup);
        tshirt_small_radio = (RadioButton) findViewById(R.id.tshirt_small_radio);
        tshirt_medium_radio = (RadioButton) findViewById(R.id.tshirt_medium_radio);
        tshirt_large_radio = (RadioButton) findViewById(R.id.tshirt_large_radio);
        tshirt_yes_radio = (RadioButton) findViewById(R.id.tshirt_yes_radio);
        tshirt_no_radio = (RadioButton) findViewById(R.id.tshirt_no_radio);
        nonveg_radio= (RadioButton) findViewById(R.id.nonveg_radio);
        db = new DatabaseHelper(this, " ", factory, 1);
        TextView name = (TextView) findViewById(R.id.person_name);
        name.setText(db.getDetails(2).getValue());
        TextView team = (TextView) findViewById(R.id.team_name);
        if ((db.getDetails(6) == null)
                || db.getDetails(6).getValue().equalsIgnoreCase("")) {
            team.setText("Not set");
        } else
            team.setText(db.getDetails(6).getValue());
        TextView ticketid = (TextView) findViewById(R.id.ticket_num);
        ticketid.setText(db.getDetails(1).getValue());

        desc = (EditText) findViewById(R.id.descedittext);
        if ((db.getDetails(11) == null)||db.getDetails(11).getValue().equalsIgnoreCase("")) {
            desc.setText(" ");
        } else
            desc.setText(db.getDetails(11).getValue());
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        ArrayAdapter<CharSequence> deptspinner_adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item);
        deptspinner_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner deptspinner = (Spinner) findViewById(R.id.deptspinner);
        deptspinner_adapter.add("CSE");
        deptspinner_adapter.add("ECE");
        deptspinner_adapter.add("MECH");
        deptspinner_adapter.add("ISE");
        deptspinner_adapter.add("MCA");
        deptspinner.setAdapter(deptspinner_adapter);
        deptspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {

                if (position == 0)
                    dept = "CSE";
                if (position == 1)
                    dept = "ECE";
                if (position == 2)
                    dept = "MECH";
                if (position == 3)
                    dept = "ISE";
                if (position == 4)
                    dept = "MCA";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

            }

        });

        ArrayAdapter<CharSequence> semspinner_adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item);
        semspinner_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner semspinner = (Spinner) findViewById(R.id.semspinner);
        semspinner_adapter.add("1");
        semspinner_adapter.add("3");
        semspinner_adapter.add("5");
        semspinner_adapter.add("7");
        semspinner.setAdapter(semspinner_adapter);

        semspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {

                if (position == 0)
                    sem = "1";
                if (position == 1)
                    sem = "3";
                if (position == 2)
                    sem = "5";
                if (position == 3)
                    sem = "7";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        ArrayAdapter<CharSequence> secspinner_adapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item);
        secspinner_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner secspinner = (Spinner) findViewById(R.id.secspinner);
        secspinner_adapter.add("A");
        secspinner_adapter.add("B");

        secspinner.setAdapter(secspinner_adapter);
        secspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                    View selectedItemView, int position, long id) {

                if (position == 0)
                    sec = "A";
                if (position == 1)
                    sec = "B";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        inflater = LayoutInflater.from(this);
        display = getWindowManager().getDefaultDisplay();
        screen_height = display.getHeight();
        screen_width = display.getWidth();

        profilePic = (ImageButton) findViewById(R.id.profile_photo);
        profilePic.setOnClickListener(this);
        try{
        if (db.getDetails(3) != null) {

            String db_classsec = db.getDetails(3).getValue();
            String[] finalclasssec = db_classsec.split(" ");
            if (finalclasssec[0].equalsIgnoreCase("CSE")) {
                deptspinner.setSelection(0);
            }
            if (finalclasssec[0].equalsIgnoreCase("ECE")) {
                deptspinner.setSelection(1);
            }
            if (finalclasssec[0].equalsIgnoreCase("MECH")) {
                deptspinner.setSelection(2);
            }
            if (finalclasssec[0].equalsIgnoreCase("ISE")) {
                deptspinner.setSelection(3);
            }
            if (finalclasssec[0].equalsIgnoreCase("MCA")) {
                deptspinner.setSelection(4);
            }
            if (finalclasssec[1].equalsIgnoreCase("1")) {
                semspinner.setSelection(0);
            }
            if (finalclasssec[1].equalsIgnoreCase("3")) {
                semspinner.setSelection(1);
            }
            if (finalclasssec[1].equalsIgnoreCase("5")) {
                semspinner.setSelection(2);
            }
            if (finalclasssec[1].equalsIgnoreCase("7")) {
                semspinner.setSelection(3);
            }
            if (finalclasssec[2].equalsIgnoreCase("A")) {
                secspinner.setSelection(0);
            }
            if (finalclasssec[2].equalsIgnoreCase("B")) {
                secspinner.setSelection(1);
            }

        }
        }
        catch(Exception E){
            Toast.makeText(getBaseContext(), "Error getting your preferences", 3000).show();
        
        }
        try{
        if (db.getDetails(8) != null) {
            String foodflag = db.getDetails(8).getValue();
            if (foodflag.equalsIgnoreCase("1")) {
                nonveg_radio.setChecked(true);
                veg_radio.setChecked(false);
            } else {
                nonveg_radio.setChecked(false);
                veg_radio.setChecked(true);
            }
          
        }
        }
        catch(Exception e)
        {
        	Toast.makeText(getBaseContext(), "Error getting your preferences", 3000).show();
        }
        try{

        if (db.getDetails(9) != null) {
            String teeflag = db.getDetails(9).getValue();
            if (teeflag.equalsIgnoreCase("1")) {
                tshirt_yes_radio.setChecked(true);
                tshirt_no_radio.setChecked(false);
            } else {
                tshirt_yes_radio.setChecked(false);
                tshirt_no_radio.setChecked(true);
            }

        }
        }
        catch(Exception e)
        {
        	Toast.makeText(getBaseContext(), "Error getting your preferences", 3000).show();
        }
        try{
            
        if (db.getDetails(10) != null) {
            String teesizeflag = db.getDetails(10).getValue();
            if (teesizeflag.equalsIgnoreCase("s"))

            {
                tshirt_small_radio.setChecked(true);
                tshirt_medium_radio.setChecked(false);
                tshirt_large_radio.setChecked(false);
            } else if (teesizeflag.equalsIgnoreCase("m"))

            {
                tshirt_small_radio.setChecked(false);
                tshirt_medium_radio.setChecked(true);
                tshirt_large_radio.setChecked(false);
            }
            else if (teesizeflag.equalsIgnoreCase("l"))

            {
                tshirt_small_radio.setChecked(false);
                tshirt_medium_radio.setChecked(false);
                tshirt_large_radio.setChecked(true);
            }

        }
        }
        catch(Exception e)
        {
        	Toast.makeText(getBaseContext(), "Error getting your preferences", 3000).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.submit:

            switch (veg_nonveg.getCheckedRadioButtonId()) {
            case R.id.veg_radio:
                vegnonvegflag = 0;
                break;

            case R.id.nonveg_radio:
                vegnonvegflag = 1;
                break;
            }

            switch (tshirt.getCheckedRadioButtonId()) {
            case R.id.tshirt_yes_radio:
                tshirtflag = 1;
                break;

            case R.id.tshirt_no_radio:
                tshirtflag = 0;
                break;
            }

            switch (tshirtsize.getCheckedRadioButtonId()) {
            case R.id.tshirt_small_radio:
                tshirtsizeflag = "s";
                break;

            case R.id.tshirt_medium_radio:
                tshirtsizeflag = "m";
                break;
            case R.id.tshirt_large_radio:
                tshirtsizeflag = "l";
                break;

            }
            BackgroundStuff stuff = new BackgroundStuff();
            stuff.execute();

            break;
        case R.id.profile_photo:
            pw = new PopupWindow(inflater.inflate(R.layout.choose_profile_pic,
                    null, false), (int) (0.9 * screen_width),
                    WindowManager.LayoutParams.WRAP_CONTENT, true);

            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);
            final View pvu = pw.getContentView();
            gallery = (Button) pvu.findViewById(R.id.gallery_button);
            gallery.setOnClickListener(this);
            // camera = (Button) pvu.findViewById(R.id.camera_button);
            // camera.setOnClickListener(this);

            pvu.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        pw.dismiss();
                    }
                    return false;
                }
            });
            break;

        case R.id.gallery_button:
            Intent j = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(j, 100);
            break;

        /*
         * case R.id.camera_button: String path = "/sdcard/"; File file = new
         * File(path, db.getDetails(7).getValue()+" pics.png"); Uri
         * outputFileUri = Uri.fromFile(file);
         * 
         * Intent cameraIntent = new Intent(
         * android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
         * cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
         * startActivityForResult(cameraIntent, 1337); break;
         */
        }
    }

    private class BackgroundStuff extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(Register2.this);

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
                if (db.getDetails(3) == null)
                    db.addDetails(new Details(3, "classsec", dept + " " + sem
                            + " " + sec));
                else
                    db.updateDetails(new Details(3, "classsec", dept + " "
                            + sem + " " + sec));

                if (db.getDetails(9) == null)
                    db.addDetails(new Details(9, "tshirtflag", tshirtflag + ""));
                else
                    db.updateDetails(new Details(9, "tshirtflag", tshirtflag
                            + ""));
                if (db.getDetails(8) == null)
                    db.addDetails(new Details(8, "vegnonveg", vegnonvegflag
                            + ""));
                else
                    db.updateDetails(new Details(8, "vegnonveg", vegnonvegflag
                            + ""));

                if (db.getDetails(10) == null)
                    db.addDetails(new Details(10, "tshirtsize", tshirtsizeflag));
                else
                    db.updateDetails(new Details(10, "tshirtsize",
                            tshirtsizeflag));

                if (db.getDetails(11) == null)

                    db.addDetails(new Details(11, "description", desc
                            .getEditableText()

                            .toString()));
                else
                    db.updateDetails(new Details(11, "description", desc
                            .getEditableText().toString()));

                System.out.println(tshirtflag + "");
                // CHANGE THIS

                String s_email = db.getDetails(4).getValue();
                String teampassed;
                if (db.getDetails(6) == null)
                    teampassed = "";
                else
                    teampassed = db.getDetails(6).getValue();
                String profilepicpassed;
                if (db.getDetails(7) == null||db.getDetails(7).getValue().equalsIgnoreCase(""))
                    link = "http://cdn4.propakistani.pk/wp-content/uploads/2011/06/Fraud-Man.jpg";

                hackerdetails_final_string = "email=" + s_email + "&pass="
                        + db.getDetails(5).getValue() + "&classsec="
                        + db.getDetails(3).getValue() + "&teamname="
                        + teampassed + "&profilepic=" + link + "&vegnonveg="
                        + db.getDetails(8).getValue() + "&tshirt="
                        + db.getDetails(9).getValue() + "&tshirtsize="
                        + db.getDetails(10).getValue() + "&description="
                        + db.getDetails(11).getValue();

                ServerCode LoginCode = new ServerCode();

                try {
                    result = LoginCode.serverInteract(HACKERDETAILS_STRING,
                            hackerdetails_final_string);

                } catch (IOException e) {
                    System.out.println("IOException");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException");
                    e.printStackTrace();
                }

                if (!result.equalsIgnoreCase("SUCCESS")) {
                    return result;
                }

                else {

                    db.addDetails(new Details(7, "profilepic", link));
                    Intent m = new Intent(Register2.this, Profile.class);
                    startActivity(m);
                }

            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (dialog.isShowing())
                dialog.dismiss();
            if (!result
                    .equalsIgnoreCase("Registration Successful!!You may now login with your credentials..")) {
                Toast.makeText(getBaseContext(), result, 6000).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        Log.d("Yayee", filePath);
        cursor.close();

        if (requestCode == 1337) // Camera
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profilePic.setImageBitmap(thumbnail);
            filePath = "/sdcard/" + db.getDetails(2).getValue() + " pic.png";
            UploadImageAsync uploader = new UploadImageAsync();
            uploader.execute(" ");

        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {

                InputStream imageStream;

                try {
                    imageStream = getContentResolver().openInputStream(
                            selectedImage);

                    Bitmap thumbnail = BitmapFactory.decodeStream(imageStream);
                    profilePic.setImageBitmap(thumbnail);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                UploadImageAsync uploader = new UploadImageAsync();
                uploader.execute(" ");

            }
        }
    }

    private class UploadImageAsync extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(Register2.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Uploading your new profile picture..");
            dialog.show();
            // PIC_URL=db.getDetails(7).getValue();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                HttpURLConnection conn;
                HttpURLConnection connection = null;
                HttpURLConnection connection1 = null;
                DataOutputStream outputStream = null;
                DataInputStream inputStream = null;
                String urlServer = "http://pesseacm.org/ingenius/Enter/reg/fileupload.php";
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;

                FileInputStream fileInputStream = new FileInputStream(new File(
                        filePath));

                URL url = new URL(urlServer);
                connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs & Outputs
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // Enable POST method
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);

                outputStream = new DataOutputStream(
                        connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream
                        .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                                + pathToOurFile + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);

                int serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String inputLine = "";

                while ((inputLine = in.readLine()) != null) {

                    link = inputLine;

                }
                System.out.println(link);
                if (db.getDetails(7).getValue() == null)
                    db.addDetails(new Details(7, "profilepic", link));
                else
                    db.updateDetails(new Details(7, "profilepic", link));

                in.close();

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                return "Successful";

            } catch (Exception ex) {
                return "Uploading Failed. Error : " + ex;
                // Exception handling
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing())
                dialog.dismiss();
            if (pw.isShowing())
                pw.dismiss();
            if (result.compareToIgnoreCase("successful") == 0) {
                Toast.makeText(Register2.this,
                        "File Uploaded Successfully " + link, 3000).show();
                try{
                db.updateDetails(new Details(7, "profilepic", link));
                }catch(Exception e){
                	db.addDetails(new Details(7, "profilepic", link));
                }

            } else
                Toast.makeText(Register2.this, "File Upload Failed ", 5000)
                        .show();
        }

    }

}