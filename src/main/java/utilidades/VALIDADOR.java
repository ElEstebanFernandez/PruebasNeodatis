package utilidades;

import java.util.regex.Pattern;

/**
 * Enumeracion de patrones de validacion
 * @author José Antonio Calderón
 */
public enum VALIDADOR {

    /**
     * Patrones de validacion
     */
    ID_MATERIAL("^MAT_\\d{11}$"),
    ID_FABRICANTE("FAB_\\d{11}$"),
    NOMBRE("^[a-zA-Z0-9 ]{3,50}$"),
    PUNTOS("^[1-9]\\d*$"),
    VOLUMEN("^[1-9]\\d*(\\.\\d+)?$"),
    CANTIDAD("^[1-9]\\d*$"),
    COMPUESTOS("PLASTICO|VIDRIO|PAPEL|OTRO"),
    TOXICIDAD("LETAL|ALTA|MEDIA|BAJA|NINGUNA"),
    LOTE("^[0-9]{1,10}$"),
    FECHA_ALTA("^\\d{2}/\\d{2}/\\d{4}$");

    /**
     * Patron a utilizar
     */
    private final Pattern pattern;

    /**
     * Constructor de la enumeracion
     * @param regex patron a utilizar
     */
    VALIDADOR(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    /**
     * Valida una cadena con el patron de un enumerado
     * @param cadena cadena a validar
     * @return true si la cadena cumple con el patron, false en caso contrario
     */
    public boolean validar(String cadena) {
        if (cadena == null || cadena.isEmpty()) return false;
        return pattern.matcher(cadena).matches();
    }

}
