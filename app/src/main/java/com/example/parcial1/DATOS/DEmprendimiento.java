package com.example.parcial1.DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DEmprendimiento {
    private DbHelper dbHelper;
    private int id;
    private String nombre;
    private String descripcion;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long addEmprendimiento(Context context, String nombre, String descripcion) {
        long id = -1; // Valor predeterminado si no se puede agregar
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("descripcion", descripcion);
            id = db.insert(DbHelper.TABLE_EMPRENDIMIENTO, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return id;
    }

    public ArrayList<DEmprendimiento> mostrarEmprendimiento(Context context) {
        ArrayList<DEmprendimiento> emprendimientos = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String[] projection = {"id", "nombre", "descripcion"};
            Cursor cursor = db.query(DbHelper.TABLE_EMPRENDIMIENTO, projection, null, null, null, null, null);
            if (cursor != null) {
                int idColumnIndex = cursor.getColumnIndex("id");
                int nombreColumnIndex = cursor.getColumnIndex("nombre");
                int descripcionColumnIndex = cursor.getColumnIndex("descripcion");

                while (cursor.moveToNext()) {
                    DEmprendimiento emprendimiento = new DEmprendimiento();
                    emprendimiento.setId(cursor.getInt(idColumnIndex));
                    emprendimiento.setNombre(cursor.getString(nombreColumnIndex));
                    emprendimiento.setDescripcion(cursor.getString(descripcionColumnIndex));
                    emprendimientos.add(emprendimiento);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return emprendimientos;
    }


    public boolean editarEmprendimiento(Context context, int id, String nuevoNombre, String nuevaDescripcion) {
        boolean exito = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nuevoNombre);
            values.put("descripcion", nuevaDescripcion);
            int filasAfectadas = db.update(DbHelper.TABLE_EMPRENDIMIENTO, values, "id=?", new String[]{String.valueOf(id)});
            exito = filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return exito;
    }

    public boolean eliminarEmprendimiento(Context context, int id) {
        boolean exito = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int filasAfectadas = db.delete(DbHelper.TABLE_EMPRENDIMIENTO, "id=?", new String[]{String.valueOf(id)});
            exito = filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return exito;
    }

    public long obtenerIdEmprendimiento(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long idEmprendimiento = -1;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT id FROM " + DbHelper.TABLE_EMPRENDIMIENTO + " LIMIT 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex != -1) {
                    idEmprendimiento = cursor.getLong(idIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return idEmprendimiento;
    }

}
