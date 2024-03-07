package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;






public class Activity_Inicio extends AppCompatActivity {

    RequestQueue requestQueue;
    ImageButton ib1,ib1_1,ib1_2,ib2,ib2_1,ib2_2,ib2_3,ib3,ib4;
    private boolean isExpanded = false;

    FloatingActionButton fab;
    TextView badge;
    private boolean isPressed = false;
    private boolean isPressed2 = false;
    int cantidadPendientes;
    String Rut;
    ViewPager2 viewPager;

    String serverUrl = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/TraerNovedades.php"; // URL de la página o script en el servidor que lista las imágenes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        fab = findViewById(R.id.fab);
        badge = findViewById(R.id.badge);

        ObtenerRut();

        requestQueue = Volley.newRequestQueue(this);

        ViewPager2 viewPager = findViewById(R.id.viewPager);


        fetchImagesFromServer(serverUrl);




        ib1 = findViewById(R.id.btnDulces);
        ib1_1= findViewById(R.id.ibtd1_1);
        ib1_1.setVisibility(View.INVISIBLE);
        ib1_2= findViewById(R.id.ibtd1_2);
        ib1_2.setVisibility(View.INVISIBLE);

        ib2 = findViewById(R.id.btnPasteles);
        ib2_1= findViewById(R.id.ibtp1_1);
        ib2_1.setVisibility(View.INVISIBLE);
        ib2_2= findViewById(R.id.ibtp1_2);
        ib2_2.setVisibility(View.INVISIBLE);
        ib2_3= findViewById(R.id.ibtp1_3);
        ib2_3.setVisibility(View.INVISIBLE);



        ib4 = findViewById(R.id.btnPedido);

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPressed = !isPressed;
                updateButtonState();
            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPressed2 = !isPressed2;
                updateButtonStatePasteles();
            }
        });



    }
    private void fetchImagesFromServer(String serverUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String responseBody = readResponse(connection.getInputStream());
                        List<String> imageUrls = parseImageUrls(responseBody);
                        showImages(imageUrls);
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private List<String> parseImageUrls(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            List<String> imageUrls = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                String imageUrl = jsonArray.getString(i);
                imageUrls.add(imageUrl);
            }

            return imageUrls;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private void showImages(List<String> imageUrls) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPager2 viewPager = findViewById(R.id.viewPager);
                ImageAdapter imageAdapter = new ImageAdapter(Activity_Inicio.this, imageUrls);
                viewPager.setAdapter(imageAdapter);

                int delayMillis = 3000; // Tiempo de espera entre cada desplazamiento en milisegundos
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItems = viewPager.getAdapter().getItemCount();
                        int nextItem = (currentItem + 1) % totalItems;
                        viewPager.setCurrentItem(nextItem);
                        handler.postDelayed(this, delayMillis);
                    }
                };
                handler.postDelayed(runnable, delayMillis);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Coloca aquí las líneas de código que deseas ejecutar al iniciar la actividad o al volver de otra
        traerPendientes(Rut);
        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public void ObtenerRut(){
        Bundle extras = getIntent().getExtras();
        Rut = extras.getString("rut");
    }
    private void updateButtonState() {
        if (isPressed) {
            ib1_1.setVisibility(View.VISIBLE);
            ib1_2.setVisibility(View.VISIBLE);

        } else {
            ib1_1.setVisibility(View.INVISIBLE);
            ib1_2.setVisibility(View.INVISIBLE);


        }
    }
    private void updateButtonStatePasteles() {
        if (isPressed2) {

            ib2_1.setVisibility(View.VISIBLE);
            ib2_2.setVisibility(View.VISIBLE);
            ib2_3.setVisibility(View.VISIBLE);
        } else {

            ib2_1.setVisibility(View.INVISIBLE);
            ib2_2.setVisibility(View.INVISIBLE);
            ib2_3.setVisibility(View.INVISIBLE);

        }
    }


    public void CerrarSesion(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void IrPan(View v){

        Intent intent = new Intent(this, Activity_Second.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);


    }
    public void Irpedido(View b){
        Intent intent = new Intent(this, Activity_Pedido.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    public void Alfajores(View a){
        Intent intent = new Intent(this, Activity_Alfajores.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);



    }


    public void traerPendientes(final String rut) {
        String URL = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/TraerPendientes.php";

        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Aquí manejas la respuesta del servidor
                        // response contiene el valor devuelto por la consulta SQL
                        Log.d("ESTE ES EL RESPONSE", response);
                        cantidadPendientes = Integer.parseInt(response.trim());
                        // Haz lo que necesites con el valor obtenido
                        badge.setText(String.valueOf(cantidadPendientes));
                        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Aquí manejas los errores de la solicitud
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Parámetros POST que se enviarán al archivo PHP
                Map<String, String> params = new HashMap<>();
                params.put("rut", rut);
                return params;
            }
        };

        requestQueue.add(request);
    }
    public void Vercarrito(View v){
        pasarRut();
    }
    public void pasarRut(){
        Intent intent = new Intent(this, Activity_Five.class);
        // Pasar los parámetros a través de un objeto Bundle
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        intent.putExtras(bundle);

        // Iniciar la nueva actividad
        startActivity(intent);
    }
    public void irHome(View z){
        Intent intent = new Intent(this, Activity_Inicio.class);
        startActivity(intent);
    }
}