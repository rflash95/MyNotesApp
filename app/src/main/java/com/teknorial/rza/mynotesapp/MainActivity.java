package com.teknorial.rza.mynotesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.teknorial.rza.mynotesapp.adapter.NoteAdapter;
import com.teknorial.rza.mynotesapp.db.NoteHelper;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.teknorial.rza.mynotesapp.FormAddUpdateActivity.REQUEST_UPDATE;
import static com.teknorial.rza.mynotesapp.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_notes)
    RecyclerView rvNotes;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private Cursor list;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Notes");

        ButterKnife.bind(this);

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        fabAdd.setOnClickListener(this);

        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);

        new LoadNoteAsync().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab_add) {
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }


    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);


            list = notes;
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0) {
                showSnackBarMessage("Tidak ada data saat int");
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD) {
            if (resultCode == FormAddUpdateActivity.RESULT_ADD) {
                new LoadNoteAsync().execute();
                showSnackBarMessage("Satu item berhasil ditambahkan");
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE) {
                new LoadNoteAsync().execute();
                showSnackBarMessage("Satu item berhasil diubah");
            } else if (resultCode == FormAddUpdateActivity.RESULT_DELETE) {
                new LoadNoteAsync().execute();
                showSnackBarMessage("Satu item berhasil dihapus");
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }
}
