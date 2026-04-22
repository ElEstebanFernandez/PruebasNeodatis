package modelo.anotaciones;


/**
 * @author Dell
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