package com.example.aplicacionmeteorologicapmdm;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WeatherApp";
    private static final String API_KEY = "HG5UCRUSBKGYS34URRNB5ZWUZ";
    private static final String API_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guadalajara%2C%20espa%C3%B1a/today?unitGroup=metric&include=current&key=HG5UCRUSBKGYS34URRNB5ZWUZ&contentType=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    }
                },
                new Response.ErrorListener() {
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