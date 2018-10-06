package com.android415.pigim.pigim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText sendMsg = (EditText) findViewById(R.id.sendMsgText);
        final TextView receiveMsg = (TextView) findViewById(R.id.receiveMsgText);
        final ScrollView receiceScroll = (ScrollView) findViewById(R.id.receiveMsgScroll);

        sendMsg.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                String conversation;

                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    conversation = receiveMsg.getText().toString();
                    conversation += "\n" + sendMsg.getText().toString();
                    sendMsg.setText("");
                    receiveMsg.setText(conversation);
                    receiceScroll.fullScroll(View.FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });
    }

}
