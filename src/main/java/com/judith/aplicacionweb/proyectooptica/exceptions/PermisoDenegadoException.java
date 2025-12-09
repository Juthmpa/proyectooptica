package com.judith.aplicacionweb.proyectooptica.exceptions;


/**
 * Excepción lanzada cuando un usuario intenta realizar una acción
 * para la que no tiene permisos según su rol.
 */
public class PermisoDenegadoException extends RuntimeException {
    public PermisoDenegadoException(String message) {
        super(message);
    }
}