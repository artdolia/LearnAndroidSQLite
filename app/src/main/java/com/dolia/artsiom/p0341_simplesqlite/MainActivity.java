package com.dolia.artsiom.p0341_simplesqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity implements OnClickListener {

    final String LOG_TAG = "myLog";

    EditText etName, etEmail, etID;
    Button btnAdd, btnRead, btnClear, btnUpdate, btnDel;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etID = (EditText) findViewById(R.id.etID);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnClear = (Button) findViewById(R.id.btnClear);

        btnAdd.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v){

        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String id = etID.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()){

            case R.id.btnUpdate:

                if(id.equalsIgnoreCase("")){
                    break;
                }
                Log.d(LOG_TAG, "--- Update mytable: ---");

                cv.put("name", name);
                cv.put("email", email);

                int updateCount = db.update("myTable", cv, "id = ?",
                        new String[]{ id });
                Log.d(LOG_TAG, "Updated " + updateCount + " rows");
                break;

            case R.id.btnDel:

                if(id.equalsIgnoreCase("")){
                    break;
                }
                Log.d(LOG_TAG, "--- Delete from mytable: ---");

                int delCount = db.delete("myTable", "id = " + id, null);

                Log.d(LOG_TAG, "Deleted " + delCount + " rows");
                break;

            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert into myTable ---");

                cv.put("name", name);
                cv.put("email", email);

                long rowID = db.insert("myTable", null, cv);
                Log.d(LOG_TAG, "Row Inserted, rowID: " + rowID);
                break;

            case R.id.btnRead:
                Log.d(LOG_TAG, "--- Rows in myTable ---");
                Cursor cur = db.query("myTable", null, null, null, null, null, null);

                if(cur.moveToFirst()){

                    int idColIndex = cur.getColumnIndex("id");
                    int nameColIndex = cur.getColumnIndex("name");
                    int emailColIndex = cur.getColumnIndex("email");

                    do{
                        Log.d(LOG_TAG,
                                "ID = " + cur.getInt(idColIndex) +
                                ", name = " + cur.getString(nameColIndex) +
                                ", email = " + cur.getString(emailColIndex)
                        );
                    }while(cur.moveToNext());

                }else{
                    Log.d(LOG_TAG, "--- No Rows in myTable ---");
                }

                cur.close();
                break;

            case R.id.btnClear:
                Log.d(LOG_TAG, "--- Clear myTable ---");
                int clearCount = db.delete("myTable", null, null);
                Log.d(LOG_TAG, "Deleted " + clearCount + " rows");
                break;
        }
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context, "myDB", null, 1);
        }


        @Override
        public void onCreate(SQLiteDatabase db){

            Log.d(LOG_TAG, "--- onCreate database ---");

            db.execSQL("create table myTable("
                    +"id integer primary key autoincrement,"
                    +"name text,"
                    +"email text);");
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        }
    }
}
