package cl.pablobelmar.alimente.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductoUsuario  extends RealmObject {

    @PrimaryKey
    private int idProducto;

    private int numeroLista;
    private String nombreProducto;
    private String descripcionProducto;
    private String cantidadAzucarProducto;
    private String cantidadProteinaProducto;

    public String getCantidadGrasaProducto() {
        return cantidadGrasaProducto;
    }

    public void setCantidadGrasaProducto(String cantidadGrasaProducto) {
        this.cantidadGrasaProducto = cantidadGrasaProducto;
    }

    public String getFechaHistorialProducto() {
        return fechaHistorialProducto;
    }

    public void setFechaHistorialProducto(String fechaHistorialProducto) {
        this.fechaHistorialProducto = fechaHistorialProducto;
    }

    private String cantidadGrasaProducto;
    private String recomendacionProdcuto;
    private String fechaHistorialProducto;
    /*nuevo atributo para saber si es que fue enviado o no a la bd remota*/
    private boolean sendBd;

//
    public ProductoUsuario(){
        this.numeroLista = numero("numeroLista");
    }

    public ProductoUsuario(String nombreProducto, String descripcionProducto,
                           String cantidadAzucarProducto, String cantidadProteinaProducto,
                           String recomendacionProdcuto, String fechaProducto, boolean senbd){


        this.nombreProducto =nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.cantidadAzucarProducto = cantidadAzucarProducto;
        this.cantidadProteinaProducto = cantidadProteinaProducto;
        this.recomendacionProdcuto = recomendacionProdcuto;
        this.fechaHistorialProducto = fechaProducto;
        this.sendBd = senbd;
        this.idProducto = numero("idProducto");
        this.numeroLista = numero ("numeroLista");
    }
    //ProductoUsuario(id, nombre, descripcion, fecha, cap, cpp, cgp,rp)
    public ProductoUsuario(String id, String nombre, String descripcion, String fecha,
                           String cap, String cpp, String cgp, String rp) {

        this.idProducto= Integer.parseInt(id);
        this.nombreProducto = nombre;
        this.descripcionProducto = descripcion;
        this.fechaHistorialProducto = fecha;
        this.cantidadAzucarProducto = cap;
        this.cantidadProteinaProducto = cpp;
        this.cantidadGrasaProducto = cgp;
        this.recomendacionProdcuto = rp;
        this.numeroLista = numero("numeroLista");
    }


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getNumeroLista() {
        return numeroLista;
    }

    public void setNumeroLista(int numeroLista) {
        this.numeroLista = numeroLista;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getCantidadAzucarProducto() {
        return cantidadAzucarProducto;
    }

    public void setCantidadAzucarProducto(String cantidadAzucarProducto) {
        this.cantidadAzucarProducto = cantidadAzucarProducto;
    }

    public String getCantidadProteinaProducto() {
        return cantidadProteinaProducto;
    }

    public void setCantidadProteinaProducto(String cantidadProteinaProducto) {
        this.cantidadProteinaProducto = cantidadProteinaProducto;
    }

    public String getRecomendacionProdcuto() {
        return recomendacionProdcuto;
    }

    public void setRecomendacionProdcuto(String recomendacionProdcuto) {
        this.recomendacionProdcuto = recomendacionProdcuto;
    }

    public String getFechaProducto() {
        return fechaHistorialProducto;
    }

    public void setFechaProducto(String fechaProducto) {
        this.fechaHistorialProducto = fechaProducto;
    }

    public boolean isSendBd() {
        return sendBd;
    }

    public void setSendBd(boolean sendBd) {
        this.sendBd = sendBd;
    }

    public int numero(String valor) {
        try {
            Realm realm = Realm.getDefaultInstance();
            /* se consulta por el id max actual guardado*/
            Number number = realm.where(ProductoUsuario.class).max(valor);
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
