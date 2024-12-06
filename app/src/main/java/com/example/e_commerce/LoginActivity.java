package com.example.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText email, contra;
    Button iniciar, registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referencias de las vistas
        email = findViewById(R.id.email);
        contra = findViewById(R.id.contra);
        iniciar = findViewById(R.id.iniciar);
        registrar = findViewById(R.id.registrar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = email.getText().toString().trim();
                String contraUser = contra.getText().toString().trim();

                if (emailUser.isEmpty() && contraUser.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Ingresa los datos", Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(emailUser,contraUser);
                }
            }
        });

        // Acción para abrir la actividad de registro
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistraActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loginUser(String emailUser, String contraUser){
        mAuth.signInWithEmailAndPassword(emailUser,contraUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()){
                  finish();
                  startActivity(new Intent(LoginActivity.this,MainActivity.class));
                  Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
              }else{
                  Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
              }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}