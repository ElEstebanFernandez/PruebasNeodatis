package persistencia;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import modelo.anotaciones.Tabla;


/**
 * @author Esteban Fernandez Olid
 * @date 04/04/2026
 */
public class NeoDatisSGBD_CRUD<T> implements SGBD_SERVICIO_CRUD<T> {

    private  String tabla;
    private static final String RUTA = "ficheros_bd/";

    /*
* Hacer el setter para que cada entidad pase su tabla
* */
    public NeoDatisSGBD_CRUD(Class<T> claseTabla) {
        if (claseTabla.isAnnotationPresent(Tabla.class)) {
            this.tabla = claseTabla.getAnnotation(Tabla.class).name();
        } else {
            this.tabla = claseTabla.getSimpleName().toLowerCase();
        }
    }

    private ODB open() {
        return ODBFactory.open(RUTA + tabla + ".odb");
    }
    @Override
    public void init(Class<T> claseInstancia) {

    }

    @Override
    public boolean insert(T obj) {
        ODB odb = open();
        try {
            odb.store(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            odb.close();
        }
    }
    @Override
    public boolean update(T obj) {
        ODB odb = open();
        try {
            odb.store(obj);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            odb.close();
        }
    }

    @Override
    public boolean delete(String campo, Object valor, Class<T> type) {
        ODB odb = open();

        try {

            CriteriaQuery query = new CriteriaQuery(type, Where.equal(campo, valor));
            Objects<T> result = odb.getObjects(query);

            if (result == null || !result.hasNext()) return false;

            T obj = result.next();
            odb.delete(obj);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            odb.close();
        }

    }


    @Override
    public boolean createTable(T obj) {
        return false;
    }

    @Override
    public boolean loadData(String filePath) {
        
        return false;
    }

    @Override
    public boolean exportData(String filePath) {
        return false;
    }
}