package com.udacity.stockhawk.sync;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.ContextHolder;
import com.udacity.stockhawk.ui.StockWidgetFactory;

import org.parceler.Parcels;

/**
 * Created by user on 12/13/2016.
 */

public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        StockWidgetFactory stockWidgetFactory = new StockWidgetFactory();
        ContextHolder contextHolder = Parcels.unwrap(intent.getParcelableExtra("Context"));
        stockWidgetFactory.setContext(contextHolder.getContext());
        return stockWidgetFactory;
    }


}
