package com.judith.aplicacionweb.proyectooptica.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashUtil {

    // Define el costo (strength) del hashing. 12 es un buen compromiso.
    private static final int HASH_COST = 12;

    /**
     * Hashea (encripta) una contraseña.
     *
     * @param password La contraseña en texto plano.
     * @return El hash seguro de la contraseña.
     */
    public static String hashPassword(String password) {
        // Genera el salt y el hash en un solo paso
        return BCrypt.hashpw(password, BCrypt.gensalt(HASH_COST));
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash dado.
     *
     * @param plainPassword  La contraseña ingresada por el usuario.
     * @param hashedPassword El hash almacenado en la base de datos.
     * @return true si coincide, false en caso contrario.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Ejemplo de uso:
    public static void main(String[] args) {
        String password = "MiPasswordSecreto123";
        String hashed = hashPassword(password);
        System.out.println("Password Hash: " + hashed);

        // La verificación debe ser TRUE
        boolean isCorrect = checkPassword(password, hashed);
        System.out.println("Verificación Correcta: " + isCorrect);

        // La verificación debe ser FALSE
        boolean isIncorrect = checkPassword("OtraPassword", hashed);
        System.out.println("Verificación Incorrecta: " + isIncorrect);
    }
}
