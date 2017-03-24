package com.example.abhishekyadav.wificalling;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

public class first extends AppCompatActivity {
    ArrayAdapter adapter;
    WifiManager manager;
    WifiScanReceiver reciever;
    WifiInfo wifiInfo;
    ListView list;
    ArrayList<String> data;
TextView a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        list = (ListView) findViewById(R.id.listview);
//        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        manager.setWifiEnabled(true);
//        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        a=(TextView)findViewById(R.id.textView1);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(first.this,audio.class);
                startActivity(intent);

            }
        });
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        manager.setWifiEnabled(true);
        reciever = new WifiScanReceiver();
        wifiInfo = manager.getConnectionInfo();

        data = new ArrayList<String>();
        data.add("loading...");
        adapter= new ArrayAdapter<String>(getApplicationContext(),
               R.layout.list1,R.id.textView ,data);
               list.setAdapter(adapter);

               manager.startScan();

    }

    protected void onPause() {
        unregisterReceiver(reciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(reciever, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }





    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = manager.getScanResults();

            data.clear();
            for (int i = 0; i < wifiScanList.size(); i++) {
                String ssid = wifiScanList.get(i).SSID;
                int ip = wifiInfo.getNetworkId();
                String bssid = wifiScanList.get(i).BSSID;


                data.add(ssid+" "+bssid);
            }

            adapter.notifyDataSetChanged();
            manager.startScan();


        }
    }
}

