package com.example.parcial1.PRESENTACION;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial1.DATOS.DNotaVenta;
import com.example.parcial1.NEGOCIO.NNotaVenta;
import com.example.parcial1.R;

import java.util.ArrayList;
import java.util.List;

public class PNotaVenta extends AppCompatActivity {

    EditText editTextNombreCliente, editTextCantidad;
    Spinner spinnerProductos;
    Button btnGuardarNotaVenta;
    RecyclerView recyclerViewNotasVenta;
    ArrayList<DNotaVenta> listaNotasVenta;
    NotaVentaAdapter adapter;
    NNotaVenta nNotaVenta;
    RadioGroup radioGroupTipoEnvio;
    RadioGroup radioGroupTipoPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_venta);

        editTextNombreCliente = findViewById(R.id.editTextNombreCliente);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        spinnerProductos = findViewById(R.id.spinnerProductos);
        btnGuardarNotaVenta = findViewById(R.id.btnGuardarNotaVenta);
        recyclerViewNotasVenta = findViewById(R.id.recyclerViewNotasVenta);

        listaNotasVenta = new ArrayList<>();
        adapter = new NotaVentaAdapter(listaNotasVenta, this);
        recyclerViewNotasVenta.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotasVenta.setAdapter(adapter);

        nNotaVenta = new NNotaVenta(this);

        // Cargar los productos en el Spinner
        cargarProductos();

        // Obtener referencia al RadioGroup de tipo de envío
        radioGroupTipoEnvio = findViewById(R.id.radioGroupTipoEnvio);
        radioGroupTipoPago = findViewById(R.id.radioGroupTipoPago);

        btnGuardarNotaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarNotaVenta();
            }
        });

        // Cargar las notas de venta desde la base de datos
        cargarNotasVenta();
    }

    private void guardarNotaVenta() {
        String nombreCliente = editTextNombreCliente.getText().toString();
        String cantidadText = editTextCantidad.getText().toString();

        if (spinnerProductos.getSelectedItem() != null) {
            long idProducto = spinnerProductos.getSelectedItemId();
            int cantidad = Integer.parseInt(cantidadText);

            // Obtener el tipo de envío seleccionado
            int idTipoEnvio = radioGroupTipoEnvio.getCheckedRadioButtonId();
            RadioButton radioButtonTipoEnvio = findViewById(idTipoEnvio);
            String tipoEnvio = radioButtonTipoEnvio.getText().toString();

            // Obtener el tipo de pago seleccionado
            int idTipoPago = radioGroupTipoPago.getCheckedRadioButtonId();
            RadioButton radioButtonTipoPago = findViewById(idTipoPago);
            String tipoPago = radioButtonTipoPago.getText().toString();

            long id = nNotaVenta.addNotaVenta(nombreCliente, cantidad, tipoEnvio, idProducto, tipoPago);

            if (id > 0) {
                Toast.makeText(PNotaVenta.this, "Nota de venta guardada con éxito", Toast.LENGTH_SHORT).show();
                cargarNotasVenta();
                limpiarCampos();
            } else {
                Toast.makeText(PNotaVenta.this, "Error al guardar la nota de venta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PNotaVenta.this, "No hay productos disponibles para seleccionar", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarProductos() {
        List<String> nombresProductos = nNotaVenta.obtenerNombresProductos();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresProductos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductos.setAdapter(adapter);
    }

    private void cargarNotasVenta() {
        listaNotasVenta.clear();
        listaNotasVenta.addAll(nNotaVenta.mostrarNotasVenta());
        adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    private void limpiarCampos() {
        editTextNombreCliente.setText("");
        editTextCantidad.setText("");
    }

    public class NotaVentaAdapter extends RecyclerView.Adapter<NotaVentaAdapter.NotaVentaViewHolder> {

        private ArrayList<DNotaVenta> mNotasVenta;
        private Context mContext;

        public NotaVentaAdapter(ArrayList<DNotaVenta> notasVenta, Context context) {
            mNotasVenta = notasVenta;
            mContext = context;
        }

        @Override
        public NotaVentaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notaventa, parent, false);
            return new NotaVentaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotaVentaViewHolder holder, int position) {
            DNotaVenta notaVenta = mNotasVenta.get(position);
            holder.bind(notaVenta);
            holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Eliminar Nota de Venta");
                    builder.setMessage("¿Estás seguro de que quieres eliminar esta nota de venta?");

                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            long idNotaVenta = mNotasVenta.get(holder.getAdapterPosition()).getId();
                            eliminarNotaVenta(idNotaVenta);
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

        private void eliminarNotaVenta(long idNotaVenta) {
            NNotaVenta nNotaVenta = new NNotaVenta(mContext);
            boolean eliminada = nNotaVenta.eliminarNotaVenta(idNotaVenta);
            if (eliminada) {
                Toast.makeText(mContext, "Nota de venta eliminada correctamente", Toast.LENGTH_SHORT).show();
                cargarNotasVenta();
            } else {
                Toast.makeText(mContext, "Error al eliminar la nota de venta", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public int getItemCount() {
            return mNotasVenta.size();
        }

        public class NotaVentaViewHolder extends RecyclerView.ViewHolder {

            private TextView txtCodigo, txtNombreCliente, txtCantidad, txtTipoEnvio, txtIdProducto, txtTipoPago;
            private Button btnEliminar;

            public NotaVentaViewHolder(View itemView) {
                super(itemView);
                txtCodigo = itemView.findViewById(R.id.txtCodigo);
                txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
                txtCantidad = itemView.findViewById(R.id.txtCantidad);
                txtTipoEnvio = itemView.findViewById(R.id.txtTipoEnvio);
                txtIdProducto = itemView.findViewById(R.id.txtIdProducto);
                txtTipoPago = itemView.findViewById(R.id.txtTipoPago);
                btnEliminar = itemView.findViewById(R.id.btnEliminarNota);
            }

            public void bind(DNotaVenta notaVenta) {
                txtCodigo.setText(String.format("%03d", notaVenta.getCodigo()));
                txtNombreCliente.setText(notaVenta.getNombreCliente());
                txtCantidad.setText(String.valueOf(notaVenta.getCantidad()));
                txtIdProducto.setText(String.valueOf(notaVenta.getIdProducto()));
                txtTipoEnvio.setText(notaVenta.getTipoEnvio());
                txtTipoPago.setText(notaVenta.getTipoPago());
            }
        }
    }

}
