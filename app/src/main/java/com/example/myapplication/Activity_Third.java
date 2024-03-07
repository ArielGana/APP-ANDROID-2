package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Activity_Third extends AppCompatActivity {


    TextView tx1, tx2,tx3;
    Button btn1;
    FloatingActionButton fab;
    TextView badge;
    String Rut;

    EditText edt1;
    int Contador;
    double Pfinal;
    int id,cantidadPendientes;


    RequestQueue requestQueue;

    String precio,descripcion,nombre;



    ImageView img;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/Listarid.php";
    private static final String URL2 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/SaveOrden.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        badge = findViewById(R.id.badge);
        requestQueue = Volley.newRequestQueue(this);



        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);

        requestQueue = Volley.newRequestQueue(this);
        tx1 = findViewById(R.id.Pan);
        tx3 = findViewById(R.id.Descripcion);
        tx2 = findViewById(R.id.Precio);
        edt1=findViewById(R.id.edtcanti);
        //img=findViewById(R.id.Foto);
        fab = findViewById(R.id.fab);



        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        Bundle extras = getIntent().getExtras();
        traerPendientes(Rut);
        if (extras != null) {
            // Obtener los valores de los parámetros
            id = extras.getInt("ID");
            Rut = extras.getString("rut");
            Contador = extras.getInt("Contador");



        }



            Log.d("ESTE ES EL ID",String.valueOf(id));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL1 + "?id=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       String respuesta = response.toString();
                        Log.d("TAG",respuesta );
                        try {
                             nombre = response.getString("Nombre");
                             descripcion = response.getString("Descripcion");
                             precio = response.getString("Precio");



                            tx1.setText(nombre);
                            tx3.setText(descripcion);
                            tx2.setText("Precio: "+precio);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("el error232",error.getMessage());
                    }
                });

        requestQueue.add(request);

    }


    public void irCarro(View v){
        Intent intent = new Intent(this, Activity_Five.class);



        // Pasar los parámetros a través de un objeto Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("Contador", Contador);
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        intent.putExtras(bundle);
        startActivity(intent);


    }



    protected void onResume() {
        super.onResume();

        // Coloca aquí las líneas de código que deseas ejecutar al iniciar la actividad o al volver de otra
        traerPendientes(Rut);
        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);
    }

        public void anadirCarro(View v){

        cantidadPendientes++;
        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);

        Toast.makeText(Activity_Third.this , "Producto Añadido", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaActual = dateFormat.format(calendar.getTime());

        String Cantidad = edt1.getText().toString();
        Pfinal=Integer.parseInt(Cantidad)*Double.valueOf(precio);
        createUser(Double.valueOf(precio),fechaActual,Integer.parseInt(Cantidad),Double.valueOf(Pfinal),Rut,id,1);
        cantidadPendientes++;



    }


    private void createUser(final Double Precio, final String Fecha, final int Cantidad, final Double PrecioFinal, final String usuario_rut, final int pan_id, final int estadoorden_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Activity_Third.this , "Correcto "+nombre, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String >params= new HashMap<>();
                params.put("Precio", String.valueOf(Precio));
                params.put("Fecha",String.valueOf(Fecha));
                params.put("Cantidad",String.valueOf(Cantidad));
                params.put("PrecioFinal",String.valueOf(PrecioFinal));
                params.put("usuario_rut",String.valueOf(usuario_rut));
                params.put("pan_id",String.valueOf(pan_id));
                params.put("alfajor_id","6");
                params.put("dulce_id","7");
                params.put("estadoorden_id",String.valueOf(estadoorden_id));
                return params;

            }
        };
        requestQueue.add(stringRequest);

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
    public void CerrarSesion(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void irHome(View z){
        Intent intent = new Intent(this, Activity_Inicio.class);
        startActivity(intent);
    }

}
