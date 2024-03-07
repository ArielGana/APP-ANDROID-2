package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/ValidarRut.php";
    Button btnRegistrar,btnredirect;
    RequestQueue requestQueue;
    EditText edt1;

    int Contador;
    TextView edt2;
    String Rut;
    String Veri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        edt1 = findViewById(R.id.edtRut);



        btnRegistrar = findViewById(R.id.Registrarse);
        btnredirect = findViewById(R.id.btnRedireccionar);
        btnRegistrar.setOnClickListener(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        // Aquí puedes usar el token de registro del dispositivo
                    }
                });


    }




    public void Redireccion(View v){
        Intent i = new Intent(this, Activity_Fourth.class);
        startActivity(i);

    }
    public void irInicio(View c){
        Intent i = new Intent(this, Activity_Inicio.class);
        startActivity(i);

    }

    public void onClick(View view) {
        Rut = edt1.getText().toString().trim();

if(validarCaracteresNumericos(Rut)) {
    if (validarTexto(Rut)) {
        if (validarUltimoCaracter(Rut)) {
            if (Rut.length() > 0) {
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        URL1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("respuesta del server", response);
                                Log.d("respuesta del server", Rut);

                                if (response.trim().equals("El Rut ya esta registrado.")) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
                                    // Puedes agregar más parámetros según tus necesidades
                                    Intent i = new Intent(MainActivity.this, Activity_Inicio.class);
                                    // Rut ya registrado, proceder con la siguiente actividad
                                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    // Asignar el Bundle al Intent
                                    i.putExtras(bundle);
                                    startActivity(i);
                                    // Rut no registrado, mostrar mensaje de error
                                    Log.d("MainActivity", "Respuesta: " + response);
                                } else {
                                    Toast.makeText(MainActivity.this, "Usuario No Registrado", Toast.LENGTH_SHORT).show();
                                }
                            }


                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("MainActivity", "Respuesta: " + error);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("rut", Rut);
                        return params;
                    }
                };

                requestQueue.add(stringRequest);

            } else {
                Toast.makeText(MainActivity.this, "Debe Ingresar un Rut", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "El Ultimo Caracter debe ser K o numerico ", Toast.LENGTH_LONG).show();
        }
    } else {
        Toast.makeText(MainActivity.this, "Debe Ingresar un Rut Valido ", Toast.LENGTH_LONG).show();
    }
}else {
    Toast.makeText(MainActivity.this, "Debe Ingresar un Rut Valido ", Toast.LENGTH_LONG).show();
}
    }

    public static boolean validarTexto(String texto) {
        return !texto.contains("-");
    }
    public static boolean validarUltimoCaracter(String texto) {
        return texto.matches(".+[0-9kK]$");
    }
    public static boolean validarCaracteresNumericos(String cadena) {
        String subcadena = cadena.substring(0, cadena.length() - 1);
        return subcadena.matches("\\d+");
    }

    public void pasar2(View v){
        Intent i = new Intent(this, Activity_Second.class);
        startActivity(i);
        Bundle bundle = new Bundle();
        bundle.putInt("Contador", 0);
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        i.putExtras(bundle);

    }




}
