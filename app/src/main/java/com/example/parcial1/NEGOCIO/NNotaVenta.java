package com.example.parcial1.NEGOCIO;

import android.content.Context;

import com.example.parcial1.DATOS.DNotaVenta;
import com.example.parcial1.DATOS.DProducto;

import java.util.ArrayList;
import java.util.List;

public class NNotaVenta {
    private Context context;
    private DNotaVenta dNotaVenta;

    public NNotaVenta(Context context) {
        this.context = context;
        this.dNotaVenta = new DNotaVenta(context);
    }

    public long addNotaVenta(String nombreCliente, int cantidad, String tipoEnvio, long idProducto, String tipoPago) {
        return dNotaVenta.addNotaVenta(nombreCliente, cantidad, tipoEnvio, idProducto, tipoPago);
    }

    public ArrayList<DNotaVenta> mostrarNotasVenta() {
        return dNotaVenta.mostrarNotasVenta();
    }

    public boolean eliminarNotaVenta(long idNotaVenta) {
        return dNotaVenta.eliminarNotaVenta(idNotaVenta);
    }

    public List<String> obtenerNombresProductos() {
        DProducto dProducto = new DProducto();
        return dProducto.obtenerNombresProductos(context);
    }

}
