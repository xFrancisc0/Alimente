package cl.pablobelmar.alimente;

import androidx.appcompat.app.AppCompatActivity;
import cl.pablobelmar.alimente.controlador.Splash;
import cl.pablobelmar.alimente.models.ProductoUsuario;
import cl.pablobelmar.alimente.views.PanelActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoProductoActivity extends AppCompatActivity {
    EditText editIdUsuario, editIdProducto, editFecha, editNombre_producto, editDescripcion_producto, editCant_azucar_producto, editCant_proteina_producto, editCant_grasas_producto, editRecomendacion_producto;

    private Button btnvolver;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_producto);

        Log.d("Llegue aqui2", "Hola mundo2");
        Bundle bundle = this.getIntent().getExtras();
        String id_usuario =bundle.getString("id_usuario");
        String id_producto =bundle.getString("id_producto");

        editIdUsuario = (EditText) findViewById(R.id.editIdUsuario);
        editIdProducto = (EditText) findViewById(R.id.editIdProducto);
        editFecha = (EditText) findViewById(R.id.editFecha);
        editNombre_producto = (EditText) findViewById(R.id.editNombre_producto);
        editDescripcion_producto = (EditText) findViewById(R.id.editDescripcion_producto);
        editCant_azucar_producto = (EditText) findViewById(R.id.editCant_azucar_producto);
        editCant_proteina_producto = (EditText) findViewById(R.id.editCant_proteina_producto);
        editCant_grasas_producto = (EditText) findViewById(R.id.editCant_grasas_producto);
        editRecomendacion_producto = (EditText) findViewById(R.id.editRecomendacion_producto);
        btnvolver = (Button) findViewById(R.id.btnVolver);


        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluador("panel");
            }
        });
        editIdUsuario.setText(id_usuario);
        editIdProducto.setText(id_producto);

        Obtener_historial(id_usuario,id_producto);
    }

    public void Obtener_historial(final String id_usuario, final String id_producto){


        /*alumnosAppV2: Se adjuntan los parametros al map que son los que solicita la API y se envian por post*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_usuario", id_usuario);
        params.put("id_producto", id_producto);

        String URL = "http://abascur.cl/android/android_5/GetHistorial";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");

                            if (status.equals("success")) {
                                Log.d("Mensaje: ", mensaje);
                                JSONArray mJsonArray = new JSONArray(mensaje);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);

                                editFecha.setText(mJsonObject.getString("fecha"));
                                editNombre_producto.setText(mJsonObject.getString("nombre_producto"));
                                editDescripcion_producto.setText(mJsonObject.getString("descripcion_producto"));
                                editCant_azucar_producto.setText(mJsonObject.getString("cant_azucar_producto"));
                                editCant_proteina_producto.setText(mJsonObject.getString("cant_proteina_producto"));
                                editCant_grasas_producto.setText(mJsonObject.getString("cant_grasas_producto"));
                                editRecomendacion_producto.setText(mJsonObject.getString("recomendacion_producto"));

                            }
                            else{
                                //Si fallo la insersion, se regresa al activity de donde se regreso,
                                //Es decir, a la actividad del listado de historiales
                                //Mandando por supuesto la id del usuario para futuros escaneos de codigo qr
                                Intent intent= new Intent(InfoProductoActivity.this, PanelActivity.class);
                                Bundle b = new Bundle();
                                b.putString("id_usuario", id_usuario);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
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
    /******************************************************************************************/
    private void evaluador(String lugarDondeLlevar) {
        Intent SplashIntent = new Intent(getApplicationContext(), Splash.class);
        SplashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle splash = new Bundle();
        splash.putString("nextActivity",lugarDondeLlevar);
        SplashIntent.putExtras(splash);

        startActivity(SplashIntent);
    }


}
