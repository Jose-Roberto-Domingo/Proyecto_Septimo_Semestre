package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistraActivity extends AppCompatActivity {

    private EditText email, contra;
    private Button btnRegistrar, btnCancelar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra);

        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referencias a las vistas
        email = findViewById(R.id.email_registro);
        contra = findViewById(R.id.contra_registro);
        btnRegistrar = findViewById(R.id.btn_registrar);
        btnCancelar = findViewById(R.id.btn_cancelar);

        // Acción para registrar usuario
        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        // Acción para cancelar y regresar al LoginActivity
        btnCancelar.setOnClickListener(v -> {
            startActivity(new Intent(RegistraActivity.this, LoginActivity.class));
            finish(); // Cierra la actividad actual
        });
    }

    private void registrarUsuario() {
        String emailUsuario = email.getText().toString().trim();
        String contraUsuario = contra.getText().toString().trim();

        // Validar campos vacíos
        if (emailUsuario.isEmpty() || contraUsuario.isEmpty()) {
            Toast.makeText(RegistraActivity.this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(emailUsuario, contraUsuario)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistraActivity.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                        // Redirigir al MainActivity
                        startActivity(new Intent(RegistraActivity.this, MainActivity.class));
                        finish(); // Cierra esta actividad
                    } else {
                        Toast.makeText(RegistraActivity.this, "Error al registrar usuario.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegistraActivity.this, "El usuario ya está registrado.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistraActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
