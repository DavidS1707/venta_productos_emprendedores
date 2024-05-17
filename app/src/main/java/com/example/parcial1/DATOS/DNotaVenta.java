package com.example.parcial1.DATOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DNotaVenta extends DbHelper {
    Context context;

    private long id;
    private int codigo;
    private String nombreCliente;
    private int cantidad;
    private String tipoEnvio;
    private String tipoPago;
    private long idProducto;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }

    public DNotaVenta(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long addNotaVenta(String nombreCliente, int cantidad, String tipoEnvio, long idProducto, String tipoPago) {
        long id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre_cliente", nombreCliente);
            values.put("cantidad", cantidad);
            values.put("tipo_envio", tipoEnvio);
            values.put("id_producto", idProducto);
            values.put("tipo_pago", tipoPago);

            // No es necesario agregar el campo "codigo", se generará automáticamente
            id = db.insert(TABLE_NOTAVENTA, null, values);

            // Formatear el ID para asegurar que el código tenga tres dígitos
            String formattedId = String.format("%03d", id);

            // Actualizar el campo "codigo" con el código formateado
            ContentValues updateValues = new ContentValues();
            updateValues.put("codigo", formattedId);
            db.update(TABLE_NOTAVENTA, updateValues, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception ex) {
            Log.e("AddNotaVenta", "Error al insertar la nota de venta: " + ex.getMessage());
        }

        return id;
    }

    public ArrayList<DNotaVenta> mostrarNotasVenta() {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<DNotaVenta> listaNotasVenta = new ArrayList<>();
        DNotaVenta notaVenta;
        Cursor cursorNotaVenta = null;

        cursorNotaVenta = db.rawQuery("SELECT * FROM " + TABLE_NOTAVENTA, null);

        if (cursorNotaVenta != null && cursorNotaVenta.moveToFirst()) {
            do {
                notaVenta = new DNotaVenta(context);
                int codigoIndex = cursorNotaVenta.getColumnIndex("codigo");
                int nombreIndex = cursorNotaVenta.getColumnIndex("nombre_cliente");
                int cantidadIndex = cursorNotaVenta.getColumnIndex("cantidad");
                int tipoEnvioIndex = cursorNotaVenta.getColumnIndex("tipo_envio");
                int tipoPagoIndex = cursorNotaVenta.getColumnIndex("tipo_pago"); // Agregar el índice para tipo de pago
                int idProductoIndex = cursorNotaVenta.getColumnIndex("id_producto");

                // Verificar si los índices de las columnas son válidos
                if (codigoIndex != -1 && nombreIndex != -1 && cantidadIndex != -1 && tipoEnvioIndex != -1 && tipoPagoIndex != -1 && idProductoIndex != -1) {
                    notaVenta.setCodigo(cursorNotaVenta.getInt(codigoIndex));
                    notaVenta.setNombreCliente(cursorNotaVenta.getString(nombreIndex));
                    notaVenta.setCantidad(cursorNotaVenta.getInt(cantidadIndex));
                    notaVenta.setTipoEnvio(cursorNotaVenta.getString(tipoEnvioIndex));
                    notaVenta.setTipoPago(cursorNotaVenta.getString(tipoPagoIndex)); // Establecer el tipo de pago
                    notaVenta.setIdProducto(cursorNotaVenta.getLong(idProductoIndex));
                    listaNotasVenta.add(notaVenta);
                } else {
                    Log.e("mostrarNotasVenta", "Las columnas no existen en el cursor");
                }
            } while (cursorNotaVenta.moveToNext());
        }
        if (cursorNotaVenta != null) {
            cursorNotaVenta.close();
        }
        return listaNotasVenta;
    }

    public boolean eliminarNotaVenta(long idNotaVenta) {
        boolean success = false;
        SQLiteDatabase db = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            db = dbHelper.getWritableDatabase();

            int filasAfectadas = db.delete(TABLE_NOTAVENTA, "id=?", new String[]{String.valueOf(idNotaVenta)});
            success = filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return success;
    }

}
