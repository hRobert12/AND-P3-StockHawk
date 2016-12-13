package com.udacity.stockhawk.ui;

import android.database.Cursor;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelProperty;

/**
 * Created by Robert on 12/6/2016.
 */

@Parcel
public class CursorHolder {

    @ParcelProperty("cursor")
    Cursor heldCursor;

    @ParcelConstructor
    CursorHolder(Cursor cursor) {
        this.heldCursor = cursor;
    }

    public Cursor getHeldCursor() {
        return heldCursor;
    }
}
