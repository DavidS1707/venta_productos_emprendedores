package com.example.parcial1.DATOS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NOMBRE = "parcial.db";
    public static final String TABLE_EMPRENDIMIENTO = "t_emprendimiento";
    public static final String TABLE_PRODUCTO = "t_producto";
    public static final String TABLE_CATALOGO = "t_catalogo";
    public static final String TABLE_NOTAVENTA = "t_notaventa";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla emprendimiento
        String createTableEmprendimiento = "CREATE TABLE " + TABLE_EMPRENDIMIENTO + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT,"
                + "descripcion TEXT"
                + ");";

        // Crear tabla producto
        String createTableProducto = "CREATE TABLE " + TABLE_PRODUCTO + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT,"
                + "precio INTEGER,"
                + "id_emprendimiento INTEGER,"
                + "id_catalogo INTEGER,"
                + "FOREIGN KEY (id_emprendimiento) REFERENCES " + TABLE_EMPRENDIMIENTO + "(id),"
                + "FOREIGN KEY (id_catalogo) REFERENCES " + TABLE_CATALOGO + "(id)"
                + ");";

        // Crear tabla catalogo
        String createTableCatalogo = "CREATE TABLE " + TABLE_CATALOGO + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT"
                + ");";

        // Crear tabla notaventa
        String createTableNotaVenta = "CREATE TABLE " + TABLE_NOTAVENTA + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "codigo INTEGER UNIQUE,"
                + "nombre_cliente TEXT,"
                + "cantidad INTEGER,"
                + "tipo_envio TEXT,"
                + "tipo_pago TEXT,"
                + "id_producto INTEGER,"
                + "FOREIGN KEY (id_producto) REFERENCES " + TABLE_PRODUCTO + "(id)"
                + ");";

        // Ejecutar las sentencias SQL para crear las tablas
        db.execSQL(createTableEmprendimiento);
        db.execSQL(createTableProducto);
        db.execSQL(createTableCatalogo);
        db.execSQL(createTableNotaVenta);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar las tablas existentes si ya existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPRENDIMIENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOGO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAVENTA);

        // Volver a crear las tablas
        onCreate(db);
    }

}
