package cl.pablobelmar.alimente.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cl.pablobelmar.alimente.R;
import cl.pablobelmar.alimente.controlador.Splash;
import cl.pablobelmar.alimente.controlador.Utilidades;
import cl.pablobelmar.alimente.models.Logs;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRut, editNombre, editApellido, editCorreo, editContra;
    private Button btnRegistro;
    private Realm mRealm;
     Toolbar toolbar;
    //VARIABLES GLOBALES
    //--------------------------------------------------------------------------
    //Variable contrasena obtenida de la BD, su fin es ser comparada en el LOGIN.
    int id_max;
    String Respuesta_Consulta_BD;
    private SharedPreferences prefs;

    //--------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //********************************************************

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setLogo(R.drawable.lapiz);
        toolbar.setTitle("  Registro Usuario");
        setSupportActionBar(toolbar);


        //********************************************************

        setUpRealmConfig();
        mRealm= Realm.getDefaultInstance();

        editRut = (EditText) findViewById(R.id.editRut);
        editNombre = (EditText) findViewById(R.id.editNombre);
        editApellido = (EditText) findViewById(R.id.editApellido);
        editCorreo = (EditText) findViewById(R.id.editCorreo);
        editContra = (EditText) findViewById(R.id.editContra);
        btnRegistro = (Button) findViewById(R.id.btnRegistro);

        prefs = getSharedPreferences("Preference", Context.MODE_PRIVATE);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFormularioValido()) {
                    String rut = editRut.getText().toString();
                    String nombre = editNombre.getText().toString();
                    String apellido = editApellido.getText().toString();
                    String correo = editCorreo.getText().toString();
                    String contra = editContra.getText().toString();

                    //String fecha = obtener_fecha_actual();



                    //Intento registrar estos datos en la DB.
                    //Si se registran los datos de forma exitosa, Respuesta_Consulta_BD = Ingreso_Exitoso
                    //De lo contrario, Respuesta_Consulta_BD = Ingreso_Fallido

                    //El primer paso para registrar los datos en la DB es encontrar la id maxima de la DB
                    //y sumarla +1. Despues de esto esta id maxima la añado al json que sera insertado.
                    Obtener_id_maxima_usuario(rut, nombre, apellido, correo, contra);
                }
            }
        });

    }



    private void setUpRealmConfig() {

        // Se inicializa realm
        Realm.init(this.getApplicationContext());

        // Configuración por defecto en realm
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);

    }



    public boolean isFormularioValido() {

        boolean r = false;
        if (TextUtils.isEmpty(editRut.getText().toString().trim())) {
            editRut.setError("Por favor Ingresa un RUT ");
        } else if (!Utilidades.validarRut(editRut.getText().toString().trim())) {
            editRut.setError("Por favor Ingresa un RUT Valido ");
        } else if (TextUtils.isEmpty(editNombre.getText().toString().trim())) {
            editContra.setError("Por favor Ingresa un nombre ");
        } else if (TextUtils.isEmpty(editContra.getText().toString().trim())) {
            editContra.setError("Por favor Ingresa una contraseña ");
        } else {
            r = true;
        }

        return r;
    }


    public void Obtener_id_maxima_usuario(final String rut, final String nombre,
                                          final String apellido, final String correo, final String contra){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "http://abascur.cl/android/android_5/GetIDMaximaUsuario";

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                JSONObject mensaje = response;
                                JSONArray arr = mensaje.getJSONArray("mensaje");
                                for (int i = 0; i < arr.length(); i++) {

                                    if(arr.getJSONObject(i).getString("MAX_ID")=="null"){
                                        Log.d("max_id es", arr.getJSONObject(i).getString("MAX_ID"));
                                        id_max=0;
                                        Insertar_o_actualizar_usuario(rut, nombre, apellido, correo, contra);
                                    }else{
                                        Log.d("max_id es", arr.getJSONObject(i).getString("MAX_ID"));
                                        id_max = Integer.parseInt(arr.getJSONObject(i).getString("MAX_ID"));
                                        Insertar_o_actualizar_usuario(rut, nombre, apellido, correo, contra);
                                    }
                                }

                            }else{
                                Toast.makeText(getApplicationContext(), "Hubo un fallo al intentar encontrar, reintente.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Hubo un fallo al intentar encontrar, reintente.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Hubo un fallo al intentar ingresar, reintente.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonReque);
    }


    /*alumnosAppV2: metodo para registrar o reemplazar un alumno de realm a la BD remota*/
    public void Insertar_o_actualizar_usuario(final String rut, String nombre,
                                              String apellido, String correo, String contra){

        /*alumnosAppV2: Se adjuntan los parametros al map que son los que solicita la API y se envian por post*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(id_max+1));
        params.put("rut", String.valueOf(rut));
        params.put("nombre", nombre);
        params.put("apellido", apellido);
        params.put("correo", correo);
        params.put("contrasena", contra);
        params.put("rango", "1");

        String URL = "http://abascur.cl/android/android_5/InsertOrUpdateUsuario";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");
                            if (status.equals("success")) {
                                Respuesta_Consulta_BD = "Ingreso_Exitoso";
                                Respuesta_de_registro(rut);
                            }else if(status.equals("rutyaexiste")){
                                Respuesta_Consulta_BD = "Rut ya existe";
                                Respuesta_de_registro(rut);
                            }
                            else{
                                Respuesta_Consulta_BD = "Ingreso_Fallido";
                                Respuesta_de_registro(rut);
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


    /*En esta funcion se analiza el rut y la contraseña, con respecto a la respuesta obtenida de la petición a la API*/
    /*Esta respuesta es Respuesta_Consulta_BD*/
    public void Respuesta_de_registro(final String rut){


        if(Respuesta_Consulta_BD.equals("Ingreso_Fallido")){
            Toast.makeText(getApplicationContext(), "Hubo un error al ingresar en la BD, intente nuevamente con otros datos.", Toast.LENGTH_SHORT).show();
        }

        else if(Respuesta_Consulta_BD.equals("Rut ya existe")){
            Toast.makeText(getApplicationContext(), "Este RUT ya esta registrado en nuestra BD. Error al ingresar.", Toast.LENGTH_SHORT).show();
        }

        else{
            String accion = "Registro";
            Logs logs= new Logs(rut,accion);
            mRealm.beginTransaction();
            mRealm.insertOrUpdate(logs);
            mRealm.commitTransaction();

            //Despues de esto puedo proceder a irme a la actividad 4
            evaluador("login");
        }
    }


    public String obtener_fecha_actual(){
        LocalDateTime myDateObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myDateObj = LocalDateTime.now();
        }
        System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        }

        String fecha_actual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fecha_actual = myDateObj.format(myFormatObj);
        }

        return fecha_actual;
    }

    public void GuardarPreferencias(final String rut){
        //Se guarda con sharedpreference
        SharedPreferences.Editor editor = prefs.edit();
        //Setear rut en RUT_Usuario en sharedPreference
        editor.putString("RUT_Usuario", rut);
        //Guardar cambios
        editor.apply();
    }
    private void evaluador(String lugarDondeLlevar) {
        Intent SplashIntent = new Intent(getApplicationContext(), Splash.class);
        SplashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle splash = new Bundle();
        splash.putString("nextActivity",lugarDondeLlevar);
        SplashIntent.putExtras(splash);

        startActivity(SplashIntent);
    }

}

