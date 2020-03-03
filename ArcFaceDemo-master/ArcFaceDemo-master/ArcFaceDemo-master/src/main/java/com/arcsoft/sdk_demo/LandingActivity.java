package com.arcsoft.sdk_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LandingActivity extends Activity{
    private Button mloginbotton;
    private EditText mNumText,mPassText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_manager);
        mloginbotton=(Button)findViewById(R.id.button_login);
        mNumText=(EditText)findViewById(R.id.editText_num);
        mPassText=(EditText)findViewById(R.id.editText_password);
        mloginbotton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mNumText.getText().toString().equals("admin")&&mPassText.getText().toString().equals("123456"))
                        {
                            Intent i =new Intent(LandingActivity.this ,MainActivity.class);
                            startActivity(i);
                        }
                    }
                }
           );
        }
    }
