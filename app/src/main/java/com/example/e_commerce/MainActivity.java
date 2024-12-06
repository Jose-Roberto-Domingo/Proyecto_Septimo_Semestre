package com.example.e_commerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Vistas
    ListView lvRecientes, lvUltimaBusqueda, lvComentarios;
    MaterialSearchBar searchBar;
    Button btnAgregarComentario;
    RecyclerView recyclerViewProductos;
    ProductoAdapter productoAdapter;

    // Datos
    ArrayList<String> recientesList, ultimaBusquedaList, comentariosList;
    ArrayAdapter<String> adapterRecientes, adapterUltimaBusqueda, adapterComentarios;

    private static final int REQUEST_CODE_COMENTARIO = 1;

    @Override
    protected void onPause() {
        super.onPause();
        guardarDatos();
    }

    private void guardarDatos() {
        // Usar SharedPreferences para guardar las listas
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convertir listas en cadenas JSON o simples, y guardarlas
        editor.putString("recientes", new Gson().toJson(recientesList));
        editor.putString("ultima_busqueda", new Gson().toJson(ultimaBusquedaList));
        editor.putString("comentarios", new Gson().toJson(comentariosList));

        // Aplicar los cambios
        editor.apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        lvRecientes = findViewById(R.id.lv_recientes);
        lvUltimaBusqueda = findViewById(R.id.lv_ultima_busqueda);
        lvComentarios = findViewById(R.id.lv_comentarios);
        recyclerViewProductos = (RecyclerView) findViewById(R.id.recyclerViewProductos);
        searchBar = findViewById(R.id.searchBar);
        btnAgregarComentario = findViewById(R.id.btn_agregar_comentario);

        // Recuperar los datos de SharedPreferences
        cargarDatos();

        // Inicializar listas
        recientesList = new ArrayList<>();
        ultimaBusquedaList = new ArrayList<>();
        comentariosList = new ArrayList<>();


        adapterRecientes = new ArrayAdapter<>(this, R.layout.list_item, recientesList);
        adapterUltimaBusqueda = new ArrayAdapter<>(this, R.layout.list_item, ultimaBusquedaList);
        adapterComentarios = new ArrayAdapter<>(this, R.layout.list_item, comentariosList);

        // Configurar ListViews
        lvRecientes.setAdapter(adapterRecientes);
        lvUltimaBusqueda.setAdapter(adapterUltimaBusqueda);
        lvComentarios.setAdapter(adapterComentarios);

        // Configurar MaterialSearchBar
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                String searchQuery = text.toString().trim();
                if (!searchQuery.isEmpty()) {
                    agregarBusquedaReciente(searchQuery); // Guardar la búsqueda
                    buscarEnBaseDeDatos(searchQuery);    // Realizar la búsqueda en la base de datos
                } else {
                    Toast.makeText(MainActivity.this, "Por favor ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        // Mostrar la última búsqueda cuando se inicie la aplicación
        mostrarUltimaBusqueda();

        // Configurar botón para agregar comentario
        btnAgregarComentario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComentarioActivity.class);
            startActivityForResult(intent, REQUEST_CODE_COMENTARIO);
        });
    }
    ArrayList<Producto> productosList = new ArrayList<>();
    private void productos() {

        // Obtener los productos de Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String categoria = document.getString("categoria");
                            Double precio = document.getDouble("precio");
                            String imagenUrl = document.getString("imagenUrl");

                            if (nombre != null && categoria != null && precio != null && imagenUrl != null) {
                                Producto producto = new Producto(nombre, categoria, precio, imagenUrl);
                                productosList.add(producto);
                            }
                        }

                        // Ahora configuramos el RecyclerView
                        RecyclerView recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
                        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));

                        // Crear el adapter y asignarlo al RecyclerView
                        ProductoAdapter productoAdapter = new ProductoAdapter(this, productosList);
                        recyclerViewProductos.setAdapter(productoAdapter);
                    } else {
                        Toast.makeText(this, "Error al obtener productos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarBusquedaReciente(String query) {
        if (!recientesList.contains(query)) {
            recientesList.add(0, query); // Agregar la búsqueda reciente al principio
            if (recientesList.size() > 5) {
                recientesList.remove(5); // Limitar a 5 búsquedas recientes
            }
            adapterRecientes.notifyDataSetChanged();
        }

        // Actualizar la última búsqueda
        ultimaBusquedaList.clear();
        ultimaBusquedaList.add(query); // Solo mostrar la última búsqueda
        adapterUltimaBusqueda.notifyDataSetChanged();
    }

    private void mostrarUltimaBusqueda() {
        if (!ultimaBusquedaList.isEmpty()) {
            String ultimaBusqueda = ultimaBusquedaList.get(0);
            searchBar.setText(ultimaBusqueda);
        }
    }
    
    private void buscarEnBaseDeDatos(String searchQuery) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener todos los productos de la colección "productos"
        db.collection("productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        ArrayList<String> walmartProducts = new ArrayList<>();
                        ArrayList<String> ebayProducts = new ArrayList<>();

                        // Iterar sobre los documentos obtenidos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            String categoria = document.getString("categoria");
                            String tienda = document.getString("tienda");
                            double precio = document.getDouble("precio") != null ? document.getDouble("precio") : 0.0;

                            // Convertir el precio a String para comparación
                            String precioString = String.valueOf(precio);

                            // Verificar si alguno de los campos coincide con el término de búsqueda
                            if ((nombre != null && nombre.toLowerCase().contains(searchQuery.toLowerCase())) ||
                                    (categoria != null && categoria.toLowerCase().contains(searchQuery.toLowerCase())) ||
                                    (tienda != null && tienda.toLowerCase().contains(searchQuery.toLowerCase())) ||
                                    precioString.contains(searchQuery)) {

                                String producto = nombre + " - $" + precio;

                                // Clasificar productos por tienda
                                if ("Walmart".equalsIgnoreCase(tienda)) {
                                    walmartProducts.add(producto);
                                } else if ("eBay".equalsIgnoreCase(tienda)) {
                                    ebayProducts.add(producto);
                                }
                            }
                        }

                        // Verificar si hay resultados y redirigir a BusquedaActivity
                        if (!walmartProducts.isEmpty() || !ebayProducts.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, BusquedaActivity.class);
                            intent.putStringArrayListExtra("walmart_products", walmartProducts);
                            intent.putStringArrayListExtra("ebay_products", ebayProducts);
                            // Aquí debería enviarse el término de búsqueda
                            intent.putExtra("searchQuery", searchQuery);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "No se encontraron resultados.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Manejar errores al obtener los datos
                        Toast.makeText(MainActivity.this, "Error al obtener productos de la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void cargarDatos() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Recuperar las listas
        String recientesJson = sharedPreferences.getString("recientes", "[]");
        String ultimaBusquedaJson = sharedPreferences.getString("ultima_busqueda", "[]");
        String comentariosJson = sharedPreferences.getString("comentarios", "[]");

        // Convertir las cadenas JSON de vuelta a listas
        recientesList = new Gson().fromJson(recientesJson, new TypeToken<ArrayList<String>>(){}.getType());
        ultimaBusquedaList = new Gson().fromJson(ultimaBusquedaJson, new TypeToken<ArrayList<String>>(){}.getType());
        comentariosList = new Gson().fromJson(comentariosJson, new TypeToken<ArrayList<String>>(){}.getType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_COMENTARIO && resultCode == RESULT_OK && data != null) {
            String nuevoComentario = data.getStringExtra("nuevo_comentario");
            if (nuevoComentario != null) {
                agregarComentario(nuevoComentario);
            }
        }
    }

    // Agregar comentario a la lista
    private void agregarComentario(String comentario) {
        comentariosList.add(0, comentario); // Agregar el nuevo comentario al principio
        adapterComentarios.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "Comentario agregado", Toast.LENGTH_SHORT).show();
    }
}