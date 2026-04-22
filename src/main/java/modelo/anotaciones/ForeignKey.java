package modelo.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Dell
 * @version 1.0
 * @since 2026
 * Project: Eco_Pulse_2026
 */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ForeignKey {
        String references();
    }

