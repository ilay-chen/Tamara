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
import android.util.Log;
import android.view.View;

import java.util.List;

import static com.icstudios.tamara.orderListItem.ITEMS;

public class search_ble extends AppCompatActivity implements MyOrderFragmentRecyclerViewAdapter.ItemClickListener{
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final int ACCESS_COARSE_LOCATION_REQUEST = 1235;
    ScanSettings scanSettings;
    private int mColumnCount = 1;
    MyOrderFragmentRecyclerViewAdapter listAdapter;
    SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_list);
        RecyclerView recyclerView = findViewById(R.id.list);

        db = new SQLiteDatabaseHandler(this);

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);

        hasPermissions();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();

        scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(1L)
                .build();


        if (scanner != null) {
            scanner.startScan(null, scanSettings, scanCallback);
            Log.d("tamara-test", "scan started");
        }  else {
            Log.e("tamara-test", "could not get scanner object");
        }

        // Set the adapter
        if (recyclerView instanceof RecyclerView) {
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
            }
            //orderListItem.refreshList();

            listAdapter = new MyOrderFragmentRecyclerViewAdapter(orderListItem.ITEMS);
            listAdapter.setClickListener(this);
            recyclerView.setAdapter(listAdapter);

            //recyclerView.setAdapter(new MyOrderFragmentRecyclerViewAdapter(orderListItem.ITEMS));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        }

    }

    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, ACCESS_COARSE_LOCATION_REQUEST);
                return false;
            }
        }
        return true;
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            int i = 0;
            for( ; i < ITEMS.size(); i++) {
                String d = ITEMS.get(i).getAddress();
                String s = result.getDevice().getAddress();
                if(d.equals(s)) {
                    break;
                }
            }
            if(i>=ITEMS.size())
                ITEMS.add(i,new orderListItem.ScanItem(result));
            else ITEMS.set(i,new orderListItem.ScanItem(result));

            listAdapter.notifyDataSetChanged();
//            orderListItem.refreshList(ITEMS);
            Log.d("tamara-test", "davice:" + device.getAddress()  + "rssi" + result.getRssi());
            // ...do whatever you want with this found device
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult ss : results) {
                Log.d("tamara-test", ss.getDevice().getName() + "rssi" + ss.getRssi());
                int i = 0;
                for (; i < ITEMS.size(); i++) {
                    String d = ITEMS.get(i).getAddress();
                    String s = ss.getDevice().getAddress();
                    if (d.equals(s)) {
                        break;
                    }
                }
                if (i >= ITEMS.size())
                    ITEMS.add(i, new orderListItem.ScanItem(ss));
                else ITEMS.set(i, new orderListItem.ScanItem(ss));

                db.addPlayer(new orderListItem.ScanItem(ss));
//                orderListItem.refreshList(results);
                // Ignore for now
            }
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanFailed(int errorCode) {
            // Ignore for now
        }
    };

    @Override
    public void onItemClick(View view, int position) {

    }
}