package persistencia;




/**
 * @author Esteban Fernandez Olid
 * @param <T>
 * @date 04/04/2026
 */
public interface SGBD_SERVICIO_CRUD<T>{

    boolean insert(T obj);

    boolean update(T obj);

    boolean delete(String campo, Object valor, Class<T> type);

    boolean createTable(T obj);

    boolean loadData(String filePath);

    boolean exportData(String filePath);

    void init(Class<T> claseInstancia);


}


