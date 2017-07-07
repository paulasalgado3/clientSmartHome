package paula.smarthome;

/**
 * Created by Paula on 04/07/2017.
 */

public class Dispositivo {
    private String id;
    private String tipo;
    private Boolean estado;
    private String ubicacion;

    public void Dispositivo(String id, String tipo, Boolean estado){
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
    }
    public void Dispositivo(String id, String tipo, Boolean estado, String ubicacion){
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
        this.ubicacion = ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEstado(Boolean estado) {

        this.estado = estado;
    }

    public void setTipo(String tipo) {

        this.tipo = tipo;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Boolean getEstado() {

        return estado;
    }

    public String getTipo() {

        return tipo;
    }

    public String getId() {

        return id;
    }

    public String getUbicacion() {

        return ubicacion;
    }
}
