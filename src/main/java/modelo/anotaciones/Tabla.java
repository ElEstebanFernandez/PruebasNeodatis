package modelo.anotaciones;


/**
 * @author Esteban Fernandez Olid
 * @author José Antonio Calderón Pineda
 * @version 1.0
 * @since 2026
 * Project: Eco_Pulse_2026
 */
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Tabla {
    String name();
}