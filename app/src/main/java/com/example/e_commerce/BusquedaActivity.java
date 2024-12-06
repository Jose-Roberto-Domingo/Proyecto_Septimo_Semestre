package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

public class BusquedaActivity extends AppCompatActivity {

    ListView listViewEcommerce1, listViewEcommerce2;
    Button btnComparar, btnVolver;
    MaterialSearchBar searchBar;
    LinearLayout layoutResultados, layoutComparacion;
    TextView tvDetallesEcommerce1, tvDetallesEcommerce2;

    private String selectedProductEcommerce1 = null;
    private String selectedProductEcommerce2 = null;

    private ArrayList<String> walmartResults = new ArrayList<>();
    private ArrayList<String> ebayResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        // Inicializar las vistas
        listViewEcommerce1 = findViewById(R.id.listViewEcommerce1);
        listViewEcommerce2 = findViewById(R.id.listViewEcommerce2);
        btnComparar = findViewById(R.id.btnComparar);
        searchBar = findViewById(R.id.searchBar);
        btnVolver = findViewById(R.id.btnVolver);
        layoutResultados = findViewById(R.id.layoutResultados);

        // Obtener el término de búsqueda desde el Intent
        Intent intent = getIntent();
        String searchQuery = intent.getStringExtra("searchQuery");

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Buscar productos en la base de datos
            buscarProductosEnBaseDeDatos(searchQuery);
        } else {
            Toast.makeText(this, "No se recibió ningún término de búsqueda.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurar el botón Volver
        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(BusquedaActivity.this, MainActivity.class));
            finish();
        });
        // Configurar MaterialSearchBar
        configurarSearchBar();
    }

    private void configurarSearchBar() {
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // No se requiere acción específica al abrir o cerrar el search bar
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // Cuando el usuario confirma la búsqueda
                String searchQuery = text.toString().trim();
                if (!searchQuery.isEmpty()) {
                    buscarProductosEnBaseDeDatos(searchQuery);
                } else {
                    Toast.makeText(BusquedaActivity.this, "Por favor ingresa un término de búsqueda.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                // Opcional: Manejar acciones adicionales si se necesita
            }
        });
    }

    private void buscarProductosEnBaseDeDatos(String searchQuery) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        walmartResults.clear();
                        ebayResults.clear();

                        // Filtrar productos que coincidan con el término de búsqueda
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String categoria = document.getString("categoria");
                            String tienda = document.getString("tienda");
                            Double precio = document.getDouble("precio");

                            if ((nombre != null && nombre.toLowerCase().contains(searchQuery.toLowerCase())) ||
                                    (categoria != null && categoria.toLowerCase().contains(searchQuery.toLowerCase()))) {

                                String producto = (nombre != null ? nombre : "Producto desconocido") +
                                        " - $" + (precio != null ? precio : "0.0");


                                if ("Walmart".equalsIgnoreCase(tienda)) {
                                    walmartResults.add(producto);
                                } else if ("eBay".equalsIgnoreCase(tienda)) {
                                    ebayResults.add(producto);
                                }
                            }
                        }

                        // Configurar los ListViews
                        configurarListViews();
                    } else {
                        Toast.makeText(this, "Error al obtener productos de la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configurarListViews() {
        ArrayAdapter<String> walmartAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                walmartResults
        );
        listViewEcommerce1.setAdapter(walmartAdapter);

        ArrayAdapter<String> ebayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                ebayResults
        );
        listViewEcommerce2.setAdapter(ebayAdapter);

        // Acción cuando se selecciona un producto de Walmart
        listViewEcommerce1.setOnItemClickListener((parent, view, position, id) -> {
            selectedProductEcommerce1 = walmartResults.get(position);
            checkIfReadyToCompare();
        });

        // Acción cuando se selecciona un producto de eBay
        listViewEcommerce2.setOnItemClickListener((parent, view, position, id) -> {
            selectedProductEcommerce2 = ebayResults.get(position);
            checkIfReadyToCompare();
        });

        // Mensajes de lista vacía
        if (walmartResults.isEmpty()) {
            Toast.makeText(this, "No se encontraron productos en Walmart.", Toast.LENGTH_SHORT).show();
        }
        if (ebayResults.isEmpty()) {
            Toast.makeText(this, "No se encontraron productos en eBay.", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkIfReadyToCompare() {
        btnComparar.setEnabled(selectedProductEcommerce1 != null && selectedProductEcommerce2 != null);

        // Configurar el botón Comparar
        btnComparar.setOnClickListener(v -> {
            if (selectedProductEcommerce1 != null && selectedProductEcommerce2 != null) {
                layoutResultados.setVisibility(View.GONE);
                layoutComparacion.setVisibility(View.VISIBLE);

                tvDetallesEcommerce1.setText("Detalles de: " + selectedProductEcommerce1);
                tvDetallesEcommerce2.setText("Detalles de: " + selectedProductEcommerce2);
            } else {
                Toast.makeText(this, "Selecciona productos de ambas tiendas para comparar.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
