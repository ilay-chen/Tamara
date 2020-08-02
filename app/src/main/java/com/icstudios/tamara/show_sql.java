package com.icstudios.tamara;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static com.icstudios.tamara.orderListItem.refreshList;

public class show_sql extends AppCompatActivity {
    SQLiteDatabaseHandler db;
    MyOrderFragmentRecyclerViewAdapter listAdapter;
    private int mColumnCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sql);
        RecyclerView recyclerView = findViewById(R.id.list);

        db = new SQLiteDatabaseHandler(this);

        List<orderListItem.ScanItem> bleResults = db.allbleResultsByDate();

        if (bleResults != null) {

            refreshList(bleResults);

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

            }
        }
    }
}