package com.icstudios.tamara;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RadarSearch extends AppCompatActivity {

    ArrayList<Point> points = new ArrayList();
    ScanSettings scanSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_radar_search);

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

        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);


        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        points.add(new Point(screenWidth/2,screenHeight/2));

        for(int i = 0; i < 10; i++)
        {
            Random r = new Random();
            points.add(new Point(r.nextInt(500),r.nextInt(500)));
        }

        DemoView demoview = new DemoView(this,points);
        setContentView(demoview);



    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(currentTime).replace(".","-");
            date = format.format(currentTime);
            for (ScanResult ss : results) {

                Log.d("tamara-test", ss.getDevice().getName() + "rssi" + ss.getRssi() + ", date:" + date);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
        }
    };

    private class DemoView extends View{
        ArrayList<Point> points = new ArrayList();
        public DemoView(Context context, ArrayList<Point> points){
            super(context);
            this.points.addAll(points);
        }


        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // custom drawing code here
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);

            // make the entire canvas white
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            for(Point point : points)
            {
                paint.setAntiAlias(false);
                paint.setColor(Color.BLUE);
                canvas.drawCircle(point.x, point.y, 15, paint);
            }
//
//            // draw blue circle with anti aliasing turned off
//            paint.setAntiAlias(false);
//            paint.setColor(Color.BLUE);
//            canvas.drawCircle(20, 20, 15, paint);
//
//            // draw green circle with anti aliasing turned on
//            paint.setAntiAlias(true);
//            paint.setColor(Color.GREEN);
//            canvas.drawCircle(60, 20, 15, paint);
//
//            // draw red rectangle with anti aliasing turned off
//            paint.setAntiAlias(false);
//            paint.setColor(Color.RED);
//            canvas.drawRect(100, 5, 200, 30, paint);
//
//            // draw the rotated text
//            canvas.rotate(-45);
//
//            paint.setStyle(Paint.Style.FILL);
//            canvas.drawText("Graphics Rotation", 40, 180, paint);

            //undo the rotate
//            canvas.restore();
        }
    }
    class Point {
        int x;
        int y;
        public Point()
        {

        }
        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}