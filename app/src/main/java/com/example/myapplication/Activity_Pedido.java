package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_Pedido extends AppCompatActivity {


    String Rut,Description,Fecha;
    private static final String URL1 = "https://slate-gray-cargoes.000webhostapp.com/Panaderia/SavePedido.php";
    private ImageButton btnCalendar;
    private Calendar calendar;
    TextView edt1;
    EditText editText;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);


        Bundle extras = getIntent().getExtras();
        Rut = extras.getString("rut");

        edt1=findViewById(R.id.txvFecha);
        btnCalendar = findViewById(R.id.btnCalendar);
        calendar = Calendar.getInstance();
        editText = findViewById(R.id.edtmulti);
        requestQueue = Volley.newRequestQueue(this);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



    }



    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Aquí puedes hacer algo con la fecha seleccionada por el usuario
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        edt1.setText(selectedDate);


                    }
                }, year, month, day);

        datePickerDialog.show();


    }
    public void CerrarSesion(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void Pedir(View a){
        Fecha= edt1.getText().toString();
        Description=editText.getText().toString();
        createPedido(Description,Fecha,Rut);
    }
    private void createPedido(final String Descrip,final String Date,final String rut) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Activity_Pedido.this, "Gracias Por tu Pedido Nos Comunicaremos Contigo", Toast.LENGTH_LONG).show();

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
                params.put("Descripcion",Descrip);
                params.put("FechaEntrega",Date);
                params.put("usuario_rut",rut);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}