package com.example.parcial1.NEGOCIO;

import android.content.Context;

import com.example.parcial1.DATOS.DCatalogo;
import com.example.parcial1.DATOS.DProducto;

import java.util.ArrayList;
import java.util.List;

public class NProducto {

    public long addProducto(Context context, String nombre, double precio, long idCatalogo, long idEmprendimiento) {
        return new DProducto().addProducto(context, nombre, precio, idCatalogo, idEmprendimiento);
    }

    public ArrayList<DProducto> mostrarProductos(Context context) {
        DProducto dProducto = new DProducto();
        return dProducto.mostrarProductos(context);
    }

    public boolean editarProducto(Context context, int idProducto, String nuevoNombre, double nuevoPrecio, long nuevoIdCatalogo) {
        DProducto dProducto = new DProducto();
        return dProducto.editarProducto(context, idProducto, nuevoNombre, nuevoPrecio, nuevoIdCatalogo);
    }

    public boolean eliminarProducto(Context context, int idProducto) {
        DProducto dProducto = new DProducto();
        return dProducto.eliminarProducto(context, idProducto);
    }

}
