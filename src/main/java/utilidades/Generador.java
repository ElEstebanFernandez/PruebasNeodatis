package utilidades;

import modelo.Material;
import modelo.anotaciones.Tabla;

import java.util.Random;
import java.util.random.RandomGenerator;

public class Generador {

    /**
     * Genera un id aleatorio para una entidad
     * @param tipo clase de la entidad
     * @return id generado
     * @param <T> tipo de la entidad
     */
    public static <T> String generarId(Class<T> tipo) {
        StringBuilder idGenerado = new StringBuilder();
        String nombreEntidad = "";

        if (tipo.isAnnotationPresent(Tabla.class)) {
            nombreEntidad = tipo.getAnnotation(Tabla.class).name()
                    .toUpperCase().substring(0, 3).concat("_");
        } else {
            nombreEntidad = tipo.getSimpleName()
                    .toUpperCase().substring(0, 3).concat("_");
        }

        idGenerado.append(nombreEntidad);
        RandomGenerator random = new Random();
        for (int i = 0; i <= 10; i++) {
            idGenerado.append(random.nextInt(10));
        }

        return idGenerado.toString();
    }
}
