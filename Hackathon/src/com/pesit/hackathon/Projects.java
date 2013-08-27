package com.pesit.hackathon;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Projects extends Activity implements OnClickListener{
    public ArrayList<String> projects;
    public ArrayList<String> project_descriptions;
    Button project_description;
    DatabaseHelper db;
    CursorFactory factory;
    //TextView proj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);
        project_description=(Button) findViewById(R.id.update_project);
        project_description.setOnClickListener(this);
       // proj=(TextView) findViewById(R.id.projectdesc);
        db = new DatabaseHelper(this, " ", factory, 1);
       /* if(!(db.getDetails(14).getValue().equalsIgnoreCase("")
        	||db.getDetails(14) == null
			|| db.getDetails(14).getValue().length() == 0))
			{
        	proj.setText("Your project is registered");
			}*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.update_project:
            if(db.getDetails(4)==null)
            {
                Intent i=new Intent(Projects.this,Login.class);
                startActivity(i);
            }
            else if(db.getDetails(6)==null)
            {
                Intent j= new Intent(Projects.this,Team.class);
                startActivity(j);
                
            }
            else
            {
            Intent i=new Intent(Projects.this,ProjectDescription.class);
            startActivity(i);
            }
            break;
        }
        
        
    }
}