package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Five extends AppCompatActivity {

    RequestQueue requestQueue;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/ListarOrdenes.php";
    private static final String URL2     = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/ListarOrdenes.php";
    ImageButton ib1,ib1_1,ib1_2,ib2,ib2_1,ib2_2,ib2_3,ib3,ib4;
    private boolean isPressed = false;
    private boolean isPressed2 = false;
    private boolean isExpanded = false;
    String id,Rut;
    String idorder;

    double totalPreciof = 0.0; // Variable para almacenar el total de precios
    double totalPrecio = 0.0;
    List<View> orderViews = new ArrayList<>();
    List<Double> preciosTotales = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            // Obtener los valores de los parámetros
            id = extras.getString("iduser");
            Rut = extras.getString("rut");
        }
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
                Intent intent = new Intent(Activity_Five.this, Activity_Inicio.class);
                Bundle bundle = new Bundle();
                bundle.putString("rut", Rut);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Five.this, Activity_Pedido.class);
                Bundle bundle = new Bundle();
                bundle.putString("rut", Rut);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        requestQueue = Volley.newRequestQueue(this);


// Obtén una referencia al LinearLayout
        ScrollView scrollView = findViewById(R.id.scrollView); // Obtener la referencia del ScrollView
        LinearLayout linearContainer = new LinearLayout(Activity_Five.this);

        linearContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        linearContainer.setOrientation(LinearLayout.VERTICAL); // Asegurar orientación vertical
        Typeface typeface = ResourcesCompat.getFont(Activity_Five.this, R.font.description);
        TextView precioFinalTextView = new TextView(Activity_Five.this);

        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                URL1 + "?usuario_rut="+Rut,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {



                        if(response.length() == 0 ){

                            Toast.makeText(Activity_Five.this , "No tienes Ninguna Orden :(", Toast.LENGTH_SHORT).show();
                            EnviarDevuelta();
                        }
                        try {


                            // Crea un contenedor adicional para los orderLayout
                            LinearLayout orderContainer = new LinearLayout(Activity_Five.this);
                            orderContainer.setOrientation(LinearLayout.VERTICAL);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject orderObject = response.getJSONObject(i);

                                final String idorder = orderObject.getString("id");
                                String precio = orderObject.getString("Precio");
                                String cantidad = orderObject.getString("Cantidad");
                                String pan_id = orderObject.getString("NombrePan");
                                String alfajor_id = orderObject.getString("NombreAlfajor");
                                String img = orderObject.getString("Imagen");

                                // Calcula el precio total sumando los precios de cada orden
                                totalPrecio = Integer.parseInt(cantidad) * Double.valueOf(precio);
                                totalPreciof = totalPreciof + totalPrecio;
                                preciosTotales.add(totalPrecio);
                                // Crea una vista LinearLayout para contener los elementos de la orden
                                LinearLayout orderLayout = new LinearLayout(Activity_Five.this);
                                orderLayout.setOrientation(LinearLayout.HORIZONTAL);
                                orderLayout.setPadding(0, 0, 0, 16); // Espacio inferior entre cada orden

                                // Crea una vista ImageView para mostrar la foto
                                ImageView imageView = new ImageView(Activity_Five.this);
                                // Aquí puedes establecer la imagen según el ID de la orden

                                TraerImg(img,imageView);



                                // Ajusta el tamaño de la imagen a 120x120 píxeles
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(320, 320);
                                layoutParams.setMargins(0, 0, 0, 0); // Margen izquierdo de 50 píxeles
                                imageView.setLayoutParams(layoutParams);


                                // Crea un espacio entre la imagen y el TextView
                                Space space1 = new Space(Activity_Five.this);
                                LinearLayout.LayoutParams spaceParams1 = new LinearLayout.LayoutParams(
                                        20, // Ancho de 50 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space1.setLayoutParams(spaceParams1);

                                // Crea  boton Eliminar
                                Button eliminarButton = new Button(Activity_Five.this);
                                eliminarButton.setText("Eliminar");
                                eliminarButton.setBackgroundColor(Color.parseColor("#1C3562"));
                                eliminarButton.setTextColor(Color.WHITE);
                                eliminarButton.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {

                                        int index = orderContainer.indexOfChild(orderLayout);
                                        orderLayout.setVisibility(View.GONE);

                                        orderContainer.requestLayout();

                                        String idOrdenEliminar = idorder;

                                        double precioTotalEliminar = preciosTotales.get(index);
                                        totalPreciof -= precioTotalEliminar;
                                        eliminarFuncion(idOrdenEliminar);
                                        precioFinalTextView.setText("Precio final: " + totalPreciof);
                                    }
                                });
                                eliminarButton.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));

                                // Crea un espacio entre el TextView y el botón Eliminar
                                Space space2 = new Space(Activity_Five.this);
                                LinearLayout.LayoutParams spaceParams2 = new LinearLayout.LayoutParams(
                                        80, // Ancho de 50 píxeles
                                        LinearLayout.LayoutParams.WRAP_CONTENT // Altura ajustable según el contenido
                                );
                                space2.setLayoutParams(spaceParams2);

                                precioFinalTextView.setText("Precio final: " + totalPreciof);
                                precioFinalTextView.setTextColor(Color.BLACK);
                                precioFinalTextView.setTypeface(typeface);
                                precioFinalTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);



                                // Crea una vista TextView para mostrar los datos de la orden
                                Typeface typeface = ResourcesCompat.getFont(Activity_Five.this, R.font.description);
                                TextView textView = new TextView(Activity_Five.this);
                                textView.setText("ID Orden: "+idorder+"\n"+"Precio: " + precio + "\n" + "Cantidad: " + cantidad + "\n" + "Pan ID: " + pan_id + "\n" + "Total: " + totalPrecio);
                                textView.setTextColor(Color.BLACK);
                                textView.setTypeface(typeface);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                                // Agrega los elementos al LinearLayout de la orden

                                orderLayout.addView(imageView);
                                orderLayout.addView(space1);
                                orderLayout.addView(textView);
                                orderLayout.addView(space2);
                                orderLayout.addView(eliminarButton);


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

                            // Agrega el botón Ordenar al final del layout con margen superior y centrado
                            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            buttonParams.setMargins(0, 16, 0, 0); // Margen superior de 16 píxeles, ajusta según sea necesario
                            buttonParams.gravity = Gravity.CENTER_HORIZONTAL; // Centra el botón horizontalmente
                            Button ordenarButton = new Button(Activity_Five.this);
                            ordenarButton.setText("Ordenar");
                            ordenarButton.setBackgroundColor(Color.parseColor("#1C3562"));
                            ordenarButton.setTextColor(Color.WHITE);
                            ordenarButton.setLayoutParams(buttonParams);
                            ordenarButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Lógica para el botón Ordenar
                                    // Implementa la funcionalidad según sea necesario

                                    // Crear un cuadro de diálogo
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Five.this);
                                    builder.setTitle("Gracias :)");
                                    builder.setMessage("Estas Seguro Que Deseas Ordenar ?");

// Agregar un botón al cuadro de diálogo
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Acciones a realizar cuando se hace clic en el botón "Aceptar"
                                            Intent intent = new Intent(Activity_Five.this, Activity_Inicio.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("ID", id);
                                            bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
                                            // Puedes agregar más parámetros según tus necesidades

                                            // Asignar el Bundle al Intent
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            ordenarFuncion();
                                            Enviar();
                                        }
                                    });

// Opcionalmente, agregar un botón de "Cancelar"
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Acciones a realizar cuando se hace clic en el botón "Cancelar"
                                        }
                                    });

// Mostrar el cuadro de diálogo
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            });
                            linearContainer.addView(ordenarButton);

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


    public void onBackPressed() {
        Intent intent = new Intent(this, Activity_Second.class);
        // Pasar los parámetros a través de un objeto Bundle
        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        bundle.putString("rut", Rut);// Aquí debes utilizar el nombre correcto del parámetro
        // Puedes agregar más parámetros según tus necesidades

        // Asignar el Bundle al Intent
        intent.putExtras(bundle);

        // Iniciar la nueva actividad
        startActivity(intent);
    }
        public void ordenarFuncion() {
            // Implementa la lógica para ordenar los productos

            String urlCambiarEstado = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/ModificarOrden.php";

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    urlCambiarEstado,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


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
                    params.put("rut",Rut);

                    return params;
                }
            };

            requestQueue.add(stringRequest);



        }
    public void Enviar(){

        String urltoken = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/TraerTokenAdmin.php";

// Realizar la solicitud HTTP GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urltoken, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener el token del JSON de respuesta
                            String token = response.getString("token");

                            String title="Nuevo Pedido ";
                            String message="Tienes Un nuevo Pedido Revisa La APP";

                            FCMsend.pushNotification(
                                    Activity_Five.this,token,title,message);

                            // Aquí puedes guardar el token en una variable o hacer lo que necesites con él
                            // Por ejemplo, asignarlo a una variable en tu clase o actividad
                            // String miToken = token;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud
                    }
                });

// Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest);



    }


        public void eliminarFuncion(String order) {
            // Implementa la lógica para eliminar la orden en la posición orderIndex
            String urlEliminar = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/EliminarOrder.php";

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    urlEliminar,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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
                    params.put("id",order);

                    return params;
                }
            };

            requestQueue.add(stringRequest);

        }

        public void EnviarDevuelta(){
            Intent i = new Intent(this, Activity_Third.class);
            startActivity(i);
        }
    public void CerrarSesion(View v){
        Intent intent = new Intent(this, MainActivity.class);
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
    public void irHome(View z){
        Intent intent = new Intent(this, Activity_Inicio.class);
        startActivity(intent);
    }
    public void IrPan(View v){

        Intent intent = new Intent(this, Activity_Second.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);


    }
    public void Irpedido(View a){
        Intent intent = new Intent(this, Activity_Pedido.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    public void Alfajores(View v){
        Intent intent = new Intent(this, Activity_Alfajores.class);
        Bundle bundle = new Bundle();
        bundle.putString("rut", Rut);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}