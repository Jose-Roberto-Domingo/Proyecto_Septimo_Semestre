package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ComentarioActivity extends AppCompatActivity {

    TextInputEditText etNombreUsuario, etComentario, etProducto; // Ahora etProducto es un TextInputEditText
    Button btnGuardar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        // Inicializar vistas
        etNombreUsuario = findViewById(R.id.et_nombre_usuario);
        etComentario = findViewById(R.id.et_comentario);
        etProducto = findViewById(R.id.et_producto); // Cambiado a TextInputEditText
        btnGuardar = findViewById(R.id.btn_guardar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        // Acción para el botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String nombreUsuario = etNombreUsuario.getText().toString().trim();
            String productoSeleccionado = etProducto.getText().toString().trim();
            String comentario = etComentario.getText().toString().trim();

            if (nombreUsuario.isEmpty() || productoSeleccionado.isEmpty() || comentario.isEmpty()) {
                Toast.makeText(ComentarioActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            } else {
                String nuevoComentario = nombreUsuario + " comentó sobre " + productoSeleccionado + ": " + comentario;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nuevo_comentario", nuevoComentario);
                setResult(RESULT_OK, resultIntent);
                finish(); // Finalizar la actividad y devolver el resultado
            }
        });


        // Acción para el botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            startActivity(new Intent(ComentarioActivity.this, MainActivity.class));
            finish(); // Finalizar esta actividad
        });
    }
}
