// PCatalogo.java
package com.example.parcial1.PRESENTACION;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial1.NEGOCIO.NCatalogo;
import com.example.parcial1.DATOS.DCatalogo;
import com.example.parcial1.R;

import java.util.ArrayList;

public class PCatalogo extends AppCompatActivity {

    RecyclerView listaCatalogos;
    ArrayList<DCatalogo> listaArrayCatalogo;

    EditText txtNombreCat;
    Button btnGuardarCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_catalogo);

        listaCatalogos = findViewById(R.id.listaCatalogos);
        listaCatalogos.setLayoutManager(new LinearLayoutManager(this));
        listaArrayCatalogo = new ArrayList<>();

        txtNombreCat = findViewById(R.id.nombreCat);
        btnGuardarCat = findViewById(R.id.btnGuardarCat);

        cargarCatalogos();

        btnGuardarCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCatalogo = txtNombreCat.getText().toString();
                if (!nombreCatalogo.isEmpty()) {
                    NCatalogo nCatalogo = new NCatalogo();
                    long id = nCatalogo.AddCatalogo(getApplicationContext(), nombreCatalogo);
                    if (id > 0) {
                        // Si se guarda correctamente, agregar el nuevo catálogo a la lista y notificar al adaptador
                        DCatalogo nuevoCatalogo = new DCatalogo();
                        nuevoCatalogo.setId((int) id);
                        nuevoCatalogo.setNombre(nombreCatalogo);
                        listaArrayCatalogo.add(nuevoCatalogo);
                        listaCatalogos.getAdapter().notifyDataSetChanged();
                        txtNombreCat.setText("");
                    } else {
                        Toast.makeText(PCatalogo.this, "Error al guardar el catálogo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PCatalogo.this, "Ingrese un nombre para el catálogo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarCatalogos() {
        NCatalogo nCatalogo = new NCatalogo();
        listaArrayCatalogo = nCatalogo.mostrarCatalogo(getApplicationContext());

        CatalogoAdapter adapter = new CatalogoAdapter(listaArrayCatalogo, this);
        listaCatalogos.setAdapter(adapter);
    }

    public static class CatalogoAdapter extends RecyclerView.Adapter<CatalogoAdapter.CatalogoViewHolder> {

        private ArrayList<DCatalogo> listaCatalogos;
        private Context context;

        public CatalogoAdapter(ArrayList<DCatalogo> listaCatalogos, Context context) {
            this.listaCatalogos = listaCatalogos;
            this.context = context;
        }

        @NonNull
        @Override
        public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalogo, parent, false);
            return new CatalogoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
            DCatalogo catalogo = listaCatalogos.get(position);
            holder.bind(catalogo);

            // Botón Editar
            holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Editar catálogo");
                    final EditText input = new EditText(context);
                    input.setText(catalogo.getNombre());
                    builder.setView(input);
                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nuevoNombre = input.getText().toString().trim();
                            if (!nuevoNombre.isEmpty()) {
                                NCatalogo nCatalogo = new NCatalogo();
                                boolean success = nCatalogo.editarCatalogo(context, catalogo.getId(), nuevoNombre);
                                if (success) {
                                    catalogo.setNombre(nuevoNombre);
                                    notifyItemChanged(holder.getAdapterPosition());
                                    Toast.makeText(context, "Catálogo editado correctamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error al editar el catálogo", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(context, "Ingrese un nuevo nombre para el catálogo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

            // Botón Eliminar
            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Eliminar catálogo");
                    builder.setMessage("¿Estás seguro de que quieres eliminar este catálogo?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NCatalogo nCatalogo = new NCatalogo();
                            boolean success = nCatalogo.eliminarCatalogo(context, catalogo.getId());
                            if (success) {
                                listaCatalogos.remove(catalogo);
                                notifyItemRemoved(holder.getAdapterPosition());
                                Toast.makeText(context, "Catálogo eliminado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error al eliminar el catálogo", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaCatalogos.size();
        }

        public static class CatalogoViewHolder extends RecyclerView.ViewHolder {

            private TextView txtNombreCatalogo;
            private Button btnEditar;
            private Button btnEliminar;

            public CatalogoViewHolder(@NonNull View itemView) {
                super(itemView);
                txtNombreCatalogo = itemView.findViewById(R.id.nombreCatalogo);
                btnEditar = itemView.findViewById(R.id.btnEditar);
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
            }

            public void bind(DCatalogo catalogo) {
                txtNombreCatalogo.setText(catalogo.getNombre());
            }
        }
    }

}
