package com.teknorial.rza.mynotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teknorial.rza.mynotesapp.db.NoteHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.teknorial.rza.mynotesapp.db.DatabaseContract.CONTENT_URI;
import static com.teknorial.rza.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.teknorial.rza.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.teknorial.rza.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_description)
    EditText edtDescription;

    @BindView(R.id.btn_submit)
    Button btnSubmit;


    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;


    private Note note;
    private int position;
    private NoteHelper noteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_update);

        ButterKnife.bind(this);

        btnSubmit.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        Uri uri = getIntent().getData();


        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) note = new Note(cursor);
                cursor.close();
            }

        }

        String actionBarTitle = null;
        String btnTitle = null;

        if (isEdit) {
            actionBarTitle = "Ubah";
            btnTitle = "Update";
            edtTitle.setText(note.getTitle());
            edtDescription.setText(note.getDescription());

        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";

        }

        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit.setText(btnTitle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            boolean isEmpty = false;

            /*
             * Jika fieldnya masih kosong maka tampilkan error
             * */

            if (TextUtils.isEmpty(title)) {
                isEmpty = true;
                edtTitle.setError("Field can not be blank");

            }

            if (!isEmpty) {

                //gunakan contentvalues untuk menampung data
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(DESCRIPTION, description);


                /*
                 * Jika merupakan edit setResult nya UPDATE, dan kila bukan  maka setresultnya ADD
                 *
                 * */

                if (isEdit) {
                    getContentResolver().update(getIntent().getData(), values, null, null);

                    setResult(RESULT_UPDATE);
                    finish();
                } else {
                    values.put(DATE, getCurrentDate());

                    getContentResolver().insert(CONTENT_URI, values);

                    setResult(RESULT_ADD);
                    finish();

                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);

    }

    final int ALERT_DIALOG_CLOSE = 10;
    final int ALERT_DIALOG_DELETE = 20;

       /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
     */

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah Anda ingin membatalkan perubahan pada form?";

        } else {
            dialogMessage = "Apakah Anda yakin ingin menghapus item ini?";
            dialogTitle = "Hapus Note";
        }


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            getContentResolver().delete(getIntent().getData(), null, null);
                            setResult(RESULT_DELETE, null);
                            finish();
                        }
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }
}
