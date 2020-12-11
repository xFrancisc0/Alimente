package cl.pablobelmar.alimente.views;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText rutUsuario, clave;
    private Button btnIngreso;
    private TextView btnRegistro;
    private Realm mRealm;
    private Boolean bd=false;
    //VARIABLES GLOBALES
    //--------------------------------------------------------------------------
    //Variable contrasena obtenida de la BD, su fin es ser comparada en el LOGIN.
    String Respuesta_Consulta_BD;
    private SharedPreferences credencial;
    private SharedPreferences idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpRealmConfig();
        mRealm= Realm.getDefaultInstance();
        /********************************************************************/
        rutUsuario = (EditText)findViewById(R.id.editRut);
        clave    = (EditText)findViewById(R.id.editContra);
        btnRegistro = (TextView)findViewById(R.id.btnRegistro);
        btnIngreso = (Button)findViewById(R.id.btnIngreso);

        /*******************************************************************/

        credencial=getSharedPreferences("credeciales", Context.MODE_PRIVATE);
        idUser = getSharedPreferences("idUser", Context.MODE_PRIVATE);



        /***********************************************************************************/

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluador("registro");
            }
        });

        /***********************************************************************************/

        btnIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //**********************************************************************************
                if(isFormularioValido()){

                    String rutp = rutUsuario.getText().toString();
                    Toast.makeText(getApplicationContext(), "paso la validacion",Toast.LENGTH_LONG).show();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("rut", rutp);
                    Toast.makeText(getApplicationContext(), "arriba de la api",Toast.LENGTH_LONG).show();
                    String URL = "http://abascur.cl/android/android_5/GetUsuario";

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "declaro el request",Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Log.d("JSONPost", response.toString());
                                    try {
                                        String status = response.getString("status");
                                        Toast.makeText(getApplicationContext(), "entamos al try",Toast.LENGTH_LONG).show();

                                        if (status.equals("success")){
                                              Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_LONG).show();
                                            JSONArray data= response.getJSONArray("mensaje");
                                                Toast.makeText(getApplicationContext(), "2",Toast.LENGTH_LONG).show();

                                            procesandodatos(data);
                                            Toast.makeText(getApplicationContext(), "3",Toast.LENGTH_LONG).show();
                                        }else{
                                            String mensaje = response.getString("mensaje");
                                            Toast.makeText(getApplicationContext(), mensaje,Toast.LENGTH_LONG).show();

                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "algo salio mal",Toast.LENGTH_LONG).show();

                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                             VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(), "paso la error",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(jsonReque);
                    Toast.makeText(getApplicationContext(), "paso algo",Toast.LENGTH_LONG).show();
                }


                //*****************************************************************************************
            }
        });


    }
    //********************************************************************************************

/*************************************************************************************/
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

    /******************************************************************************************/
    private void evaluador(String lugarDondeLlevar) {
        Intent SplashIntent = new Intent(getApplicationContext(), Splash.class);
        SplashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle splash = new Bundle();
        splash.putString("nextActivity",lugarDondeLlevar);
        SplashIntent.putExtras(splash);

        startActivity(SplashIntent);
    }

    /**********************************************************************/



    //********************************************************************************************
    public boolean isFormularioValido() {

        boolean r = false;
        if (TextUtils.isEmpty(rutUsuario.getText().toString().trim())) {
            rutUsuario.setError("Por favor Ingresa un RUT ");

        } else if (!Utilidades.validarRut(rutUsuario.getText().toString().trim())) {
            rutUsuario.setError("Por favor Ingresa un RUT Valido ");

        }
        else if(TextUtils.isEmpty(clave.getText().toString().trim())){
            clave.setError("Por favor Ingresa tu clave ");
        }else {
            r = true;
        }

        return r;
    }
    //********************************************************************************************
    public void login(String rutp, String clavep, String id){

        if(clave.getText().toString().equals(clavep)){
            //Toast.makeText(getApplicationContext(),"Ingresando.",Toast.LENGTH_LONG).show();

            SharedPreferences.Editor sesion = credencial.edit();
            SharedPreferences.Editor idp = idUser.edit();

            sesion.putString("rut" , rutp);
            sesion.putString("clave", clavep);
            sesion.commit();

            idp.putString("id" , id);
            idp.commit();
            evaluador("panel");
        }
        else{
            Toast.makeText(getApplicationContext(),"Error, la contraseña es incorrecta.",Toast.LENGTH_LONG).show();
        }
    }
    //********************************************************************************************
    public void procesandodatos(JSONArray mensaje ){

        Toast.makeText(getApplicationContext(), "estamos en procesandodatos",Toast.LENGTH_LONG).show();
        try {
            Toast.makeText(getApplicationContext(), "4",Toast.LENGTH_LONG).show();
            JSONObject jsonObject = mensaje.getJSONObject(0);

            String rutuser  = jsonObject.getString("rut");
            String claveuser = jsonObject.getString("contrasena");
            String iduser = jsonObject.getString("id");
            // Toast.makeText(getApplicationContext(), "5",Toast.LENGTH_LONG).show();
            if(Utilidades.validarRut(rutuser)){
                login(rutuser, claveuser, iduser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Algo salio mal D:<.",Toast.LENGTH_LONG).show();
        }
    }





}
