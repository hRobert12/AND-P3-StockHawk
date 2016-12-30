package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
                Contract.Quote.COLUMN_HISTORY,
                Contract.Quote.COLUMN_SYMBOL
        };

        Cursor c = db.query(Contract.Quote.TABLE_NAME,
                projection,
                Contract.Quote.COLUMN_SYMBOL + " = \"" + recievedIntent.getStringExtra("stock") + "\"",
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {

            String historyRaw = c.getString(c.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            String[] temp = historyRaw.split("\n");
            String[][] finalData = new String[temp.length][2];

            int i = 0;

            for (String tempString : temp) {
                String[] tempData = tempString.split(", ");
                finalData[i] = tempData;

                i++;
            }

            ArrayList<Entry> entries = new ArrayList<>();

            for (String[] stringData : finalData) {
                entries.add(new Entry(Float.parseFloat(stringData[0]) / 100000000, Float.parseFloat(stringData[1])));
            }


            if (entries.size() != 0) {

                LineDataSet dataSet = new LineDataSet(entries, "price");
                LineData lineData = new LineData(dataSet);
                chart.setNoDataText("No Data");
                chart.setData(lineData);
                //chart.invalidate();

            } else {
                chart.setVisibility(View.GONE);
            }
        }

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        c.close();
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
