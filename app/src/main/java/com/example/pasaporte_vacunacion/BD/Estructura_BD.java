package com.example.pasaporte_vacunacion.BD;

import java.text.DateFormat;
import java.util.Date;

public class Estructura_BD {
    private Estructura_BD(){}

    public static final String TABLE_NAME="USUARIOS";
    public static final String NOMBRE_COLUMN1="_Id";
    public static final String NOMBRE_COLUMN2="Dni";
    public static final String NOMBRE_COLUMN3="Correo";
    public static final String NOMBRE_COLUMN4="Password";
    //public static final String NOMBRE_COLUMN3="Dni_vencimiento";
    //public static final String NOMBRE_COLUMN4="Nacimiento";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Estructura_BD.TABLE_NAME + " (" +
                    Estructura_BD.NOMBRE_COLUMN1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Estructura_BD.NOMBRE_COLUMN2 + TEXT_TYPE + COMMA_SEP +
                    Estructura_BD.NOMBRE_COLUMN3 + TEXT_TYPE + COMMA_SEP +
                    Estructura_BD.NOMBRE_COLUMN4 + TEXT_TYPE + ")";
                    //Estructura_BD.NOMBRE_COLUMN3 + TEXT_TYPE + COMMA_SEP +
                    //Estructura_BD.NOMBRE_COLUMN4 + TEXT_TYPE + COMMA_SEP +

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Estructura_BD.TABLE_NAME;
}
