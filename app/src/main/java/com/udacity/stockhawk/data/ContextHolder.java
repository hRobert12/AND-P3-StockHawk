package com.udacity.stockhawk.data;

import android.content.Context;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Created by Robert on 12/23/2016.
 */

@Parcel
public class ContextHolder {

    @ParcelProperty("context")
    private Context context;

    @ParcelConstructor
    public ContextHolder(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
