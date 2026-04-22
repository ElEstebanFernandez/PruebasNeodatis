package modelo;


import modelo.anotaciones.Id;
import modelo.anotaciones.ForeignKey;
import modelo.anotaciones.Tabla;

/**
 * @author Esteban Fernandez Olid
 * @date 11/04/2026
 */


@Tabla(name = "material")
public class Material {

    @Id
    private String id;
    private String nombre;
    private int puntos;
    private double volumen;
    private int cantidad;
    private String compuestos;
    private String toxicidad;
    private boolean enPromocion;
    private String lote;
    private String fechaAlta;
    @ForeignKey(references = "fabricante(id)")
    private String idFabricante;

    public Material() {
    }

    public Material(String id, String nombre, int puntos, double volumen, int cantidad,
                    String compuestos, String toxicidad, boolean enPromocion,
                    String lote, String fechaAlta, String idFabricante) {
        this.id = id;
        this.nombre = nombre;
        this.puntos = puntos;
        this.volumen = volumen;
        this.cantidad = cantidad;
        this.compuestos = compuestos;
        this.toxicidad = toxicidad;
        this.enPromocion = enPromocion;
        this.lote = lote;
        this.fechaAlta = fechaAlta;
        this.idFabricante = idFabricante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCompuestos() {
        return compuestos;
    }

    public void setCompuestos(String compuestos) {
        this.compuestos = compuestos;
    }

    public String getToxicidad() {
        return toxicidad;
    }

    public void setToxicidad(String toxicidad) {
        this.toxicidad = toxicidad;
    }

    public boolean isEnPromocion() {
        return enPromocion;
    }

    public void setEnPromocion(boolean enPromocion) {
        this.enPromocion = enPromocion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getIdFabricante() {
        return idFabricante;
    }

    public void setIdFabricante(String idFabricante) {
        this.idFabricante = idFabricante;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", puntos=" + puntos +
                ", volumen=" + volumen +
                ", cantidad=" + cantidad +
                ", compuestos='" + compuestos + '\'' +
                ", toxicidad='" + toxicidad + '\'' +
                ", enPromocion=" + enPromocion +
                ", lote='" + lote + '\'' +
                ", fechaAlta='" + fechaAlta + '\'' +
                ", idFabricante='" + idFabricante + '\'' +
                '}';
    }
}