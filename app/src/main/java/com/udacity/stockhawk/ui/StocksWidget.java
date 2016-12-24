package com.udacity.stockhawk.ui;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.ContextHolder;
import com.udacity.stockhawk.sync.StockWidgetService;

import org.parceler.Parcels;

/**
 * Implementation of App Widget functionality.
 */
public class StocksWidget extends AppWidgetProvider {
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


        Intent intent = new Intent(context, StockWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra("Context", Parcels.wrap(new ContextHolder(context)));
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.stocks_widget);
        rv.setRemoteAdapter(appWidgetId, intent);

        Intent clickIntentTemplate = new Intent(context, StockDatails.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.recycler_view, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
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

