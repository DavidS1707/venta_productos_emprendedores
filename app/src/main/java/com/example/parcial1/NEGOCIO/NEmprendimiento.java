package com.example.parcial1.NEGOCIO;

import android.content.Context;

import com.example.parcial1.DATOS.DEmprendimiento;

import java.util.ArrayList;

public class NEmprendimiento {

    public long addEmprendimiento(Context context, String nombre, String descripcion) {
        DEmprendimiento dEmprendimiento = new DEmprendimiento();
        return dEmprendimiento.addEmprendimiento(context, nombre, descripcion);
    }

    public ArrayList<DEmprendimiento> mostrarEmprendimiento(Context context) {
        DEmprendimiento dEmprendimiento = new DEmprendimiento();
        return dEmprendimiento.mostrarEmprendimiento(context);
    }

    public boolean editarEmprendimiento(Context context, long idEmprendimiento, String nuevoNombre, String nuevaDescripcion) {
        DEmprendimiento dEmprendimiento = new DEmprendimiento();
        return dEmprendimiento.editarEmprendimiento(context, (int) idEmprendimiento, nuevoNombre, nuevaDescripcion);
    }

    public boolean eliminarEmprendimiento(Context context, long idEmprendimiento) {
        DEmprendimiento dEmprendimiento = new DEmprendimiento();
        return dEmprendimiento.eliminarEmprendimiento(context, (int) idEmprendimiento);
    }
    public long obtenerIdEmprendimiento(Context context) {
        DEmprendimiento dEmprendimiento = new DEmprendimiento();
        return dEmprendimiento.obtenerIdEmprendimiento(context);
    }

}
