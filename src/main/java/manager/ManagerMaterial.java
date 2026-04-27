package manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.Material;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;
import persistencia.NeoDatisSGBD_CRUD;
import servicios.ServicioConsultasDAO;
import servicios.ServicioConsultasNeodatisDAO;
import utilidades.VALIDADOR;


// CLASE MANAGER MATERIAL
// GESTIONA CRUD, CONSULTAS, ORDER BY, FILTROS E ICRITERION USANDO NEODATIS

public class ManagerMaterial {

    private final NeoDatisSGBD_CRUD<Material> crud;
    private final ServicioConsultasDAO<Material> consultas;

    private DefaultTableModel modelo;

    public ManagerMaterial(){
        crud = new NeoDatisSGBD_CRUD<>(Material.class);
        consultas = new ServicioConsultasNeodatisDAO<>(Material.class);

        modelo = new DefaultTableModel();
    }

    // CREA UN MATERIAL VALIDANDO TODOS LOS CAMPOS
    public boolean crearMaterial(String idMaterial, String nombre, int puntos, double volumen, int cantidad,
                                 String compuestos, String toxicidad, boolean enPromocion,
                                 String lote, String fechaAlta, String idFabricante) {

        boolean fueCreado = false;

        // VALIDACION DE CAMPOS DE ENTRADA
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
        ) return false;

        Material material = new Material(idMaterial, nombre, puntos, volumen, cantidad,
                                        compuestos, toxicidad, enPromocion, lote, fechaAlta, idFabricante);

        // INSERTA EN BASE DE DATOS
        fueCreado = crud.insert(material);

        return fueCreado;
    }

    // ELIMINA MATERIAL POR ID
    public boolean eliminarMaterial(String idMaterial) {
        return crud.delete("id", idMaterial, Material.class);
    }

    // ACTUALIZA MATERIAL EXISTENTE
    public boolean actualizarMaterial(String idMaterial, String nombre, int puntos, double volumen,
                                      int cantidad, String compuestos, String toxicidad,
                                      boolean enPromocion, String lote, String fechaAlta,
                                      String idFabricante) {

        boolean fueActualizado = false;

        // VALIDACION IGUAL QUE EN CREAR
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
        ) return false;

        Material material = new Material(idMaterial, nombre, puntos, volumen, cantidad,
                                        compuestos, toxicidad, enPromocion, lote, fechaAlta, idFabricante);

        // UPDATE EN BASE DE DATOS
        fueActualizado = crud.update(material);

        return fueActualizado;
    }

    // CARGA TODA LA TABLA DE MATERIAL EN UN DEFAULTTABLEMODEL
    public <T> DefaultTableModel cargarTabla(Class<T> tipo) {

        DefaultTableModel modelo = new DefaultTableModel();

        // OBTIENE CABECERAS DESDE REFLEXION
        String[] cabeceras = obtenerCabeceras(tipo);

        for (String c : cabeceras) {
            modelo.addColumn(c);
        }

        // LISTA TODOS LOS OBJETOS
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

    // BUSCA MATERIAL POR ID PARA MOSTRAR EN VISTA
    public Material cargaMaterialVista(String id){
        return consultas.buscarPorId(id, Material.class);
    }

    // REALIZA AGREGACIONES (SUM AVG MIN MAX COUNT)
    public DefaultTableModel agregacionTabla(Class<?> clase, String campo, String funcion) {

        DefaultTableModel modelo = new DefaultTableModel();

        String alias = "resultado";

        Field field;

        // BUSCA EL CAMPO EN LA CLASE
        try {
            field = clase.getDeclaredField(campo);
        } catch (NoSuchFieldException e) {
            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"CAMPO NO EXISTE EN CLASE"});
            return modelo;
        }

        // VERIFICA SI ES NUMERICO
        Class<?> tipo = field.getType();
        boolean esNumerico = tipo == int.class || tipo == double.class ||
                             tipo == float.class || tipo == long.class ||
                             Number.class.isAssignableFrom(tipo);

        ValuesCriteriaQuery query = new ValuesCriteriaQuery(clase);

        // SWITCH DE FUNCIONES DE AGREGACION
        switch (funcion.toLowerCase()) {

            case "count":
                query.count(alias);
                break;

            case "sum":
                if (!esNumerico) {
                    modelo.addColumn("ERROR");
                    modelo.addRow(new Object[]{"CAMPO NO VALIDO PARA SUM"});
                    return modelo;
                }
                query.sum(campo, alias);
                break;

            case "avg":
                if (!esNumerico) {
                    modelo.addColumn("ERROR");
                    modelo.addRow(new Object[]{"CAMPO NO VALIDO PARA AVG"});
                    return modelo;
                }
                query.avg(campo, alias);
                break;

            case "min":
                if (!esNumerico) {
                    modelo.addColumn("ERROR");
                    modelo.addRow(new Object[]{"CAMPO NO VALIDO PARA MIN"});
                    return modelo;
                }
                query.min(campo, alias);
                break;

            case "max":
                if (!esNumerico) {
                    modelo.addColumn("ERROR");
                    modelo.addRow(new Object[]{"CAMPO NO VALIDO PARA MAX"});
                    return modelo;
                }
                query.max(campo, alias);
                break;

            default:
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"FUNCION NO SOPORTADA"});
                return modelo;
        }

        Values values;

        try {
            values = consultas.consultaAgregacion(query);

        } catch (ArithmeticException m) {

            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"ERROR ARITMETICO EN AVG"});
            return modelo;

        } catch (Exception e) {

            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"ERROR EN CONSULTA"});
            return modelo;
        }

        // NOMBRE DE COLUMNA RESULTADO
        String nombreColumna;

        if (funcion.equalsIgnoreCase("count")) {
            nombreColumna = "COUNT(*)";
        } else {
            nombreColumna = funcion.toUpperCase() + "(" + field.getName() + ")";
        }

        modelo.addColumn(nombreColumna);

        // AÑADE RESULTADO
        if (values.hasNext()) {
            ObjectValues ov = (ObjectValues) values.next();
            modelo.addRow(new Object[]{ov.getByAlias(alias)});
        }

        return modelo;
    }

    // ORDER BY ASC O DESC
    public DefaultTableModel orderByTabla(Class<?> clase, String campo, String orden) {

        DefaultTableModel modelo = new DefaultTableModel();

        CriteriaQuery query = new CriteriaQuery(clase);

        // ORDENACION SEGUN COMBO
        switch (orden.toLowerCase()) {

            case "asc":
                query.orderByAsc(campo);
                break;

            case "desc":
                query.orderByDesc(campo);
                break;

            default:
                modelo.addColumn("ERROR");
                modelo.addRow(new Object[]{"ORDEN NO VALIDO"});
                return modelo;
        }

        Objects objects;

        try {
            objects = consultas.listarOrdenado(query);

        } catch (Exception e) {

            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"ERROR EN CONSULTA"});
            return modelo;
        }

        // CABECERAS DINAMICAS
        String[] cabeceras = obtenerCabeceras(clase);

        for (String c : cabeceras) {
            modelo.addColumn(c);
        }

        // CARGA DATOS
        while (objects.hasNext()) {

            Object obj = objects.next();
            Object[] fila = new Object[cabeceras.length];

            for (int i = 0; i < cabeceras.length; i++) {

                try {
                    Field f = clase.getDeclaredField(cabeceras[i]);
                    f.setAccessible(true);
                    fila[i] = f.get(obj);

                } catch (Exception e) {
                    fila[i] = null;
                }
            }

            modelo.addRow(fila);
        }

        return modelo;
    }

    // OBTIENE NOMBRES DE CAMPOS DE LA CLASE
    public static String[] obtenerCabeceras(Class<?> clase) {

        Field[] fields = clase.getDeclaredFields();
        String[] cabeceras = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            cabeceras[i] = fields[i].getName();
        }

        return cabeceras;
    }

    // FILTRO DINAMICO CON ICRITERION
    public DefaultTableModel filtrarIcriterion(Class<Material> clase, String campo, String operador, String valor) {

        DefaultTableModel modelo = new DefaultTableModel();

        try {

            // CONVIERTE VALOR A COMPARABLE SEGUN TIPO
            Comparable valorConvertido = convertirValor(clase, campo, valor);

            ICriterion criterio;

            switch (operador.toLowerCase()) {

                case "igual a":
                    criterio = Where.equal(campo, valorConvertido);
                    break;

                case "mayor que":
                    criterio = Where.gt(campo, valorConvertido);
                    break;

                case "menor que":
                    criterio = Where.lt(campo, valorConvertido);
                    break;

                case "mayor o igual":
                    criterio = Where.ge(campo, valorConvertido);
                    break;

                case "menor o igual":
                    criterio = Where.le(campo, valorConvertido);
                    break;

                case "contiene":
                    criterio = Where.like(campo, "%" + valor + "%");
                    break;

                default:
                    modelo.addColumn("ERROR");
                    modelo.addRow(new Object[]{"OPERADOR NO SOPORTADO"});
                    return modelo;
            }

            CriteriaQuery query = new CriteriaQuery(clase, criterio);
            Objects objects = consultas.listarOrdenado(query);

            String[] cabeceras = obtenerCabeceras(clase);

            for (String c : cabeceras) {
                modelo.addColumn(c);
            }

            while (objects.hasNext()) {

                Object obj = objects.next();
                Object[] fila = new Object[cabeceras.length];

                for (int i = 0; i < cabeceras.length; i++) {

                    try {
                        Field f = clase.getDeclaredField(cabeceras[i]);
                        f.setAccessible(true);
                        fila[i] = f.get(obj);
                    } catch (Exception e) {
                        fila[i] = null;
                    }
                }

                modelo.addRow(fila);
            }

        } catch (Exception e) {

            modelo.addColumn("ERROR");
            modelo.addRow(new Object[]{"ERROR EN FILTRO"});
        }

        return modelo;
    }

    // CONVIERTE STRING A TIPO SEGUN CAMPO
    private Comparable convertirValor(Class<?> clase, String campo, String valor) {

        try {
            Field f = clase.getDeclaredField(campo);
            Class<?> tipo = f.getType();

            if (tipo == int.class || tipo == Integer.class)
                return Integer.parseInt(valor);

            if (tipo == double.class || tipo == Double.class)
                return Double.parseDouble(valor);

            if (tipo == float.class || tipo == Float.class)
                return Float.parseFloat(valor);

            if (tipo == long.class || tipo == Long.class)
                return Long.parseLong(valor);

            if (tipo == String.class)
                return valor;

            // BOOLEAN NO SE USA EN COMPARACIONES
            return null;

        } catch (Exception e) {
            return null;
        }
    }
}