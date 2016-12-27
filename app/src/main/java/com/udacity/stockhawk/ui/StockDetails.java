package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.udacity.stockhawk.data.DbHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.R.id.symbol;

public class StockDetails extends AppCompatActivity {
    @BindView(R.id.symbol_details)
    TextView symbolText;
    @BindView(R.id.chart)
    LineChart chart;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_datails);

        ButterKnife.bind(this);

        Intent recievedIntent = getIntent();
        symbolText.setText(recievedIntent.getStringExtra("stock"));

        dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                Contract.Quote.COLUMN_PRICE,
                Contract.Quote.COLUMN_SYMBOL
        };

        Cursor c = db.query(Contract.Quote.TABLE_NAME,
                projection,
                Contract.Quote.COLUMN_SYMBOL + " = " + symbol,
                null,
                null,
                null,
                null);

        ArrayList<Entry> entries = new ArrayList<>();

        if (c.moveToFirst()) {
            while (c.moveToNext()) {
                entries.add(new Entry(c.getPosition() * 2, c.getFloat(c.getColumnIndex(Contract.Quote.COLUMN_PRICE))));
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
