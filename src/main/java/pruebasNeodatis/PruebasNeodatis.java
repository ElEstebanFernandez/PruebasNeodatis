/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pruebasNeodatis;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import modelo.Fabricante;
import modelo.Material;
import modelo.Usuario;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import vista.VentanaLoggin;

/**
 *
 * @author Dell
 */
public class PruebasNeodatis {

    public static void main(String[] args) {
        
     //LANZA INTERFACE GRAFICA
      VentanaLoggin loginVentana = new VentanaLoggin();
      loginVentana.show();
    
      //FABRICANTE PRUEBAS
     // ID ->     "FAB_12345678910",
    //CONTRASEÑA  "UI18827A$%E"
   
  /*
    Prueba listar Material
    ServicioConsultasDAO<Material> consultasBd = new ServicioConsultasNeodatisDAO(Material.class);
    
    ArrayList<Material> miMaterial = consultasBd.listar(Material.class);
    
    miMaterial.forEach(m->System.out.println(m));*/
    
  



    }
}
