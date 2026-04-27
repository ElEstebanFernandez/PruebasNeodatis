/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import modelo.Material;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

/**
 *
 * @author Dell
 * @param <T>
 */
public interface ServicioConsultasDAO<T> {

    ODB abrirBD();

    <T> ArrayList<T> listar(Class<T> tipo);

    T buscarPorId(String id, Class<T> typo);

    ArrayList<T> buscarPorCriterio(ICriterion criterio, Class<T> typo);

    ArrayList<ObjectValues> buscarPorValue(ValuesCriteriaQuery valor);

    ArrayList<T> cargadorListas(Objects<T> entrada);
    
    Values consultaAgregacion(ValuesCriteriaQuery query);

    Objects listarOrdenado(CriteriaQuery query);
    

}
