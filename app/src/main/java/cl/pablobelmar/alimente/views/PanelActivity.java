package cl.pablobelmar.alimente.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cl.pablobelmar.alimente.EscanearQrActivity;
import cl.pablobelmar.alimente.InfoProActivity;
import cl.pablobelmar.alimente.InfoProductoActivity;
import cl.pablobelmar.alimente.R;
import cl.pablobelmar.alimente.adapters.ListAdapters;
import cl.pablobelmar.alimente.controlador.Splash;
import cl.pablobelmar.alimente.controlador.Utilidades;
import cl.pablobelmar.alimente.models.ProductoUsuario;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private ListAdapters adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<ProductoUsuario> listprod = new   ArrayList<ProductoUsuario>();
    private FrameLayout frameLayout;
    private SharedPreferences credencial;
    private SharedPreferences idUser;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        /*Se carga la instancia de realm*/
        setUpRealmConfig();
        mRealm = Realm.getDefaultInstance();
        credencial = getSharedPreferences("credeciales", Context.MODE_PRIVATE);
        idUser = getSharedPreferences("idUser", Context.MODE_PRIVATE);
        /********************toolbar***************************/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setLogo(R.drawable.home);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
/***************************fin toolbar**************************************/


/**********************listados d**********************************/

        /******************************************************************************************/
        frameLayout = findViewById(R.id.frameLayout);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        /******************************************************************************************/
        /*tareaAppV2: se referencia el SwipeRefreshLayout*/
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                /*tareaAppV2:solo se puede actualizar el listado si existe conexión a internet*/
                if (Utilidades.verificaConexion(getApplication())) {
                    //Toast.makeText(getApplicationContext(), usuarioapp, Toast.LENGTH_SHORT).show();
                    //SyncbdRemote();---->
                    updateListHistorial();
                } else {
                    Toast.makeText(getApplicationContext(), "Esta acción necesita conexión a internet, comprueba tu conexión.", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "En el caso que la nota exista se reemplazara", Toast.LENGTH_LONG).show();
                /*Método para lanzar el dialog*/
              //  DialogAgregarNota();-----> QR


                //Para escanear un nuevo producto debo enviar la id del usuario
                //Para poder despues del escaneo crear un nuevo historial
                //Envio la id de usuario = 1 para este ejempo
                //Cambiar el "1" or id_usuario obtenida con una consulta
                //Volley al CRUD de la tabla Usuario en la API.
                // Utilizando como variable de entrada el RUT.
                //Obtener informacion de un usuario


                //-------------------------------------
                //
                //Method: POST

                //url: http://abascur.cl/android/android_5/GetUsuario

                //Data: {"rut": "18686188-4"}
                //=====================

                //Obtener_id_de_un_rut();
                SendEscanearActivity(idUser.getString("id", null));
            }
        });

        /*Método para actualizar el listado de alumnos*/
        updateListHistorial();


        /*Para cargar el listado de asignaturas a través del adapatador*/
        /*El adaptador solicita el listado y el evento click*/
        adapter = new ListAdapters(listprod, new ListAdapters.OnItemClickListener() {
            /*Los eventos que se generan son los mismos OnClickListener que se agregaron en AlumnoListAdapters*/
            @Override
            public void OnItemClick(ProductoUsuario asig, int position) {
               // Toast.makeText(getApplicationContext(), "La " + asig.getIdProducto() + " es:" + position + " Codigo: " + asig.getNombreProducto(), Toast.LENGTH_SHORT).show();
                /*Se llama a la función statica vibrar de la clase Utilidades*/
                //DialogVerNota(asig.getIdProducto());-------------------------------------------->>>>>>>>>>>

                Utilidades.Vibrar(1, PanelActivity.this);

            }

            @Override
            public void OnDeleteClick(final ProductoUsuario asig, final int position) {

                /*Tipo de dialog por defecto similar a un Alert en Js*/
                /*Solicita un contexto*/
                AlertDialog alertDialog = new AlertDialog.Builder(PanelActivity.this).create();
                /*Se agrega un titulo*/
                alertDialog.setTitle("Alerta");
                /*Se agrega un Mensaje*/
                alertDialog.setMessage("¿Esta seguro que quiere eliminar a " + asig.getNombreProducto() + "?");
                /*Se agregan los botones con sus respectivos eventos*/
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
                );
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNota(asig);
                                adapter.removeItem(position);
                                dialog.dismiss();
                            }
                        });
                /*se lanza el dialog*/
                alertDialog.show();

            }

            @Override
            public void OnEditClick(final ProductoUsuario asig, final int position) {

                Toast.makeText(getApplicationContext(), asig.getIdProducto() + "", Toast.LENGTH_SHORT).show();

                //EditarNota(asig);---------------------->>>>>>>>>>>>>>>>>>>>
                verproducto(asig);

                Utilidades.Vibrar(1, PanelActivity.this);


            }
        });

        /*Se referencia el ReciclerView del layout*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        /* se agrega los valores por defecto para mostrar el reciclerview tipo de layout y la animación*/
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*Ahora se finalmente se le agrega el adaptador que se creo arriba al recyclerview que es el que contiene el listado*/
        recyclerView.setAdapter(adapter);


        /*************************************fin listado*****************************************************/


    }

    //***********************************funciones del listado **********************************************/
  /*  public void DialogAgregarNota() {
        // consigo el rut del usuario para mandarlo al fragment
        /*Para poder configurar nuestro propio fragment y mostrarlo necesitamos de supportFragment*
        FragmentManager fm = getSupportFragmentManager();
        /*En el caso que se quiera solo lanzar una modal y no pasar información desde la actividad al fragment*
        //AgregarAlumnoFragment dialogFragment = new AgregarAlumnoFragment()
        /*Se llama al fragment que ocuparemos como dialog, se le pasa el adaptador y el listado de alumnos*
        /*los metodos se deben agregar en el fragment *
        AgregarNotaFragment dialogFragment = new AgregarNotaFragment().setAdapter(adapter).setArrayList(listNotas);

        /*Se genera el evento para cuando se cierre la modal en el caso que sea necesario*
        dialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //  Toast.makeText(getApplicationContext(), "Se cerro la modal", Toast.LENGTH_LONG).show();

                /*ABASCUR: faltaba validar la conexión a internet*
                if (Utilidades.verificaConexion(getApplication())) {
                    SyncbdRemote();
                }
            }
        });
        /*se muestra el dialog*
        dialogFragment.show(fm, "Sample Fragment");
    }*/

    public void updateListHistorial() {
        /*se agrega a un nuevo arraylist, el listado de notas a través de realm*/
        /*Para hacer consultas no es necesario abrir una transacción*/
        listprod = new ArrayList(mRealm.where(ProductoUsuario.class).findAll());
        /*Tarea2AppV2:Si el listado es vacio se solicita automaticamente la info a la bd*/
        if (listprod.size() == 0) {
            if (Utilidades.verificaConexion(getApplication())) {
                String usuarioapp = idUser.getString("id", null);
                GetAllList(usuarioapp);
            }
        }
    }

    /*****///////////////////////////////////////////////////////////////////////////////////////////////////////*/

    //sdkfjsdjflksdjflkjsdf  GetAllList
    public void GetAllList(String iduser) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_usuario", iduser);
        String URL = "http://abascur.cl/android/android_5/GetAllHistorialesDeUsuario";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("JSONPost", response.toString());
                        try {
                            String status = response.getString("status");

                            if (!response.getString("status").equals("NoData")) {
                                JSONArray mensaje = response.getJSONArray("mensaje");
                                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                                /*tarea2AppV2: si es que la respuesta es succes guardamos el listado en realm*/
                                if (status.equals("success")) {
                                    //Toast.makeText(getApplicationContext(),"dentro de estatus" + status, Toast.LENGTH_SHORT).show();
                                    addListToRealm(mensaje);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Error no se puedo realizar la consulta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Sin Data", Toast.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
            }
        });
        queue.add(jsonReque);
    }


    /*****///////////////////////////////////////////////////////////////////////////////////////////////////////*/
    /*Tarea2AppV2: metodo para actualizar el listado de realm por la BD remota*/
    public void addListToRealm(JSONArray mensaje) {
        /*Tarea2AppV2: se limpia el listado y se borra de realm*/
        listprod.clear();
        mRealm.beginTransaction();
        mRealm.delete(ProductoUsuario.class);
        mRealm.commitTransaction();

        try {
            /*<tarea2AppV2: se recorre el JSON y se agregan las nueva notas*/
            if (mensaje.length() > 0) {

                for (int i = 0; i < mensaje.length(); i++) {
                    JSONObject jsonObject = mensaje.getJSONObject(i);

                    String id = jsonObject.getString("id_producto");
                    String nombre = jsonObject.getString("nombre_producto");
                    String descripcion = jsonObject.getString("descripcion_producto");
                    String fecha = jsonObject.getString("fecha");
                    String cap = jsonObject.getString("cant_azucar_producto");
                    String cpp = jsonObject.getString("cant_proteina_producto");
                    String cgp = jsonObject.getString("cant_grasas_producto");
                    String rp = jsonObject.getString("recomendacion_producto");

                    listprod.add(new ProductoUsuario(id, nombre, descripcion, fecha, cap, cpp, cgp,rp));

                }

            }

            /*tarea2AppV2: se carga la lista*/
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(listprod);
            mRealm.commitTransaction();

            /*tarea2AppV2: se actualiza la lista del adaptador para ver los cambios reflejados*/
            adapter.updateList(listprod);

            /*alumnosAppV2: se termina el evento del SwipeRefreshLayout*/
            mSwipeRefreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /*TareaAppV2: metodo para verificar en realm si existe un elemento no enviado a la BD remota*/
   /* private void SyncbdRemote() {

        RealmResults<ProductoUsuario> ListadoNoSync = mRealm.where(ProductoUsuario.class).equalTo("sendBd", false).findAll();
        // Toast.makeText(getActivity(), "Listado a sincronizar : "+ListadoNoSync,Toast.LENGTH_SHORT).show();

        if (ListadoNoSync.size() > 0) {
            Utilidades.showNotification(getApplication(), new Intent(), "Alerta", "Enviando información a la BD");

            for (int i = 0; i < ListadoNoSync.size(); i++) {

                String rut = credencial.getString("rut", null);
                ;
                int id = ListadoNoSync.get(i).getIdProducto();
                String nombre = ListadoNoSync.get(i).getNombreProducto();
                String descripcion = ListadoNoSync.get(i).getDescripcionProducto();
                String fecha = ListadoNoSync.get(i).getFechaProducto();
                private String cantidadAzucarProducto = ListadoNoSync.get(i).getCantidadAzucarProducto();
                private String cantidadProteinaProducto = ListadoNoSync.get(i).getCantidadProteinaProducto();
                private String recomendacionProdcuto = ListadoNoSync.get(i).getRecomendacionProdcuto();

                //Toast.makeText(getActivity(), "Se Sincronizaron los datos del : " + fecha, Toast.LENGTH_SHORT).show();

                //InsertOrUpdateBD(rut, String.valueOf(id), nombre,descripcion, fecha);
            }
            /*ABASCUR: llamamos a GetALlList despues de haber enviado los elementos que estén con sendBd:false*
            GetAllList(usuarioapp);
        } else {
            GetAllList(usuarioapp);
        }

    }*/

    /*alumnosAppV2: metodo para enviar un alumno de realm a la BD remota*/
   /* public void InsertOrUpdateBD(String rut, final String id, String nombre, String descripcion, String fecha, String fup) {

        Toast.makeText(getApplicationContext(), "la fecha es:" + fecha, Toast.LENGTH_SHORT).show();
        /*alumnosAppV2: Se adjuntan los parametros al map que son los que solicita la API y se envian por post*
        Map<String, String> params = new HashMap<String, String>();
        params.put("rutUsuario", rut);
        params.put("idNota", id);
        params.put("nombreNota", nombre);
        params.put("descripcionNota", descripcion);
        params.put("fechaHoraCreacion", fecha);
        params.put("fechaHoraUpdate", fup);
        params.put("idAcceso", idaccesoAPI);

        // Toast.makeText(getApplicationContext(), params.toString() , Toast.LENGTH_SHORT).show();

        String URL = "http://abascur.cl/android/misnotasapp/InsertOrUpdateNota";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("JSONPost", response.toString());
                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");
                            if (status.equals("success")) {
                                // Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                                /*alumnosAppV2: Se actualiza en realm el estado*
                                UpdateEnviado(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonReque);
    }*/

    /*Tarea2AppV2: metodo para actualizar el estado de envio de un nota*/
   /* private void UpdateEnviado(String id) {
        int id_int = Integer.parseInt(id);
        mRealm.beginTransaction();
        NotaUsuario nota = mRealm.where(NotaUsuario.class).equalTo("idNota", id_int).findFirst();
        assert nota != null;
        nota.setSendBd(true);
        mRealm.commitTransaction();
    }*/

    //metodo para ver en un modal el contenido de la nota
  /*  public void DialogVerNota(int idposi) {
        /*Para poder configurar nuestro propio fragment y mostrarlo necesitamos de supportFragment*
        FragmentManager fm = getSupportFragmentManager();
        /*En el caso que se quiera solo lanzar una modal y no pasar información desde la actividad al fragment*/
        //AgregarAlumnoFragment dialogFragment = new AgregarAlumnoFragment()
        /*Se llama al fragment que ocuparemos como dialog, se le pasa el adaptador y el listado de alumnos*/
        /*los metodos se deben agregar en el fragment *
        VerNotaFragment dialogFragment = new VerNotaFragment().setAdapter(adapter).setArrayList(listNotas).setPosicion(idposi);

        /*se muestra el dialog*
        dialogFragment.show(fm, "Sample Fragment");


    }*/

    //metodo para editar la nota
   /* public void EditarNota(NotaUsuario notita) {

        Intent SplashIntent = new Intent(getApplicationContext(), EditarActivity.class);
        SplashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle splash = new Bundle();
        splash.putString("id", Integer.toString(notita.getIdNota()));
        SplashIntent.putExtras(splash);

        startActivity(SplashIntent);
    }*/


    /*se debe llamar desde el click de eliminar y se debe verificar la conexión de internet*/
    /*tarea2AppV2:solo se puede eliminar si existe conexión a internet*/
    public void deleteNota(ProductoUsuario item) {

        if (Utilidades.verificaConexion(getApplicationContext())) {
            /*tarea2AppV2:solo se envia la petición ara eliminar la nota de la BD*/
            DeleteBD(idUser.getString("id", null), String.valueOf(item.getIdProducto()));

            /*se abre una transacción*/
            mRealm.beginTransaction();
            /*se consulta por el id que tenga como rut el mismo rut que se paso por parametro,ese se elimina*/
            mRealm.where(ProductoUsuario.class).equalTo("idProducto", item.getIdProducto()).findFirst().deleteFromRealm();
            mRealm.commitTransaction();

            /*Se genera un mensaje a traves de una snackbar*/
            Snackbar snackbar = Snackbar.make(frameLayout, "Eliminado de forma correcta", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        } else {
            Toast.makeText(getApplicationContext(), "Para eliminar una tarea se necesita Internet", Toast.LENGTH_LONG).show();
        }
    }


    /*alumnosAppV2: metodo para eliminar un alumno de la BD remota*/
    public void DeleteBD(final String iduser, String idpro) {

        /*alumnosAppV2: se adjunto el rut a un objeto tipo Map y se envia a la url por POST*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("id_usuario", iduser);
        params.put("id_producto", idpro);


        String URL = "http://abascur.cl/android/android_5/DeleteHistorial";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonReque = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Log.d("JSONPost", response.toString());
                        /*tarea2AppV2: Aqui se encuentra la respuesta de la API*/
                        try {
                            String status = response.getString("status");
                            String mensaje = response.getString("mensaje");
                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "mensaje", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonReque);
    }


    //*****************************fin funciones listado*********************************************/


    /**********************cerrar sesion**********************/

    public void eliminarseccion() {

        SharedPreferences.Editor eliminar = credencial.edit();
        SharedPreferences.Editor eliminarId = idUser.edit();
        eliminar.putString("rut", null);
        eliminar.putString("clave", null);
        eliminar.commit();

        eliminarId.putString("id", null);
        eliminarId.commit();
    }

    //********************************************************************************************
    public void salir() {

        eliminarseccion();
        String splash = "userListToLogin";
        Intent intent = new Intent(PanelActivity.this, Splash.class);

        Bundle isplash = new Bundle();
        isplash.putString("nextActivity", "login");
        intent.putExtras(isplash);

        startActivity(intent);

    }

    /*****************************************************************************/
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Handling Action Bar button click
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button
            case R.id.option:

                showPopup(this.toolbar.findViewById(R.id.option));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.action_main);
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.salir:
                Toast.makeText(getApplicationContext(), "salir", Toast.LENGTH_LONG).show();
                salir();
                return true;

            default:
                return false;
        }

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



    //Aca hice un cambio, añadi esta funcion para obtener la id del usuario necesaria para escanear QR
    //AÑADIDO PARA ESCANEAR QR
    //=============================================================================
    public void Obtener_id_de_un_rut() {
        Bundle bundle = this.getIntent().getExtras();
        String rut =bundle.getString("rut");
        Map<String, String> params = new HashMap<String, String>();
        params.put("rut", rut);

        String URL = "http://abascur.cl/android/android_5/GetUsuario";
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

                                //Ya que obtuve la id del usuario, añado este dato al momento de escanear
                                SendEscanearActivity(mJsonObject.getString("id"));
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
    //=============================================================================


    //Aca hice un cambio, añadi esta funcion para obtener la id del usuario necesaria para escanear QR
    //AÑADIDO PARA ESCANEAR QR
    //=============================================================================
    private void SendEscanearActivity(String id_usuario) {
        //Antes de volver a la actividad uno debo registrar en logs que cerre secion.
        Intent intent= new Intent(PanelActivity.this, EscanearQrActivity.class);
        Bundle b = new Bundle();
        b.putString("id_usuario", id_usuario);
        intent.putExtras(b);
        startActivity(intent);
    }
    //=============================================================================


    //metodo para editar la nota
    public void verproducto(ProductoUsuario notita){

        Intent SplashIntent = new Intent(getApplicationContext(), InfoProActivity.class);
        SplashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle splash = new Bundle();
        splash.putString("id", Integer.toString(notita.getIdProducto()));
        SplashIntent.putExtras(splash);

        startActivity(SplashIntent);
    }
}

