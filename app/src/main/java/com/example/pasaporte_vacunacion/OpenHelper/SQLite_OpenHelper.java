package com.example.pasaporte_vacunacion.OpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pasaporte_vacunacion.BD.Estructura_BD;

public class SQLite_OpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "vacuna_pass.db";

    public SQLite_OpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Estructura_BD.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(Estructura_BD.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }

    public void openDB(){
        this.getWritableDatabase();
    }

    public Cursor consultarUsuario(String user, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                    Estructura_BD.NOMBRE_COLUMN2,
                    Estructura_BD.NOMBRE_COLUMN3,
                    Estructura_BD.NOMBRE_COLUMN4,
                    //Estructura_BD.NOMBRE_COLUMN5,
                    //Estructura_BD.NOMBRE_COLUMN6,
                };
        String selection = Estructura_BD.NOMBRE_COLUMN2 + " like ? and " + Estructura_BD.NOMBRE_COLUMN4 + " like ?";
        String[] selectionArgs = {user,pass};
        Cursor c = db.query(
                Estructura_BD.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        return c;
    }
}
