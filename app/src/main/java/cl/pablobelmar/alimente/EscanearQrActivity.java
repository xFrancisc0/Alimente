package cl.pablobelmar.alimente;

import androidx.appcompat.app.AppCompatActivity;
import cl.pablobelmar.alimente.views.PanelActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EscanearQrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    //AÑADIDO PARA ESCANEAR QR
    //=============================================================================
    Button btnEscanearQR;


    //=============================================================================
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escanear_qr);


        btnEscanearQR = (Button)findViewById(R.id.btnEscanearQR);
        //Hago que el boton se autoclickee para activar el evento Escanear(View v) mediante el onclick
        //Definido en el layout
        btnEscanearQR.performClick();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    //=============================================================================
    public void Escanear(View v){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    public void handleResult(Result result){
        //Si se consigue escanear un codigo QR pasa lo siguiente
        String id_producto_completo = result.getText();
        Bundle bundle = this.getIntent().getExtras();
        String[] id_producto_fragmentos = id_producto_completo.trim().split("\\|");
        String[] id_producto_subfragmentos= id_producto_fragmentos[1].trim().split("\\=");
        String id_producto = id_producto_subfragmentos[1];
        String id_usuario =bundle.getString("id_usuario");

        //Obtengo el id del usuario que hizo el escaneo y ademas de eso la id del producto escaneado
        Log.d("id_producto", id_producto);
        Log.d("id_usuario", id_usuario);


        //Se procedera a crear un registro de este escaneo en la tabla historial
        //==========================================================================================
        Insertar_o_actualizar_usuario(id_usuario,id_producto);

    }

    public void Insertar_o_actualizar_usuario(final String id_usuario, final String id_producto){


        /*alumnosAppV2: Se adjuntan los parametros al map que son los que solicita la API y se envian por post*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_usuario", id_usuario);
        params.put("id_producto", id_producto);

        String URL = "http://abascur.cl/android/android_5/InsertOrUpdateHistorial";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");

                            //Si la insersion del nuevo historial fue un exito se procedera a
                            //enviar los datos de id_usuario e id_producto al activity
                            //donde se muestra la informacion de un producto seleccionado
                            // (La actividad que tienen que hacer ustedes)
                            if (status.equals("success")) {
                                Log.d("Llegue aqui", "Hola mundo");
                                Intent intent= new Intent(EscanearQrActivity.this, InfoProductoActivity.class);
                                Bundle b = new Bundle();
                                b.putString("id_usuario", id_usuario);
                                b.putString("id_producto", id_producto);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                            else{
                                Log.d("Llegue aqui3", "Hola mundo3");
                                //Si fallo la insersion, se regresa al activity de donde se regreso,
                                //Es decir, a la actividad del listado de historiales
                                //Mandando por supuesto la id del usuario para futuros escaneos de codigo qr
                                Intent intent= new Intent(EscanearQrActivity.this, PanelActivity.class);
                                Bundle b = new Bundle();
                                b.putString("id_usuario", id_usuario);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Log.d("Llegue aqui4", "Hola mundo4");
                            Toast.makeText(getApplicationContext(), "Hubo un fallo al intentar registrar, reintente.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Hubo un fallo al intentar registrar, reintente.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonReque);
    }
}

