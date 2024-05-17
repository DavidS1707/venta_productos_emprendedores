package com.example.parcial1.DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DProducto {
    private long id;
    private String nombre;
    private double precio;
    private long idCatalogo;

    public DProducto(long id, String nombre, double precio, long idCatalogo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.idCatalogo = idCatalogo;
    }

    public DProducto() {

    }

    public int getId() {
        return (int) id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public long getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(long idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public long addProducto(Context context, String nombre, double precio, long idCatalogo, long idEmprendimiento) {
        long id = -1;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("precio", precio);
            values.put("id_catalogo", idCatalogo);
            id = db.insert(DbHelper.TABLE_PRODUCTO, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return id;
    }

    public boolean eliminarProducto(Context context, long idProducto) {
        boolean success = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int rowsAffected = db.delete(DbHelper.TABLE_PRODUCTO, "id=?", new String[]{String.valueOf(idProducto)});
            success = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    public boolean editarProducto(Context context, long idProducto, String nuevoNombre, double nuevoPrecio, long idCatalogo) {
        boolean success = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", nuevoNombre);
            values.put("precio", nuevoPrecio);
            values.put("id_catalogo", idCatalogo);
            int rowsAffected = db.update(DbHelper.TABLE_PRODUCTO, values, "id=?", new String[]{String.valueOf(idProducto)});
            success = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return success;
    }

    public ArrayList<DProducto> mostrarProductos(Context context) {
        ArrayList<DProducto> productos = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_PRODUCTO, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int nombreIndex = cursor.getColumnIndex("nombre");
                int precioIndex = cursor.getColumnIndex("precio");
                int idCatalogoIndex = cursor.getColumnIndex("id_catalogo");

                if (idIndex != -1 && nombreIndex != -1 && precioIndex != -1 && idCatalogoIndex != -1) {
                    do {
                        DProducto producto = new DProducto();
                        producto.setId(cursor.getLong(idIndex));
                        producto.setNombre(cursor.getString(nombreIndex));
                        producto.setPrecio(cursor.getDouble(precioIndex));
                        producto.setIdCatalogo(cursor.getLong(idCatalogoIndex));
                        productos.add(producto);
                    } while (cursor.moveToNext());
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
        return productos;
    }
    public List<String> obtenerNombresProductos(Context context) {
        List<String> nombresProductos = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT nombre FROM " + DbHelper.TABLE_PRODUCTO, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nombreIndex = cursor.getColumnIndex("nombre");

                if (nombreIndex != -1) {
                    do {
                        String nombreProducto = cursor.getString(nombreIndex);
                        nombresProductos.add(nombreProducto);
                    } while (cursor.moveToNext());
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
        return nombresProductos;
    }

}
