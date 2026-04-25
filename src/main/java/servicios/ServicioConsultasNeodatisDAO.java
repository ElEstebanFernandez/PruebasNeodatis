/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import java.awt.List;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import modelo.anotaciones.Tabla;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

/**
 *
 * @author Dell
 * @param <T>
 */
public class ServicioConsultasNeodatisDAO<T> implements ServicioConsultasDAO<T> {

    // GUARDA EL NOMBRE DE LA TABLA / FICHERO .ODB
    private String tabla;

    // RUTA GENERAL DONDE SE GUARDAN LOS FICHEROS DE PERSISTENCIA
    private static final String RUTA = "ficheros_bd/";


    public ServicioConsultasNeodatisDAO(Class<T> claseTabla) {

        // SI LA CLASE TIENE ANOTACION @Tabla USA ESE NOMBRE
        if (claseTabla.isAnnotationPresent(Tabla.class)) {

            this.tabla = claseTabla.getAnnotation(Tabla.class).name();

        } else {

            // SI NO TIENE ANOTACION USA EL NOMBRE DE LA CLASE EN MINUSCULAS
            this.tabla = claseTabla.getSimpleName().toLowerCase();
        }
        File miFile = new File("ficheros_bd/");
        System.out.println( miFile.exists() ? "existe" : "no existe");
    
    }

    @Override
    public boolean crear(T objeto) {
        boolean creado = false;

        ODB odb = abrirBD();

        try {
            odb.store(objeto);
            creado = true;

        } catch (Exception e) {
            System.out.println("Error al crear el objeto: " + e.getMessage());
        } finally {
            odb.close();
        }

        return creado;
    }

    @Override
    public boolean actualizar(T objetoAntiguo, T objetoNuevo) {
        boolean actualizado = false;

        ODB odb = abrirBD();

        try {
            // ELIMINA EL OBJETO ANTIGUO
            odb.delete(objetoAntiguo);

            // ALMACENA EL OBJETO NUEVO
            odb.store(objetoNuevo);

            actualizado = true;
            odb.commit();

        } catch (Exception e) {
            odb.rollback();
            System.out.println("Error al actualizar el objeto: " + e.getMessage());
        } finally {
            odb.close();
        }

        return actualizado;
    }

    @Override
    public boolean eliminar(String id, Class<T> typo) {
        boolean eliminado = false;

        ODB odb = abrirBD();

        try {
            T objeto = buscarPorId(id, typo);
            odb.delete(objeto);
            eliminado = true;

        } catch (Exception e) {
            odb.rollback();
            System.out.println("Error al eliminar el objeto: " + e.getMessage());
        } finally {
            odb.close();
        }

        return eliminado;
    }

    @Override
    public ArrayList<T> listar(Class<T> tipo) {

        // ARRAYLIST DONDE SE GUARDAN LOS REGISTROS OBTENIDOS
        ArrayList<T> lista = new ArrayList<>();

        // ABRE LA BASE DE DATOS
        ODB odb = abrirBD();

        try {

            // OBTIENE TODOS LOS OBJETOS DEL TIPO INDICADO
            Objects<T> objects = odb.getObjects(tipo);

            // RECORRE TODOS LOS RESULTADOS Y LOS AÑADE A LA LISTA
            while (objects.hasNext()) {
                lista.add(objects.next());
            }

        } finally {

            // CIERRA SIEMPRE LA BD
            odb.close();
        }

        return lista;
    }


    @Override
    public T buscarPorId(String id, Class<T> tipo) {

        // ABRE LA BASE DE DATOS
        ODB odb = abrirBD();

        try {

            // CREA CONSULTA BUSCANDO CAMPO ID IGUAL AL VALOR RECIBIDO
            CriteriaQuery query = new CriteriaQuery(tipo, Where.equal("id", id));

            // EJECUTA CONSULTA
            Objects<T> result = odb.getObjects(query);

            // SI EXISTE DEVUELVE EL PRIMER RESULTADO
            if (result.hasNext()) {
                return result.next();
            }

        } finally {

            // CIERRA LA BD
            odb.close();
        }

        // SI NO ENCUENTRA NADA DEVUELVE NULL
        return null;
    }


    @Override
    public ArrayList<T> buscarPorCriterio(ICriterion criterio, Class<T> tipo) {

        // ABRE LA BASE DE DATOS
        ODB odb = abrirBD();

        try {

            // CONSTRUYE CONSULTA DINAMICA CON EL CRITERIO RECIBIDO
            CriteriaQuery constructorConsulta = new CriteriaQuery(tipo, criterio);

            // EJECUTA CONSULTA
            Objects<T> ejecutaConsulta = odb.getObjects(constructorConsulta);

            // CONVIERTE EL RESULTADO A ARRAYLIST
            return cargadorListas(ejecutaConsulta);

        } finally {

            // CIERRA LA BD
            odb.close();
        }
    }


    @Override
    public ArrayList<ObjectValues> buscarPorValue(ValuesCriteriaQuery valor) {

        // ABRE LA BASE DE DATOS
        ODB odb = abrirBD();

        // LISTA PARA GUARDAR RESULTADOS DE AGRUPACIONES / SUM / COUNT / MAX ...
        ArrayList<ObjectValues> salidaValue = new ArrayList();

        try {

            // EJECUTA CONSULTA VALUES
            Values consultaVaules = odb.getValues(valor);

            // RECORRE RESULTADOS Y LOS GUARDA
            while (consultaVaules.hasNext()) {
                salidaValue.add(consultaVaules.nextValues());
            }

            return salidaValue;

        } finally {

            // CIERRA LA BD
            odb.close();
        }
    }



    @Override
    public ODB abrirBD() {

        // ABRE EL FICHERO .ODB SEGUN EL NOMBRE DE TABLA
        return ODBFactory.open(RUTA + tabla + ".odb");
    }



    @Override
    public ArrayList<T> cargadorListas(Objects<T> entrada) {

        // CONVIERTE OBJECTS<T> DE NEODATIS A ARRAYLIST<T>
        ArrayList<T> carga = new ArrayList();

        entrada.forEach(e -> carga.add(e));

        return carga;
    }

}
