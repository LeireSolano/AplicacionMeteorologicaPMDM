package com.example.aplicacionmeteorologicapmdm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView resultado;
    private TextView hora;
    private TextView condiciones;

    private TextView temperatura;
    private TextView sensacion;



    private RequestQueue mQ;
    private static final String TAG = "WeatherApp";
    private static final String API_KEY = "HG5UCRUSBKGYS34URRNB5ZWUZ";
    private static final String API_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guadalajara%2C%20espa%C3%B1a/today?unitGroup=metric&include=current&key=HG5UCRUSBKGYS34URRNB5ZWUZ&contentType=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView resultado = findViewById(R.id.resultado);
        TextView hora = findViewById(R.id.hora);
        TextView condiciones = findViewById(R.id.condiciones);
        TextView temperatura = findViewById(R.id.temperatura);
        TextView fecha = findViewById(R.id.fecha);
        TextView sensacion = findViewById(R.id.sensacion);



        // Crear una cola de solicitudes
        RequestQueue queue = Volley.newRequestQueue(this);

        // Crear una solicitud GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta JSON
                        Log.d(TAG, "Respuesta: " + response.toString());
                        // Aquí puedes realizar las operaciones necesarias con los datos meteorológicos

                        try {
                            JSONArray jsonArray = response.getJSONArray(  "days");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dias = jsonArray.getJSONObject(i);

                                String datetime = dias.getString("datetime");
                                double tempmax = dias.getDouble("tempmax");
                                double tempmin = dias.getDouble("tempmin");
                                String condicion=dias.getString("conditions");

                               // String horaActual = currentconditions.getString("datetime");
                                double temperaturaActual = dias.getDouble("temp");
                                double sensacionTermica = dias.getDouble("feelslike");


                                switch (condicion){
                                    case "PartPartially cloudy":
                                        //poner la imagen correspondiente
                                }

                                resultado.append("MAX:  "  + tempmax + "Cº           MIN: " + tempmin +"Cº");
                                sensacion.append("Sensación Térmica: "+sensacionTermica + " Cº" );
                                condiciones.append(condicion);
                                temperatura.append(temperaturaActual + " Cº");
                                fecha.append(datetime);
                            }

                            /*JSONArray jsonCondicion = response.getJSONArray(  "conditions");

                            for(int x=0;x<jsonCondicion.length();x++){

                                JSONObject objct = jsonArray.getJSONObject(x);

                                String condicion=objct.getString("conditions");

                                resultado.append(", "+String.valueOf(condicion));
                            }*/


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Log.e(TAG, "Error en la solicitud: " + error.toString());
                    }
                });

        // Agregar la solicitud a la cola
        queue.add(jsonObjectRequest);
    }
}