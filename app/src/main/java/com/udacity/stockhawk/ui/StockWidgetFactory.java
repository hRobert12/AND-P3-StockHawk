package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.DbHelper;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

import static com.udacity.stockhawk.R.id.symbol;

public class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor cursor;
    private Context context;
    private DecimalFormat dollarFormatWithPlus;
    private DecimalFormat dollarFormat;
    private DecimalFormat percentageFormat;

    @Override
    public void onCreate() {
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        context = StockHawkApp.getContext();

        QuoteSyncJob.initialize(context);
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.query(Contract.Quote.TABLE_NAME, null, null, null, null, null, null);

        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }

    @Override
    public RemoteViews getViewAt(int i) {

        cursor.moveToPosition(i);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);

        views.setTextViewText(symbol, cursor.getString(Contract.Quote.POSITION_SYMBOL));
        views.setTextViewText(R.id.price, dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));


        float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

        if (rawAbsoluteChange > 0) {
            views.setTextViewCompoundDrawables(R.id.change, R.drawable.percent_change_pill_green, R.drawable.percent_change_pill_green, R.drawable.percent_change_pill_green, R.drawable.percent_change_pill_green);
        } else {
            views.setTextViewCompoundDrawables(R.id.change, R.drawable.percent_change_pill_red, R.drawable.percent_change_pill_red, R.drawable.percent_change_pill_red, R.drawable.percent_change_pill_red);
        }

        String change = dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage = percentageFormat.format(percentageChange / 100);

        if (PrefUtils.getDisplayMode(context)
                .equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            views.setTextViewText(R.id.change, change);
        } else {
            views.setTextViewText(R.id.change, percentage);
        }

        final Intent fillInIntent = new Intent();

        fillInIntent.putExtra("symbol", symbol);

        views.setOnClickFillInIntent(R.id.top_level_view, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        cursor.moveToPosition(i);
        return cursor.getLong(Contract.Quote.POSITION_ID);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
