package com.icstudios.tamara;

import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderListItem {

    /**
     * An array of sample (OrderObject) items.
     */
    public static List<ScanItem> ITEMS = new ArrayList<ScanItem>();

    /**
     * A map of sample (OrderItem) items, by ID.
     */
    public static Map<String, ScanItem> ITEM_MAP = new HashMap<String, ScanItem>();

//    static {
//        for (int i = 0; i < allOrders.size(); i++)
//        {
//            addItem(createOrderItem(i, allOrders.get(i)));
//        }
//    }
    public orderListItem() { }

    public static void refreshList(List<ScanResult> newItems)
    {
        ITEMS = new ArrayList<ScanItem>();
        ITEM_MAP = new HashMap<String, ScanItem>();
        for (int i = 0; i < newItems.size(); i++)
        {
            addItem(createOrderItem(newItems.get(i)));
        }
    }

    private static void addItem(ScanItem scanResult) {
        ITEMS.add(scanResult);
        ITEM_MAP.put("1", scanResult);
    }

    private static ScanItem createOrderItem(ScanResult scanResult) {
        return new ScanItem(scanResult);
    }

    /**
     * A order item representing a piece of content.
     */
    public static class ScanItem {

        private String name;
        private String address;
        private String date;
        private String rssi;

        public ScanItem() {
        }

        public ScanItem(ScanResult scanResult) {
            this.name = scanResult.getDevice().getName();
            this.address = scanResult.getDevice().getAddress();
            this.date = "date";
            this.rssi = scanResult.getRssi()+"";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRssi() {
            return rssi;
        }

        public void setRssi(String rssi) {
            this.rssi = rssi;
        }

        @Override
        public String toString() {
            return address;
        }
    }
}