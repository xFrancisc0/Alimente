package cl.pablobelmar.alimente.controlador;

import androidx.appcompat.app.AppCompatActivity;
import cl.pablobelmar.alimente.R;
import cl.pablobelmar.alimente.views.LoginActivity;
import cl.pablobelmar.alimente.views.PanelActivity;
import cl.pablobelmar.alimente.views.RegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    String redirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Bundle bundle = this.getIntent().getExtras();
        redirector = bundle.getString("nextActivity");

        switch (redirector) {
            case "login":

                //Toast.makeText(getApplicationContext(),"loginp.",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }
                },2000);
                //  Toast.makeText(getApplicationContext(),"loginfin.",Toast.LENGTH_LONG).show();
                break;
            case "panel":

                //  Toast.makeText(getApplicationContext(),"panelp",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent listIntent = new Intent(getApplicationContext(), PanelActivity.class);
                        listIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(listIntent);
                    }
                },2000);
                //  Toast.makeText(getApplicationContext(),"pnelfin.",Toast.LENGTH_LONG).show();
                break;

            case "registro":

                //Toast.makeText(getApplicationContext(),"loginp.",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }
                },2000);
                //  Toast.makeText(getApplicationContext(),"loginfin.",Toast.LENGTH_LONG).show();
                break;

        }
    }
}
