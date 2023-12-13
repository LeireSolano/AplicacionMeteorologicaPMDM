package com.example.aplicacionmeteorologicapmdm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView resultado;
    private TextView hora;
    private TextView condiciones;

    private TextView temperatura;
    private TextView sensacion;

    private ImageView imagenClima;


    private RequestQueue mQ;
    private static final String TAG = "WeatherApp";

    private static final String API_KEY = "HG5UCRUSBKGYS34URRNB5ZWUZ";
    private static final String API_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/Guadalajara%2C%20espa%C3%B1a/today?unitGroup=metric&include=current&key=HG5UCRUSBKGYS34URRNB5ZWUZ&contentType=json";

    // Inicializar TextToSpeech
    private TextToSpeech textToSpeech;
    private String condicionLarga; // ya que luego se usa en el TextToSpeech


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
        ImageView imagenClima = findViewById(R.id.gifImageView2);


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
                            JSONObject current = response.getJSONObject( "currentConditions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dias = jsonArray.getJSONObject(i);

                                String datetime = dias.getString("datetime");
                                double tempmax = dias.getDouble("tempmax");
                                double tempmin = dias.getDouble("tempmin");
                                String condicion = current.getString("icon");
                                String condicionLarga = current.getString("conditions");
                                String horaActual = current.getString("datetime");
                                double temperaturaActual = current.getDouble("temp");
                                double sensacionTermica = current.getDouble("feelslike");

                                if(condicion.contains("snow"))
                                    condicion="snow";

                                if(condicion.contains("thunder"))
                                    condicion="thunder";

                                if(condicion.contains("showers"))
                                    condicion="rain";

                                if(condicion.contains("cloudy"))
                                    condicion="cloudy";

                                if(condicion.contains("night"))
                                    condicion="night";


                                switch (condicion){
                                    case "snow":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.nevando);
                                        break;
                                    case "rain":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.llovizna);
                                        break;
                                    case "fog":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.niebla);
                                        break;
                                    case "wind":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.ventoso);
                                        break;
                                    case "cloudy":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.partly_cloudy);
                                        break;
                                    case "clear-day":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.dom);
                                        break;
                                    case "night":
                                        //poner la imagen correspondiente
                                        imagenClima.setImageResource(R.drawable.luna);
                                        break;

                                }

                                resultado.append("MAX:  "  + tempmax + "Cº           MIN: " + tempmin +"Cº");
                                sensacion.append("Sensación Térmica: "+sensacionTermica + " Cº" );
                                condiciones.append(condicionLarga);
                                temperatura.append(temperaturaActual + " Cº");
                                fecha.append(datetime);
                                hora.append(horaActual);
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

    /*
    CREO EL TEXTtoSPEECH
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // TextToSpeech se inicializó correctamente
            int result = textToSpeech.setLanguage(new Locale("es", "ES")); // Establecer el idioma español

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Idioma español no soportado");
            } else {
                textToSpeech.speak("El clima es " + condicionLarga, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } else {
            Log.e(TAG, "ERROR");
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
