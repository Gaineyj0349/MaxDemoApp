package com.gainwise.maxdemo.SQLiteUtil;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.*;

/**
 * Created by gaine on 3/13/2018.
 */

public class DBHelperShippedIn extends SQLiteOpenHelper {
    private Context mycontext;
    private static final int version = 6;


    private static String DB_NAME_IN = "_central_db.db";
    private static String DB_NAME_OUT = "_central_db_" + version +".db";//the extension may be .sqlite or .db
    public SQLiteDatabase exampleDB;
    private String DB_PATH = "";


    public DBHelperShippedIn(Context context) throws IOException {
        super(context, DB_NAME_OUT, null, version);
        this.mycontext = context;
        DB_PATH = "/data/data/"
                + mycontext.getApplicationContext().getPackageName()
                + "/databases/";
        boolean dbexist = checkdatabase();
        if (dbexist) {
            Log.i("MAXdemo ", "database exists1");
            opendatabase();
        } else {
            Log.i("MAXdemo ", "database doesn't exists1");
            createdatabase();
        }

    }

    public void createdatabase() throws IOException {
        boolean dbexist = checkdatabase();
        if (dbexist) {
            Log.i("MAXdemo ", "database exists2");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch (IOException e) {
                Log.i("MAXdemo", e.getMessage());
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME_OUT;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            Log.i("MAXdemo ", "database doesn't exists");
        }

        return checkdb;
    }

    private void copydatabase() throws IOException {

        //Open your local db as the input stream
        InputStream myinput = mycontext.getAssets().open(DB_NAME_IN);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME_OUT;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream("/data/data/com.gainwise.maxdemo/databases/"+DB_NAME_OUT);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();

    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DB_PATH + DB_NAME_OUT;
        exampleDB = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);

    }


    public synchronized void close() {
        if (exampleDB != null) {
            exampleDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DB_NAME2 = "_central_db_" + (version-1) +".db";
        mycontext.deleteDatabase(DB_NAME2);

    }

}