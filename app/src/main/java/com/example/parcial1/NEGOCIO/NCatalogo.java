package com.example.parcial1.NEGOCIO;

import android.content.Context;

import com.example.parcial1.DATOS.DCatalogo;

import java.util.ArrayList;
import java.util.List;

public class NCatalogo {

    public long AddCatalogo(Context context, String nombre) {
        DCatalogo dCatalogo = new DCatalogo();
        return dCatalogo.AddCatalogo(context, nombre);
    }

    public ArrayList<DCatalogo> mostrarCatalogo(Context context) {
        DCatalogo dCatalogo = new DCatalogo();
        return dCatalogo.mostrarCatalogo(context);
    }

    public boolean editarCatalogo(Context context, long idCatalogo, String nuevoNombre) {
        DCatalogo dCatalogo = new DCatalogo();
        return dCatalogo.editarCatalogo(context, idCatalogo, nuevoNombre);
    }

    public boolean eliminarCatalogo(Context context, long idCatalogo) {
        DCatalogo dCatalogo = new DCatalogo();
        return dCatalogo.eliminarCatalogo(context, idCatalogo);
    }

    public List<String> obtenerNombresCatalogos(Context context) {
        DCatalogo dCatalogo = new DCatalogo();
        return dCatalogo.obtenerNombresCatalogos(context);
    }
}
