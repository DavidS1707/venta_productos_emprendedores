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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial1.NEGOCIO.NEmprendimiento;
import com.example.parcial1.DATOS.DEmprendimiento;
import com.example.parcial1.R;

import java.util.ArrayList;

public class PEmprendimiento extends AppCompatActivity {

    RecyclerView listaEmprendimiento;
    ArrayList<DEmprendimiento> listaArrayEmprendimiento;
    EmprendimientoAdapter adapter;

    EditText txtNombre, txtDescripcion;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regemprendimiento);

        listaEmprendimiento = findViewById(R.id.listaEmprendimiento);
        listaEmprendimiento.setLayoutManager(new LinearLayoutManager(this));
        listaArrayEmprendimiento = new ArrayList<>();
        adapter = new EmprendimientoAdapter(listaArrayEmprendimiento, this);
        listaEmprendimiento.setAdapter(adapter);

        txtNombre = findViewById(R.id.nombreEmp);
        txtDescripcion = findViewById(R.id.descripcionEmp);
        btnGuardar = findViewById(R.id.btnGuardarEmp);

        cargarEmprendimientos();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString();
                String descripcion = txtDescripcion.getText().toString();

                NEmprendimiento nEmprendimiento = new NEmprendimiento();
                long id = nEmprendimiento.addEmprendimiento(PEmprendimiento.this, nombre, descripcion);
                if (id > 0) {
                    DEmprendimiento nuevoEmprendimiento = new DEmprendimiento();
                    nuevoEmprendimiento.setId((int) id);
                    nuevoEmprendimiento.setNombre(nombre);
                    nuevoEmprendimiento.setDescripcion(descripcion);
                    listaArrayEmprendimiento.add(nuevoEmprendimiento);
                    adapter.notifyDataSetChanged();
                    limpiarCampos();
                    Toast.makeText(PEmprendimiento.this, "Emprendimiento guardado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PEmprendimiento.this, "Error al guardar el emprendimiento", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarEmprendimientos() {
        NEmprendimiento nEmprendimiento = new NEmprendimiento();
        listaArrayEmprendimiento.addAll(nEmprendimiento.mostrarEmprendimiento(this));
        adapter.notifyDataSetChanged();
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
    }

    // Adaptador para el RecyclerView de los emprendimientos
    public static class EmprendimientoAdapter extends RecyclerView.Adapter<EmprendimientoAdapter.ViewHolder> {

        private ArrayList<DEmprendimiento> mEmprendimientos;
        private Context mContext;

        public EmprendimientoAdapter(ArrayList<DEmprendimiento> emprendimientos, Context context) {
            mEmprendimientos = emprendimientos;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emprendimiento, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DEmprendimiento emprendimiento = mEmprendimientos.get(position);
            holder.txtNombre.setText(emprendimiento.getNombre());
            holder.txtDescripcion.setText(emprendimiento.getDescripcion());

            holder.btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Editar Emprendimiento");

                    View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_emprendimiento, null);

                    EditText editTextNombre = viewInflated.findViewById(R.id.editTextNombre);
                    EditText editTextDescripcion = viewInflated.findViewById(R.id.editTextDescripcion);

                    editTextNombre.setText(emprendimiento.getNombre());
                    editTextDescripcion.setText(emprendimiento.getDescripcion());

                    builder.setView(viewInflated);

                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nuevoNombre = editTextNombre.getText().toString().trim();
                            String nuevaDescripcion = editTextDescripcion.getText().toString().trim();
                            if (!nuevoNombre.isEmpty()) {
                                NEmprendimiento nEmprendimiento = new NEmprendimiento();
                                boolean success = nEmprendimiento.editarEmprendimiento(mContext, emprendimiento.getId(), nuevoNombre, nuevaDescripcion);
                                if (success) {
                                    emprendimiento.setNombre(nuevoNombre);
                                    emprendimiento.setDescripcion(nuevaDescripcion);
                                    notifyItemChanged(holder.getAdapterPosition());
                                    Toast.makeText(mContext, "Emprendimiento editado correctamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "Error al editar el emprendimiento", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Ingrese un nuevo nombre para el emprendimiento", Toast.LENGTH_SHORT).show();
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

            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Eliminar Emprendimiento");
                    builder.setMessage("¿Estás seguro de que deseas eliminar este emprendimiento?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NEmprendimiento nEmprendimiento = new NEmprendimiento();
                            boolean eliminado = nEmprendimiento.eliminarEmprendimiento(mContext, emprendimiento.getId());
                            if (eliminado) {
                                mEmprendimientos.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                Toast.makeText(mContext, "Emprendimiento eliminado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error al eliminar el emprendimiento", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            return mEmprendimientos.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtNombre;
            public TextView txtDescripcion;
            public Button btnEditar;
            public Button btnEliminar;

            public ViewHolder(View itemView) {
                super(itemView);
                txtNombre = itemView.findViewById(R.id.txtNombre);
                txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
                btnEditar = itemView.findViewById(R.id.btnEditar);
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
            }
        }
    }
}
