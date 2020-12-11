package cl.pablobelmar.alimente;

import androidx.appcompat.app.AppCompatActivity;
import cl.pablobelmar.alimente.controlador.Splash;
import cl.pablobelmar.alimente.models.ProductoUsuario;
import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InfoProActivity extends AppCompatActivity {

    EditText editIdUsuario, editIdProducto, editFecha, editNombre_producto, editDescripcion_producto, editCant_azucar_producto, editCant_proteina_producto, editCant_grasas_producto, editRecomendacion_producto;
    private Button btnvolver;
    private String ideditar;
    private int idedit;
    private Realm realm;
    private ProductoUsuario nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pro);


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


        //********************************************************
        Bundle bundle = this.getIntent().getExtras();
        ideditar = bundle.getString("id");

        Toast.makeText(getApplicationContext(), ideditar, Toast.LENGTH_LONG).show();
        //********************************************************


        //********************************************************
        //configuramos la bd
        realm = Realm.getDefaultInstance();
        //setUpRealmConfig();
        /*Se carga la instancia de realm*/
        //*****************************************************************************

        idedit= Integer.valueOf(ideditar);

        nota= realm.where(ProductoUsuario.class).equalTo("idProducto", idedit ).findFirst();

        //****************************************************************************

        Obtener_historial(nota);
    }


    public void Obtener_historial(ProductoUsuario p){

        editFecha.setText(p.getFechaHistorialProducto());
        editNombre_producto.setText(p.getNombreProducto());
        editDescripcion_producto.setText(p.getDescripcionProducto());
        editCant_azucar_producto.setText(p.getCantidadAzucarProducto());
        editCant_proteina_producto.setText(p.getCantidadProteinaProducto());
        editCant_grasas_producto.setText(p.getCantidadGrasaProducto());
        editRecomendacion_producto.setText(p.getRecomendacionProdcuto());
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
