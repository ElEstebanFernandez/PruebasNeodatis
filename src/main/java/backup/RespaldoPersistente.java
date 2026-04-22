package backup;


/**
 * @author Esteban Fernandez Olid
 * @date 22/04/2026
 */
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class RespaldoPersistente {

    // RUTA HACIA LA CARPETA DONDE SE GUARDA LA PERSISTENCIA DE NEODATIS
    private final Path rutaOrigen = Paths.get("ficheros_bd/");


    public void hacerRespaldo() {

        // SELECCIONA LA CARPETA DONDE SE GUARDARA EL RESPALDO
        Path baseDestino = elegirDirectorio();

        if (baseDestino == null) return;

        // SE GENERA EL NOMBRE DE LA CARPETA CON LA FECHA ACTUAL
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path carpetaRespaldo = baseDestino.resolve("respaldo_" + fecha);

        try {
            // CREA LA CARPETA DE RESPALDO SI NO EXISTE
            Files.createDirectories(carpetaRespaldo);

            // COPIA TODO EL CONTENIDO DE LA BD A LA CARPETA DE RESPALDO
            copiarCarpeta(rutaOrigen, carpetaRespaldo);

            JOptionPane.showMessageDialog(null,
                    "Respaldo creado correctamente en:\n" + carpetaRespaldo);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al hacer el respaldo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void restaurarRespaldoSeguro() {

        // SELECCIONA LA CARPETA QUE CONTIENE EL RESPALDO A RESTAURAR
        Path origenRespaldo = elegirDirectorio();

        if (origenRespaldo == null) return;

        // CONFIRMACION DEL USUARIO ANTES DE SOBRESCRIBIR LA BD
        int opcion = JOptionPane.showConfirmDialog(
                null,
                "ATENCION !!\nSe va a RESTAURAR la base de datos.\n" +
                        "Se sustituirá completamente la BD actual.\n\n" +
                        "¿Deseas continuar?",
                "Confirmar restauración",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        // SI EL USUARIO NO CONFIRMA SE CANCELA EL PROCESO
        if (opcion != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Restauración cancelada.");
            return;
        }

        try {
            // CREA UN BACKUP DE SEGURIDAD ANTES DE RESTAURAR
            Path backupPrevio = Paths.get("backup_antes_restauracion");
            Files.createDirectories(backupPrevio);
            copiarCarpeta(rutaOrigen, backupPrevio);

            // BORRA LA BASE DE DATOS ACTUAL COMPLETA
            if (Files.exists(rutaOrigen)) {
                borrarCarpeta(rutaOrigen);
            }

            // VUELVE A CREAR LA CARPETA DE LA BD
            Files.createDirectories(rutaOrigen);

            // COPIA EL RESPALDO SELECCIONADO A LA BD ACTUAL
            copiarCarpeta(origenRespaldo, rutaOrigen);

            JOptionPane.showMessageDialog(null,
                    "Restauración completada correctamente.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error durante la restauración:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private Path elegirDirectorio() {

        // ABRE VENTANA PARA SELECCIONAR UNA CARPETA
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int resultado = chooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().toPath();
        }

        return null;
    }


    private void copiarCarpeta(Path origen, Path destino) throws IOException {

        // RECORRE RECURSIVAMENTE TODA LA ESTRUCTURA DE CARPETAS
        Files.walkFileTree(origen, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                // CREA LA MISMA ESTRUCTURA DE CARPETAS EN EL DESTINO
                Path targetDir = destino.resolve(origen.relativize(dir));
                Files.createDirectories(targetDir);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                // COPIA CADA ARCHIVO AL DESTINO (SOBREESCRIBE SI EXISTE)
                Path targetFile = destino.resolve(origen.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);

                return FileVisitResult.CONTINUE;
            }
        });
    }


    private void borrarCarpeta(Path ruta) throws IOException {

        // BORRA RECURSIVAMENTE TODOS LOS ARCHIVOS Y CARPETAS
        Files.walkFileTree(ruta, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                // BORRA ARCHIVOS UNO A UNO
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

                // BORRA LAS CARPETAS DESPUES DE VACIARLAS
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}