package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.util.ArrayList;

import butterknife.BindView;

public class StockDatails extends AppCompatActivity {
    @BindView(R.id.symbol_details)
    TextView symbolText;
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_datails);

        Intent recievedIntent = getIntent();
        symbolText.setText(recievedIntent.getStringExtra("stock"));
        Cursor c = recievedIntent.getParcelableExtra("histData");

        ArrayList<Entry> entries = new ArrayList<>();

        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                entries.add(new Entry(c.getPosition(), c.getFloat(c.getColumnIndex(Contract.Quote.COLUMN_PRICE))));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "price");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
