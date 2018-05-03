package com.teknorial.rza.modulenotesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.teknorial.rza.modulenotesapp.MainActivity;
import com.teknorial.rza.modulenotesapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.teknorial.rza.modulenotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.teknorial.rza.modulenotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.teknorial.rza.modulenotesapp.db.DatabaseContract.NoteColumns.TITLE;
import static com.teknorial.rza.modulenotesapp.db.DatabaseContract.getColumnString;

public class ModulNotesAdapter extends CursorAdapter {

    public ModulNotesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_modul_note, viewGroup, false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if (cursor != null) {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_item_title);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_item_date);
            TextView tvDescription = (TextView) view.findViewById(R.id.tv_item_description);

            tvTitle.setText(getColumnString(cursor, TITLE));
            tvDescription.setText(getColumnString(cursor, DESCRIPTION));
            tvDate.setText(getColumnString(cursor, DATE));
        }

    }
}
