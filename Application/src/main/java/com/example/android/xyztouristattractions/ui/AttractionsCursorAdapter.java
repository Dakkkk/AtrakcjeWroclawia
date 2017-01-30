package com.example.android.xyztouristattractions.ui;

/**
 * Created by Dawid on 2017-01-20.
 */

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.xyztouristattractions.R;
import com.example.android.xyztouristattractions.provider.AttractionContract;
import com.squareup.picasso.Picasso;


public class AttractionsCursorAdapter extends CursorAdapter {


    public AttractionsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView attrName = (TextView) view.findViewById(R.id.attraction_name);
        TextView attrDescription = (TextView) view.findViewById(R.id.short_description);
        ImageView imgViewSource = (ImageView) view.findViewById(R.id.attraction_image);


        // Find the columns of attractions attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_SHORT_DESCRIPTION);
        int mainImgColumnIndex = cursor.getColumnIndex(AttractionContract.AttractionEntry.COLUMN_NAME_FOTO_MAIN);

        //Read attractions attributes of the current pet from the cursor
        String attractionName = cursor.getString(nameColumnIndex);
        String attractionDesc = cursor.getString(descriptionColumnIndex);
        String attractionMainImgUrl = cursor.getString(mainImgColumnIndex);

        Log.v("AttrCursorAdapter", attractionMainImgUrl );

        if (TextUtils.isEmpty(attractionDesc)) {
            attractionDesc = context.getString(R.string.attraction_doesnt_exist);
        }

        // Populate fields with extracted properties
        attrName.setText(attractionName);
        attrDescription.setText(String.valueOf(attractionDesc));

         Picasso.with(view.getContext()).load(attractionMainImgUrl).placeholder(R.drawable.empty_photo).into(imgViewSource);

    }
}
