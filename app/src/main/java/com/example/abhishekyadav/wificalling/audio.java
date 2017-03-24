package com.example.abhishekyadav.wificalling;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class audio extends AppCompatActivity {
public EditText enterip,entermsg;
public TextView myip,recmsg,mysent;
public ImageButton sentmsg,call;
public String myip1,entermsg1,recmsg1,enterip1,mysent1;
ServerSocket serverSocket=null;
Socket clientSocket=null;
Socket sentclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        enterip = (EditText) findViewById(R.id.enterip);
        entermsg = (EditText) findViewById(R.id.entermsg);
        mysent = (TextView) findViewById(R.id.mysent);
        myip = (TextView) findViewById(R.id.myip);
        recmsg = (TextView) findViewById(R.id.recmsg);
        sentmsg = (ImageButton) findViewById(R.id.sendmsg);
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        myip1 = Formatter.formatIpAddress(manager.getConnectionInfo().getIpAddress());
        myip.setText(myip1);
        call = (ImageButton) findViewById(R.id.call);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(4444);
                    while (true) {
                        clientSocket = serverSocket.accept();
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        recmsg1 = inputStream.readUTF();
                        setrecdata();


                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        }).start();

        sentmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entermsg1 = entermsg.getText().toString();
                enterip1 = enterip.getText().toString();

                if (!enterip1.equals("")) {
                    if (!entermsg1.equals("")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    sentclient = new Socket(enterip1, 4444);
                                    DataOutputStream outputStream = new DataOutputStream(sentclient.getOutputStream());
                                    outputStream.writeUTF(entermsg1);
                                    outputStream.flush();
                                    setsentmsg();
                                    outputStream.close();
                                    sentclient.close();
                                } catch (Exception e) {
                                    msgnotsent();
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "Type Your Msg", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Friend IP", Toast.LENGTH_SHORT).show();
                }
                entermsg.setText("");
            }
        });



    }










    public void setrecdata()
    {
        audio.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recmsg.append("Other :"+recmsg1+"\n");
                Toast.makeText(getApplicationContext(),"MSG REC FULLY",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void setsentmsg()
    {
        audio.this.runOnUiThread(new Runnable() {
        @Override
        public void run()
        {
        mysent.append("MY :"+entermsg1+"\n");
            Toast.makeText(getApplicationContext(),"MSG SENT FULLY",Toast.LENGTH_SHORT).show();
        }
        });

    }
public void msgnotsent()
{
    audio.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(),"MSG NOT SENT TRY AGAIN",Toast.LENGTH_SHORT).show();
        }
    });

}


}
