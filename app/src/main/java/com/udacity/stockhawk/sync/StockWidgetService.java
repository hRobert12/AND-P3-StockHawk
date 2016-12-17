package com.udacity.stockhawk.sync;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.ui.StockWidgetFactory;

/**
 * Created by user on 12/13/2016.
 */

public class StockWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetFactory();
    }


}
