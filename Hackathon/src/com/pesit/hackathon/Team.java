package com.pesit.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class Team extends Activity implements OnClickListener{
	Button create_team,join_team;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team);
        
        create_team = (Button) findViewById(R.id.createteam);
        create_team.setOnClickListener(this);
        join_team = (Button) findViewById(R.id.jointeam);
        join_team.setOnClickListener(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
        case R.id.createteam :
            Intent i = new Intent(Team.this,CreateTeam.class);
            startActivity(i);
            break;
        case R.id.jointeam :
            Intent j = new Intent(Team.this,JoinTeam.class);
            startActivity(j);
            break;
            
            
        }
    }

}