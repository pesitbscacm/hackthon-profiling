package com.pesit.hackathon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FAQ extends Activity implements OnClickListener {

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);
        TextView rules_tv = (TextView)findViewById(R.id.rules);
        
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }

    
}