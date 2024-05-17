package com.example.parcial1.DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DCatalogo {
    private int id;
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long AddCatalogo(Context context, String nombre) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);

            id = db.insert("t_catalogo", null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    public ArrayList<DCatalogo> mostrarCatalogo(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<DCatalogo> listaCatalogo = new ArrayList<>();
        Cursor cursorCatalogo = null;

        try {
            cursorCatalogo = db.query("t_catalogo", null, null, null, null, null, null);

            if (cursorCatalogo != null) {
                int columnIndexId = cursorCatalogo.getColumnIndex("id");
                int columnIndexNombre = cursorCatalogo.getColumnIndex("nombre");

                if (columnIndexId != -1 && columnIndexNombre != -1) {
                    while (cursorCatalogo.moveToNext()) {
                        DCatalogo catalogo = new DCatalogo();
                        catalogo.setId(cursorCatalogo.getInt(columnIndexId));
                        catalogo.setNombre(cursorCatalogo.getString(columnIndexNombre));
                        listaCatalogo.add(catalogo);
                    }
                } else {
                    Log.e("mostrarCatalogo", "Las columnas 'id' y 'nombre' no existen en el cursor");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursorCatalogo != null) {
                cursorCatalogo.close();
            }
            db.close();
        }

        return listaCatalogo;
    }


    public boolean editarCatalogo(Context context, long idCatalogo, String nuevoNombre) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nuevoNombre);

        int rowsAffected = db.update("t_catalogo", values, "id=?", new String[]{String.valueOf(idCatalogo)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarCatalogo(Context context, long idCatalogo) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsAffected = db.delete("t_catalogo", "id=?", new String[]{String.valueOf(idCatalogo)});
        db.close();
        return rowsAffected > 0;
    }

    public List<String> obtenerNombresCatalogos(Context context) {
        List<String> nombresCatalogos = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(DbHelper.TABLE_CATALOGO, new String[]{"nombre"}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("nombre");
                if (columnIndex != -1) {
                    do {
                        String nombreCatalogo = cursor.getString(columnIndex);
                        nombresCatalogos.add(nombreCatalogo);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("DCatalogo", "Error al obtener nombres de catálogos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return nombresCatalogos;
    }

}
