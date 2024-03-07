package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Second extends AppCompatActivity {

    RequestQueue requestQueue;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/Listar.php";
    private static final String URL2 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/SaveOrden.php";
    ImageButton ib1,ib1_1,ib1_2,ib2,ib2_1,ib2_2,ib2_3,ib3,ib4;
    private boolean isPressed = false;
    private boolean isPressed2 = false;
    private boolean isExpanded = false;
    FloatingActionButton fab;
    TextView badge;

    String Rut;

    int ID,cantidadPendientes ;
    List<View> orderViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Bundle extras = getIntent().getExtras();
        Rut = extras.getString("rut");

        requestQueue = Volley.newRequestQueue(this);

        fab = findViewById(R.id.fab);
        badge = findViewById(R.id.badge);


        traerPendientes(Rut);


        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);
        Log.d("Etiqueta", String.valueOf(cantidadPendientes));



        fab = findViewById(R.id.fab);
        badge = findViewById(R.id.badge);




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


        ib3 =findViewById(R.id.btnHome);
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
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Second.this, Activity_Inicio.class);
                Bundle bundle = new Bundle();
                bundle.putString("rut", Rut);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Second.this, Activity_Pedido.class);
                Bundle bundle = new Bundle();
                bundle.putString("rut", Rut);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
// Obtén una referencia al LinearLayout
        ScrollView scrollView = findViewById(R.id.scrollView2); // Obtener la referencia del ScrollView
        LinearLayout linearContainer = new LinearLayout(Activity_Second.this);

        linearContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        linearContainer.setOrientation(LinearLayout.VERTICAL); // Asegurar orientación vertical
        Typeface typeface = ResourcesCompat.getFont(Activity_Second.this, R.font.description);
        TextView precioFinalTextView = new TextView(Activity_Second.this);

        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URL1,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            // Crea un contenedor adicional para los orderLayout
                            LinearLayout orderContainer = new LinearLayout(Activity_Second.this);
                            orderContainer.setOrientation(LinearLayout.VERTICAL);


                            for (int i = 0; i < response.length(); i++) {
                                JSONObject orderObject = response.getJSONObject(i);

                                final String Pan_id = orderObject.getString("id");
                                String nombre = orderObject.getString("Nombre");
                                String precio = orderObject.getString("Precio");
                                String imagen = orderObject.getString("img");
                                String Cantidad = orderObject.getString("cantidad");

                                LinearLayout textButtonLayout = new LinearLayout(Activity_Second.this);
                                textButtonLayout.setOrientation(LinearLayout.VERTICAL);
                                textButtonLayout.setGravity(Gravity.CENTER_HORIZONTAL);


                                LinearLayout orderLayout = new LinearLayout(Activity_Second.this);
                                orderLayout.setOrientation(LinearLayout.HORIZONTAL);
                                orderLayout.setPadding(0, 0, 0, 16); // Espacio inferior entre cada orden

                                // Crea una vista ImageView para mostrar la foto
                                ImageView imageView = new ImageView(Activity_Second.this);
                                // Aquí puedes establecer la imagen según el ID de la orden
                                    TraerImg(imagen,imageView);

                                // Ajusta el tamaño de la imagen a 120x120 píxeles
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(320, 320);
                                layoutParams.setMargins(0, 0, 0, 0); // Margen izquierdo de 50 píxeles
                                imageView.setLayoutParams(layoutParams);

                                // Crea un espacio entre la imagen y el TextView
                                Space space1 = new Space(Activity_Second.this);
                                LinearLayout.LayoutParams spaceParams1 = new LinearLayout.LayoutParams(
                                        20, // Ancho de 50 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space1.setLayoutParams(spaceParams1);

                                // Crea  boton Añadir
                                Button VerButton = new Button(Activity_Second.this);
                                VerButton.setText("Añadir");
                                VerButton.setBackgroundColor(Color.parseColor("#1C3562"));
                                VerButton.setTextColor(Color.WHITE);
                                VerButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cantidadPendientes++;
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String fechaActual = dateFormat.format(calendar.getTime());
                                        createUser(Double.valueOf(precio),fechaActual,1,Double.valueOf(precio),Rut,Integer.parseInt(Pan_id),1,imagen);
                                        badge.setText(String.valueOf(cantidadPendientes));


                                    }
                                });


                                VerButton.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));


                                Space space3 = new Space(Activity_Second.this);
                                LinearLayout.LayoutParams spaceParams3 = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, // Ancho ajustado al contenedor
                                        16 // Altura de 16 píxeles, ajusta según sea necesario
                                );
                                space3.setLayoutParams(spaceParams3);

                                // Crea un espacio entre el TextView y el botón Eliminar
                                Space space2 = new Space(Activity_Second.this);
                                LinearLayout.LayoutParams spaceParams2 = new LinearLayout.LayoutParams(
                                        80, // Ancho de 80 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space2.setLayoutParams(spaceParams2);


                                // Crea una vista TextView para mostrar los datos de la orden
                                Typeface typeface = ResourcesCompat.getFont(Activity_Second.this, R.font.description);
                                TextView textView = new TextView(Activity_Second.this);
                                textView.setText(nombre+"\n"+"Cantidad: "+Cantidad+"\n"+"Precio: "+precio);
                                textView.setTextColor(Color.BLACK);
                                textView.setTypeface(typeface);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


                                // Agrega los elementos al LinearLayout de la orden
                                orderLayout.addView(imageView);
                                orderLayout.addView(space1);
                                orderLayout.addView(textView);
                                orderLayout.addView(space2);
                                orderLayout.addView(VerButton);
                                orderLayout.addView(space3);


                                // Agrega el LinearLayout de la orden al contenedor adicional
                                orderViews.add(orderLayout);
                                orderContainer.addView(orderLayout);
                            }

                            // Agrega el contenedor adicional al linearContainer
                            linearContainer.addView(orderContainer);

                            // Agrega el TextView para mostrar el precio final

                            priceParams.setMargins(0, 16, 0, 0); // Margen superior de 16 píxeles, ajusta según sea necesario
                            priceParams.gravity = Gravity.CENTER_HORIZONTAL; // Centra el TextView horizontalmente
                            precioFinalTextView.setLayoutParams(priceParams);
                            linearContainer.addView(precioFinalTextView);



                            // Cambia el color de fondo del LinearLayout

                            scrollView.addView(linearContainer); // Agrega el contenedor al ScrollView

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });
        requestQueue.add(request);








    }
    private void createUser(final Double Precio, final String Fecha, final int Cantidad, final Double PrecioFinal, final String usuario_rut, final int bread_id, final int estadoorden_id,final String img) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Activity_Second.this , "Añadido", Toast.LENGTH_SHORT).show();
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
                params.put("pan_id",String.valueOf(bread_id));
                params.put("alfajor_id","6");
                params.put("dulce_id","7");
                params.put("estadoorden_id",String.valueOf(estadoorden_id));
                params.put("imagen",img);
                return params;

            }
        };
        requestQueue.add(stringRequest);

    }

    public void Irpedido(View a){
        Intent intent = new Intent(this, Activity_Pedido.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);

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
    protected void onResume() {
        super.onResume();

        // Coloca aquí las líneas de código que deseas ejecutar al iniciar la actividad o al volver de otra
        traerPendientes(Rut);
        badge.setText(String.valueOf(cantidadPendientes));
        badge.setVisibility(cantidadPendientes > 0 ? View.VISIBLE : View.INVISIBLE);
    }

    public void Vercarrito(View v){
       pasarRut();
    }

    public void Alfajores(View v){
        Intent intent = new Intent(this, Activity_Alfajores.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);

    }



    public void Pasarid() {
        // Crear un intent para abrir la nueva actividad
        Intent intent = new Intent(this, Activity_Third.class);
        // Pasar los parámetros a través de un objeto Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("ID", ID);
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        intent.putExtras(bundle);

        // Iniciar la nueva actividad
        startActivity(intent);
    }
    public void pasarRut(){
        Intent intent = new Intent(this, Activity_Five.class);
        // Pasar los parámetros a través de un objeto Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("ID", ID);
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        intent.putExtras(bundle);

        // Iniciar la nueva actividad
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
public void CerrarSesion(View v){
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
}

public void TraerImg(String direccion ,ImageView imageView){

    String url=direccion;

    ImageRequest imageRequest = new ImageRequest(url,
            new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    // Aquí puedes hacer algo con la imagen, como mostrarla en un ImageView

                    imageView.setImageBitmap(response);

                }
            }, 0, 0, null,
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Manejo de errores
                }
            });
    requestQueue.add(imageRequest);
}
    public void IrPan(View v){

        Intent intent = new Intent(this, Activity_Second.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);


    }
}

