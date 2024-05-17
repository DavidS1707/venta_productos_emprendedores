package com.example.parcial1.PRESENTACION;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial1.DATOS.DProducto;
import com.example.parcial1.NEGOCIO.NCatalogo;
import com.example.parcial1.NEGOCIO.NEmprendimiento;
import com.example.parcial1.NEGOCIO.NProducto;
import com.example.parcial1.R;

import java.util.ArrayList;
import java.util.List;

public class PProducto extends AppCompatActivity {

    EditText txtNombre, txtPrecio;
    Spinner spinnerCatalogo;
    Button btnGuardar;
    RecyclerView listaProductos;
    ArrayList<DProducto> listaArrayProducto;
    long idCatalogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regproducto);

        txtNombre = findViewById(R.id.nombreProd);
        txtPrecio = findViewById(R.id.precioProd);
        spinnerCatalogo = findViewById(R.id.spinnerCatalogo);
        btnGuardar = findViewById(R.id.btnGuardarProd);
        listaProductos = findViewById(R.id.listaProductos);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));
        listaArrayProducto = new ArrayList<>();

        idCatalogo = getIntent().getLongExtra("idCatalogo", -1);

        cargarCatalogos();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString();
                double precio = Double.parseDouble(txtPrecio.getText().toString());
                long catalogoSeleccionado = spinnerCatalogo.getSelectedItemId();

                NEmprendimiento nEmprendimiento = new NEmprendimiento();
                long idEmprendimiento = nEmprendimiento.obtenerIdEmprendimiento(PProducto.this);

                NProducto nProducto = new NProducto();
                long id = nProducto.addProducto(PProducto.this, nombre, precio, catalogoSeleccionado, idEmprendimiento);

                if (id > 0) {
                    Toast.makeText(PProducto.this, "Producto guardado con éxito", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                    actualizarListaProductos();
                } else {
                    Toast.makeText(PProducto.this, "ERROR al guardar el producto", Toast.LENGTH_LONG).show();
                }
            }
        });


        actualizarListaProductos();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
    }

    private void cargarCatalogos() {
        NCatalogo nCatalogo = new NCatalogo();
        List<String> nombresCatalogos = nCatalogo.obtenerNombresCatalogos(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCatalogos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCatalogo.setAdapter(adapter);
    }

    private void actualizarListaProductos() {
        NProducto nProducto = new NProducto();
        listaArrayProducto.clear();
        listaArrayProducto.addAll(nProducto.mostrarProductos(this));
        ProductoAdapter adapter = new ProductoAdapter(listaArrayProducto, idCatalogo);
        listaProductos.setAdapter(adapter);
    }

    public static class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

        private ArrayList<DProducto> listaProductos;
        private long idCatalogo;

        public ProductoAdapter(ArrayList<DProducto> listaProductos, long idCatalogo) {
            this.listaProductos = listaProductos;
            this.idCatalogo = idCatalogo;
        }

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
            return new ProductoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            DProducto producto = listaProductos.get(position);
            holder.bind(producto);
            holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarDialogoEditarProducto(holder.itemView.getContext(), producto, idCatalogo);
                }
            });
            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarProducto(holder.itemView.getContext(), producto.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaProductos.size();
        }

        public static class ProductoViewHolder extends RecyclerView.ViewHolder {

            private TextView txtNombreProducto;
            private TextView txtPrecioProducto;
            private Button btnEditar;
            private Button btnEliminar;

            public ProductoViewHolder(@NonNull View itemView) {
                super(itemView);
                txtNombreProducto = itemView.findViewById(R.id.nombreProducto);
                txtPrecioProducto = itemView.findViewById(R.id.precioProducto);
                btnEditar = itemView.findViewById(R.id.btnEditar);
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
            }

            public void bind(DProducto producto) {
                txtNombreProducto.setText(producto.getNombre());
                txtPrecioProducto.setText(String.valueOf(producto.getPrecio()));
            }
        }

        private static void mostrarDialogoEditarProducto(Context context, DProducto producto, long idCatalogo) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_editar_producto, null);

            EditText edtNombre = view.findViewById(R.id.edtNombre);
            EditText edtPrecio = view.findViewById(R.id.edtPrecio);

            edtNombre.setText(producto.getNombre());
            edtPrecio.setText(String.valueOf(producto.getPrecio()));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Editar Producto")
                    .setView(view)
                    .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editarProducto(context, producto.getId(), edtNombre.getText().toString(), Double.parseDouble(edtPrecio.getText().toString()), idCatalogo);
                            ((PProducto) context).actualizarListaProductos();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create()
                    .show();
        }

        private static void editarProducto(Context context, int idProducto, String nombre, double precio, long idCatalogo) {
            NProducto nProducto = new NProducto();
            boolean resultado = nProducto.editarProducto(context, idProducto, nombre, precio, idCatalogo);
            if(resultado) {
                Toast.makeText(context, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
            }
        }

        private static void eliminarProducto(Context context, int idProducto) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("¿Estás seguro de eliminar este producto?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NProducto nProducto = new NProducto();
                            boolean resultado = nProducto.eliminarProducto(context, idProducto);
                            if(resultado) {
                                Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                ((PProducto) context).actualizarListaProductos();
                            } else {
                                Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        }
    }
}
