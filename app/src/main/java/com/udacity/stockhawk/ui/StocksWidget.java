package com.udacity.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.DbHelper;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class StocksWidget extends AppWidgetProvider {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.error)
    TextView error;
    private StockAdapter adapter;
    DbHelper dbHelper = new DbHelper(this);

//    @Override
//    public void onClick(String symbol) {
//        Timber.d("Symbol clicked: %s", symbol);
//
//        String[] projection = {
//                Contract.Quote.COLUMN_PRICE,
//                Contract.Quote.COLUMN_SYMBOL
//        };
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        startActivity(new Intent(this, StockDatails.class).putExtra("stock", symbol).putExtra("histData", Parcels.wrap( new CursorHolder(
//                db.query(Contract.Quote.TABLE_NAME,
//                        projection,
//                        Contract.Quote.COLUMN_SYMBOL + " = " + symbol,
//                        null,
//                        null,
//                        null,
//                        null))
//        )));

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stocks_widget);

        views = new RemoteViews()

        ButterKnife.bind();

        adapter = new StockAdapter(context, new StockAdapter.StockAdapterOnClickHandler() {
            @Override
            public void onClick(String symbol) {
                //...
            }
        });

        recyclerView
        views.setA;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        QuoteSyncJob.initialize(this);
        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);

        // Construct the RemoteViews object

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

