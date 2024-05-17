package com.example.parcial1.PRESENTACION;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.parcial1.DATOS.DbHelper;
import com.example.parcial1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones
        Button btnRegistrarEmprendimiento = findViewById(R.id.btnRegistrarEmprendimiento);
        Button btnCatalogos = findViewById(R.id.btnCatalogos);
        Button btnProductos = findViewById(R.id.btnProductos);
        Button btnNotasVenta = findViewById(R.id.btnNotasVenta);

        // Crear la base de datos
        btnRegistrarEmprendimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear la base de datos
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Intent intent = new Intent(MainActivity.this, PEmprendimiento.class);
                startActivity(intent);
            }
        });


        //Boton para ir a la vista de los catalogos
        btnCatalogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PCatalogo.class);
                startActivity(intent);
            }
        });

        //Boton para ir a la vista de los productos
        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PProducto.class);
                startActivity(intent);
            }
        });

        //Boton para ir a la vista de las notas de venta
        btnNotasVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PNotaVenta.class);
                startActivity(intent);
            }
        });
    }
}
