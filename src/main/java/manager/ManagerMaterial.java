package manager;

import modelo.Material;
import persistencia.NeoDatisSGBD_CRUD;
import servicios.ServicioConsultasDAO;
import servicios.ServicioConsultasNeodatisDAO;
import utilidades.VALIDADOR;


public class ManagerMaterial {

    private final NeoDatisSGBD_CRUD<Material> crud;
    private final ServicioConsultasDAO<Material> consultas;

    public ManagerMaterial(){
        crud = new NeoDatisSGBD_CRUD<>(Material.class);
        consultas = new ServicioConsultasNeodatisDAO<>(Material.class);
    }

    public boolean crearMaterial( String idMaterial, String nombre, int puntos, double volumen, int cantidad, String compuestos,
                                  String toxicidad, boolean enPromocion, String lote, String fechaAlta, String idFabricante) {
        boolean fueCreado = false;

        // Validamos los campos de entrada
        if (
                !VALIDADOR.ID_MATERIAL.validar(idMaterial) ||
                !VALIDADOR.NOMBRE.validar(nombre) ||
                !VALIDADOR.PUNTOS.validar(String.valueOf(puntos)) ||
                !VALIDADOR.VOLUMEN.validar(String.valueOf(volumen)) ||
                !VALIDADOR.CANTIDAD.validar(String.valueOf(cantidad)) ||
                !VALIDADOR.COMPUESTOS.validar(compuestos) ||
                !VALIDADOR.TOXICIDAD.validar(toxicidad) ||
                !VALIDADOR.LOTE.validar(lote) ||
                !VALIDADOR.FECHA_ALTA.validar(fechaAlta) ||
                !VALIDADOR.ID_FABRICANTE.validar(idFabricante)
        ) return fueCreado;

        Material material = new Material(idMaterial, nombre, puntos, volumen, cantidad, compuestos, toxicidad, enPromocion, lote, fechaAlta, idFabricante);
        fueCreado = crud.insert(material);

        return fueCreado;
    }

    public boolean eliminarMaterial(String idMaterial) {
        boolean fueEliminado = false;

        fueEliminado = crud.delete("id", idMaterial, Material.class);

        return fueEliminado;
    }

    public boolean actualizarMaterial(String idMaterial, String nombre, int puntos, double volumen, int cantidad, String compuestos, String toxicidad, boolean enPromocion, String lote, String fechaAlta, String idFabricante) {
        boolean fueActualizado = false;

        // Validamos los campos de entrada
        if (
                !VALIDADOR.ID_MATERIAL.validar(idMaterial) ||
                        !VALIDADOR.NOMBRE.validar(nombre) ||
                        !VALIDADOR.PUNTOS.validar(String.valueOf(puntos)) ||
                        !VALIDADOR.VOLUMEN.validar(String.valueOf(volumen)) ||
                        !VALIDADOR.CANTIDAD.validar(String.valueOf(cantidad)) ||
                        !VALIDADOR.COMPUESTOS.validar(compuestos) ||
                        !VALIDADOR.TOXICIDAD.validar(toxicidad) ||
                        !VALIDADOR.LOTE.validar(lote) ||
                        !VALIDADOR.FECHA_ALTA.validar(fechaAlta) ||
                        !VALIDADOR.ID_FABRICANTE.validar(idFabricante)
        ) return fueActualizado;

        Material material = new Material(idMaterial, nombre, puntos, volumen, cantidad, compuestos, toxicidad, enPromocion, lote, fechaAlta, idFabricante);
        fueActualizado = crud.update(material);

        return fueActualizado;
    }
}
