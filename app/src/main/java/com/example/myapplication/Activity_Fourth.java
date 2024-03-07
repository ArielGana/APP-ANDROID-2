package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Activity_Fourth extends AppCompatActivity implements View.OnClickListener {


EditText etName,etApellido,etRut,etTelefono,edtveri;
Button btnIngresar;

RequestQueue requestQueue;

    String token;



private static final String URL2="https://slate-gray-cargoes.000webhostapp.com/Panaderia/Save.php";
private static final String URL1="http://192.168.1.129/Panaderia/Save.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);


        requestQueue= Volley.newRequestQueue(this);

        init();

        btnIngresar.setOnClickListener(this);


    }


    public void init(){
        etName= findViewById(R.id.Nombre);
        etApellido= findViewById(R.id.Apellido);
        etRut= findViewById(R.id.Rut);
        etTelefono= findViewById(R.id.Telefono);
        btnIngresar = findViewById(R.id.Ingresar);


    }


   public void onClick(View view) {
        this.btnIngresar=btnIngresar;
        String Nombre = etName.getText().toString();
        String Apellido = etApellido.getText().toString();
        String Rut = etRut.getText().toString();
        String Telefono = etTelefono.getText().toString();




        if(Nombre.isEmpty()){
            Toast.makeText(Activity_Fourth.this , "Debes Ingresar Tu Nombre ", Toast.LENGTH_SHORT).show();
        }if(Apellido.isEmpty()){
           Toast.makeText(Activity_Fourth.this , "Debes Ingresar Tu Apellido", Toast.LENGTH_SHORT).show();
       }if(Telefono.isEmpty()){
           Toast.makeText(Activity_Fourth.this , "Debes Ingresar Tu Telefono ", Toast.LENGTH_SHORT).show();
       }if (Rut.isEmpty()) {
               Toast.makeText(Activity_Fourth.this, "Debes ingresar tu Rut", Toast.LENGTH_SHORT).show();
           } else {
               // Obtiene el último carácter del Rut
               char ultimoCaracter = Rut.charAt(Rut.length() - 1);
               // Verifica si el último carácter es un número o una "k"
               if (Character.isDigit(ultimoCaracter) || ultimoCaracter == 'k') {
                   Telefono="+569"+Telefono;
                   obtenerTokenFirebaseMessaging(Nombre,Apellido,Rut,Telefono);
               } else {
                   // El último carácter no es válido
                   Toast.makeText(Activity_Fourth.this, "El último carácter del Rut no es válido", Toast.LENGTH_SHORT).show();
               }
           }
       }



    private void createUser(final String nombre,final String apellido,final String rut,final String telefono,final String token) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error del usuario", error.toString());
                    }
                }
    ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String >params= new HashMap<>();
                params.put("Nombre",nombre);
                params.put("Apellido",apellido);
                params.put("Rut",rut);
                params.put("Telefono",telefono);
                params.put("token",token);

                return params;
            }
        };
       requestQueue.add(stringRequest);

    }

    private void obtenerTokenFirebaseMessaging(final String nombre,final String apellido,final String rut,final String telefono) {
        // Obtener el token de Firebase Messaging
        Intent i = new Intent(this, MainActivity.class);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String token = task.getResult();
                    System.out.println("Token: " + token);


                    createUser(nombre,apellido,rut,telefono,token);

                    Toast.makeText(Activity_Fourth.this , "Usuario Registrado Correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
        });
    }







}