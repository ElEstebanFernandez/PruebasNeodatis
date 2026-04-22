package modelo;


import modelo.anotaciones.Id;
import modelo.anotaciones.ForeignKey;
import modelo.anotaciones.Tabla;


import java.time.LocalDate;
import java.util.Date;

/**
 * @author Esteban Fernandez Olid
 * @date 03/04/2026
 */


@Tabla(name = "usuario")

public class Usuario {

    @Id
    private String id;                 // CHAR(12)
    private String email;              // CHAR(50)
    private String nombre;             // CHAR(25)
    private String tipo;          // ENUM
    private String pregSeguridad;      // CHAR(100)
    private String resSeguridad;       // CHAR(100)
    private Date registroFecha;   // DATE
    private String contrasena;         // CHAR(60)
    @ForeignKey(references = "direccion(id)")
    private String idDireccion;       // CHAR(12)

    public Usuario(String id, String email, String nombre, String tipo,
                   String pregSeguridad, String resSeguridad,
                   Date registroFecha, String contrasena,
                   String idDireccion) {

        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.tipo = tipo;
        this.pregSeguridad = pregSeguridad;
        this.resSeguridad = resSeguridad;
        this.registroFecha = registroFecha;
        this.contrasena = contrasena;
        this.idDireccion = idDireccion;
    }
    public Usuario(String email, String contrasena){
        this.email = email;
        this.contrasena = contrasena;
    }

    public Usuario() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPregSeguridad() {
        return pregSeguridad;
    }

    public void setPregSeguridad(String pregSeguridad) {
        this.pregSeguridad = pregSeguridad;
    }

    public String getResSeguridad() {
        return resSeguridad;
    }

    public void setResSeguridad(String resSeguridad) {
        this.resSeguridad = resSeguridad;
    }

    public Date getRegistroFecha() {
        return registroFecha;
    }

    public void setRegistroFecha(Date registroFecha) {
        this.registroFecha = registroFecha;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", pregSeguridad='" + pregSeguridad + '\'' +
                ", resSeguridad='" + resSeguridad + '\'' +
                ", registroFecha=" + registroFecha +
                ", contrasena='" + contrasena + '\'' +
                ", idDireccion='" + idDireccion + '\'' +
                '}';
    }
}