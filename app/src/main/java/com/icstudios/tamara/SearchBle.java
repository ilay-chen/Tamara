package com.icstudios.tamara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.icstudios.tamara.orderListItem.ITEMS;

public class SearchBle extends AppCompatActivity{
    SQLiteDatabaseHandler db;
    MyOrderFragmentRecyclerViewAdapter listAdapter;
    private int mColumnCount = 1;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sql);
        recyclerView = findViewById(R.id.list);

        db = new SQLiteDatabaseHandler(this);

        List<orderListItem.ScanItem> bleResults = db.allbleResultsByDate();

        if (bleResults != null) {

            orderListItem.refreshList(bleResults);

            // Set the adapter
            if (recyclerView instanceof RecyclerView) {
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
                }

                listAdapter = new MyOrderFragmentRecyclerViewAdapter(orderListItem.ITEMS);
                recyclerView.setAdapter(listAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                start();
            }
        }
    }

    public void refreshList()
    {
        List<orderListItem.ScanItem> bleResults = db.allbleResultsByDate();
        //orderListItem.refreshList(bleResults);
        orderListItem.ITEMS.clear();
        orderListItem.ITEMS.addAll(bleResults);
        listAdapter.notifyDataSetChanged();
        listAdapter = new MyOrderFragmentRecyclerViewAdapter(orderListItem.ITEMS);
        recyclerView.setAdapter(listAdapter);
    }

    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshList();
            if(started) {
                start();
            }
        }
    };

    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 1000);
    }
}