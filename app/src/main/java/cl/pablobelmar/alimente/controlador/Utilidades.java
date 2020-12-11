package cl.pablobelmar.alimente.controlador;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;
import cl.pablobelmar.alimente.R;


public class Utilidades {


    public static void Vibrar(int tipo, Activity a){
        Vibrator vibrator = (Vibrator) a.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {//Si tiene vibrador
            if (tipo == 1) {
                long tiempo = 50; //en milisegundos
                vibrator.vibrate(tiempo);
            }
            if (tipo==2){
                long[] pattern = {400, //sleep
                        600, //vibrate
                        100,300,100,150,100,75};
                vibrator.vibrate(pattern, -1);
            }
        }
    }

    static public String formatearRut(String rut){
        int cont=0;
        String format;
        if(rut.length() == 0){
            return "";
        }else{
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            format = "-"+rut.substring(rut.length()-1);
            for(int i = rut.length()-2;i>=0;i--){
                format = rut.substring(i, i+1)+format;
                cont++;
                if(cont == 3 && i != 0){
                    format = "."+format;
                    cont = 0;
                }
            }
            return format;
        }
    }
    //********************************************************************************************
    static public boolean validarRut(String rut){
        int suma=0;
        String dvR,dvT;
        int[] serie = {2,3,4,5,6,7};
        rut = rut.replace(".", "");
        rut = rut.replace("-", "");
        dvR = rut.substring(rut.length()-1);
        for(int i = rut.length()-2;i>=0; i--){
            suma +=  Integer.valueOf(rut.substring(i, i+1))
                    *serie[(rut.length()-2-i)%6];
        }
        dvT=String.valueOf(11-suma%11);
        if(dvT.compareToIgnoreCase("10") == 0){
            dvT = "K";
        }

        if(dvT.compareToIgnoreCase(dvR) == 0){
            return true;
        } else {
            return false;
        }
    }
    //********************************************************************************************

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        assert connec != null;
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos entonces true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
    //********************************************************************************************
    public static String primeraMayuscula(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

/*****************************************************************************************************************************/



/*****************************************************************************************************************************/
    public static void showNotification(Context context, Intent intent, String title, String body) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)

                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.RED, 3000, 3000)
                .setTimeoutAfter(10000)
                .setWhen(System.currentTimeMillis())
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }



}
