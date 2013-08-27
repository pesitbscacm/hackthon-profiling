package com.pesit.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegLogIn extends Activity implements OnClickListener {

	Button register;
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_log_in);
		register = (Button) findViewById(R.id.register_button);
		login = (Button) findViewById(R.id.login_button);
		register.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.register_button:
			Intent i=new Intent(RegLogIn.this,Register.class);
			startActivity(i);
			break;
		case R.id.login_button:
			Intent j=new Intent(RegLogIn.this,Login.class);
			startActivity(j);
			break;
		
		}
		
	}
}
