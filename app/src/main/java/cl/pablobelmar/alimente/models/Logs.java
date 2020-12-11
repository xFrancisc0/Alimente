package cl.pablobelmar.alimente.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Logs extends RealmObject {
    /*se le asigna la propiedad de PK al rut*/
    @PrimaryKey
    private long id;
    private String rut;
    private String accion;


    /*siempre se debe crear un constructor vació*/
    public Logs() {
    }


    public Logs(String rut, String accion) {
        this.rut = rut;
        this.accion = accion;
        /*se agrega el id de forma automática y no se pide como parametro del constructor*/
        this.id=getNextKey();
    }

    /*Métodos Get y Set de cada uno de los atributos*/
    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }


    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /*metodo para generar el id correlativo de forma automática*/
    public int getNextKey() {
        try {
            Realm realm = Realm.getDefaultInstance();
            /* se consulta por el id max actual guardado*/
            Number number = realm.where(Logs.class).max("id");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
