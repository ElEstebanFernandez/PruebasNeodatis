package persistencia;




/**
 * @author Esteban Fernandez Olid
 * @author José Antonio Caldeŕon Pineda
 */
public interface SGBD_SERVICIO_CRUD<T>{

    boolean insert(T obj);

    boolean update(String id, T objNuevo);

    boolean delete(String campo, Object valor, Class<T> type);

    boolean createTable(T obj);

    boolean loadData(String filePath);

    boolean exportData(String filePath);

    void init(Class<T> claseInstancia);


}


