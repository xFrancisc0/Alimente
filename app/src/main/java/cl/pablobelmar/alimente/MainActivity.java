package cl.pablobelmar.alimente;

import androidx.appcompat.app.AppCompatActivity;
import cl.pablobelmar.alimente.controlador.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // inicio de la aplicacion, solo debe aparecer cuando el usuairo no este logeado

    private Button btnlogin;
    private TextView btnTextRegistro;
    private SharedPreferences credencial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        credencial=getSharedPreferences("credeciales", Context.MODE_PRIVATE);
        String validador= credencial.getString("rut",null);
        if(validador != null){
            evaluador("panel");
        }
      //  credencial=getSharedPreferences("credeciales", Context.MODE_PRIVATE);
      //  String validador= credencial.getString("rut",null);
      //  if(validador != null){
         //   evaluador("panel");
      //  }

        this.btnlogin = findViewById(R.id.btnIngreso);
        this.btnTextRegistro = findViewById(R.id.registrateaqui);


        this.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluador("login");
            }
        });

        this.btnTextRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluador("registro");
            }
        });

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
