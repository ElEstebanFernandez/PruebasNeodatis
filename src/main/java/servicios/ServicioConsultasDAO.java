/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package servicios;

import java.util.ArrayList;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

/**
 *
 * @author Dell
 * @param <T>
 */
public interface ServicioConsultasDAO<T> {
    
    ODB abrirBD();
    ArrayList<T> listar(Class<T> typo);
    T buscarPorId(int id,Class<T> typo);
    ArrayList<T> buscarPorCriterio(ICriterion criterio,Class<T> typo);
    ArrayList<ObjectValues> buscarPorValue(ValuesCriteriaQuery valor);
    ArrayList<T> cargadorListas(Objects<T> entrada);

    
    
}
