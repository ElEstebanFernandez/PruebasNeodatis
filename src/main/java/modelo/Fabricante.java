package modelo;


import modelo.anotaciones.Id;
import modelo.anotaciones.ForeignKey;
import modelo.anotaciones.Tabla;
import java.util.Date;

/**
 * @author Esteban Fernandez Olid
 * @date 06/04/2026
 */
@Tabla(name = "fabricante")
public class Fabricante extends Usuario {


    @Id
    @ForeignKey(references = "usuario(id)")
    private String idUsuario;
        private String cif;
        private String sector; // ENUM


    public Fabricante(String id, String email, String nombre, String tipo, String pregSeguridad, String resSeguridad, Date registroFecha, String contrasena, String idDireccion, String cif, String sector, String idUsuario) {
        super(id, email, nombre, tipo, pregSeguridad, resSeguridad, registroFecha, contrasena, idDireccion);
        this.cif = cif;
        this.sector = sector;
this.idUsuario=idUsuario;
    }

    public Fabricante(String email, String contrasena) {
        super(email, contrasena);

    }

    public Fabricante() {}

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }


    @Override
    public String toString() {
        return super.toString()
                + "\nFabricante: " +
                ", cif='" + cif + '\'' +
                ", sector='" + sector ;
    }
}
