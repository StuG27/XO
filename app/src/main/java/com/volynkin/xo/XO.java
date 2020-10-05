package com.volynkin.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class XO extends AppCompatActivity {


    private Button mHotseatButton;
    private Button mAiButton;
    private Button mOnlineButton;
    private Button mStatsButton;
    private static final int REQUEST_CODE = 0;
    private char mAnswer = 'X';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xo_activity);

        mHotseatButton = (Button) findViewById(R.id.hotseat);
        mHotseatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode=0;
                Intent intent = Game.newIntent(XO.this, mode);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mAiButton = (Button) findViewById(R.id.ai);
        mAiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode=1;
                Intent intent = Game.newIntent(XO.this, mode);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mOnlineButton = (Button) findViewById(R.id.online);
        mOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mode=2;
                Intent intent = Game.newIntent(XO.this, mode);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        mStatsButton = (Button) findViewById(R.id.stats);
        mStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent info) {
        super.onActivityResult(requestCode, resultCode, info);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            if (info == null) {
                return;
            }
            mAnswer = Game.Answer(info);
        }
    }
}

