package manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.Material;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;
import persistencia.NeoDatisSGBD_CRUD;
import servicios.ServicioConsultasDAO;
import servicios.ServicioConsultasNeodatisDAO;
import utilidades.VALIDADOR;


public class ManagerMaterial {

    private final NeoDatisSGBD_CRUD<Material> crud;
    private final ServicioConsultasDAO<Material> consultas;
    
    private DefaultTableModel modelo;

    public ManagerMaterial(){
        crud = new NeoDatisSGBD_CRUD<>(Material.class);
        consultas = new ServicioConsultasNeodatisDAO<>(Material.class);
        
        modelo = new DefaultTableModel();
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
    
  public <T> DefaultTableModel cargarTabla(Class<T> tipo) {

    DefaultTableModel modelo = new DefaultTableModel();

    // OBTIENE CABECERAS REUTILIZANDO METODO
    String[] cabeceras = obtenerCabeceras(tipo);

    // AÑADE COLUMNAS
    for (String c : cabeceras) {
        modelo.addColumn(c);
    }

    // OBTIENE DATOS DESDE BASE DE DATOS
    ArrayList<T> lista = consultas.listar(tipo);

    for (T obj : lista) {

        Object[] fila = new Object[cabeceras.length];

        for (int i = 0; i < cabeceras.length; i++) {

            try {
                Field campo = tipo.getDeclaredField(cabeceras[i]);
                campo.setAccessible(true);
                fila[i] = campo.get(obj);
            } catch (Exception e) {
                fila[i] = null;
            }
        }

        modelo.addRow(fila);
    }

    return modelo;
}
  
      public Material cargaMaterialVista(String id){
          return consultas.buscarPorId(id, Material.class );
      }
      
public DefaultTableModel agregacionTabla(Class<?> clase, String campo, String funcion) {

    DefaultTableModel modelo = new DefaultTableModel();

    // ALIAS PARA EL RESULTADO DE LA CONSULTA
    String alias = "resultado";

    Field field;

    // OBTIENE EL CAMPO DE LA CLASE MEDIANTE REFLEXION
    try {
        field = clase.getDeclaredField(campo);
    } catch (NoSuchFieldException e) {
        modelo.addColumn("ERROR");
        modelo.addRow(new Object[]{"CAMPO NO EXISTE EN LA CLASE"});
        return modelo;
    }

    // OBTIENE EL TIPO DEL CAMPO Y VERIFICA SI ES NUMERICO
    Class<?> tipo = field.getType();
    boolean esNumerico = tipo == int.class || tipo == double.class ||
                         tipo == float.class || tipo == long.class ||
                         Number.class.isAssignableFrom(tipo);

    ValuesCriteriaQuery query = new ValuesCriteriaQuery(clase);

    // CONSTRUCCION DE LA CONSULTA SEGUN LA FUNCION
    switch (funcion.toLowerCase()) {

        case "count":
            query.count(alias);
            break;

        case "sum":
            if (!esNumerico) {
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"CAMPO NO PERMITIDO PARA SUM"});
                return modelo;
            }
            query.sum(campo, alias);
            break;

        case "avg":
            if (!esNumerico) {
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"CAMPO NO PERMITIDO PARA AVG"});
                return modelo;
            }
            query.avg(campo, alias);
            break;

        case "min":
            if (!esNumerico) {
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"CAMPO NO PERMITIDO PARA MIN"});
                return modelo;
            }
            query.min(campo, alias);
            break;

        case "max":
            if (!esNumerico) {
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"CAMPO NO PERMITIDO PARA MAX"});
                return modelo;
            }
            query.max(campo, alias);
            break;

        default:
            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"FUNCION NO SOPORTADA"});
            return modelo;
    }

    // EJECUTA LA CONSULTA Y CAPTURA ERRORES ARITMETICOS
    Values values;

    try {
        values = consultas.consultaAgregacion(query);
    } catch (ArithmeticException m) {

        modelo.addColumn("ERROR");
        modelo.addRow(new Object[]{"ERROR EN AVG: OPERACION ARITMETICA NO VALIDA"});
        return modelo;

    } catch (Exception e) {

        modelo.addColumn("ERROR");
        modelo.addRow(new Object[]{"ERROR EN LA CONSULTA"});
        return modelo;
    }

    // GENERA EL NOMBRE DE LA COLUMNA DE SALIDA
    String nombreColumna;

    if (funcion.equalsIgnoreCase("count")) {
        nombreColumna = "COUNT(*)";
    } else {
        nombreColumna = funcion.toUpperCase() + "(" + field.getName() + ")";
    }

    modelo.addColumn(nombreColumna);

    // OBTIENE Y AÑADE EL RESULTADO A LA TABLA
    if (values.hasNext()) {
        ObjectValues ov = (ObjectValues) values.next();
        Object resultado = ov.getByAlias(alias);
        modelo.addRow(new Object[]{resultado});
    }

    return modelo;
}

public DefaultTableModel orderByTabla(Class<?> clase, String campo, String orden) {

    DefaultTableModel modelo = new DefaultTableModel();

    // CREA CONSULTA DE TIPO OBJETOS (NO AGREGACION)
    CriteriaQuery query = new CriteriaQuery(clase);

    // DEFINE EL TIPO DE ORDENACION SEGUN EL VALOR DEL COMBO
    switch (orden.toLowerCase()) {

        case "asc":
            query.orderByAsc(campo);
            break;

        case "desc":
            query.orderByDesc(campo);
            break;

        default:
            // SI EL ORDEN NO ES VALIDO SE DEVUELVE TABLA CON ERROR
            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"ORDEN NO SOPORTADO"});
            return modelo;
    }

    Objects objects;

    try {
        // EJECUTA LA CONSULTA ORDENADA EN BASE DE DATOS
        objects = consultas.listarOrdenado(query);

    } catch (Exception e) {

        // SI FALLA LA CONSULTA SE DEVUELVE ERROR EN TABLA
        modelo.addColumn("ERROR");
        modelo.addRow(new Object[]{"ERROR EN LA CONSULTA"});
        return modelo;
    }

    // OBTIENE LOS NOMBRES DE LOS CAMPOS DE LA CLASE PARA LAS CABECERAS
    String[] cabeceras = obtenerCabeceras(clase);

    // AÑADE LAS CABECERAS A LA TABLA
    for (String c : cabeceras) {
        modelo.addColumn(c);
    }

    // RECORRE LOS OBJETOS OBTENIDOS DE LA CONSULTA
    while (objects.hasNext()) {

        Object obj = objects.next();

        // CREA FILA CON EL MISMO TAMAÑO QUE LAS CABECERAS
        Object[] fila = new Object[cabeceras.length];

        for (int i = 0; i < cabeceras.length; i++) {

            try {
                // OBTIENE EL VALOR DEL CAMPO MEDIANTE REFLEXION
                Field f = clase.getDeclaredField(cabeceras[i]);
                f.setAccessible(true);
                fila[i] = f.get(obj);

            } catch (Exception e) {
                // SI FALLA EL ACCESO SE ASIGNA NULL
                fila[i] = null;
            }
        }

        // AÑADE LA FILA AL MODELO
        modelo.addRow(fila);
    }

    // DEVUELVE EL MODELO LISTO PARA LA TABLA
    return modelo;
}
    public static String[] obtenerCabeceras(Class<?> clase) {

    Field[] fields = clase.getDeclaredFields();
    String[] cabeceras = new String[fields.length];

    for (int i = 0; i < fields.length; i++) {
        cabeceras[i] = fields[i].getName();
    }

    return cabeceras;
}
}
