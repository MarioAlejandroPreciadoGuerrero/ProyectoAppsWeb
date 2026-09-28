/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author USER
 */
public class Conexion {
    private static EntityManagerFactory emf = null;
    
    private Conexion (){
    }
    
    public static EntityManager crearConexion (){
        try {
            if (emf == null ){
                emf = Persistence.createEntityManagerFactory("ConexionPU");
            }
            return emf.createEntityManager();
        } catch (Exception e) {  
            System.out.println("Ha ocurrido un error a la hora de crear la conexion" + e.getMessage());
            
            throw new RuntimeException("Error con la conexion");
        }
    }
    
    public static void cerrarConexion(){
        if (emf != null && emf.isOpen()){
            emf.close();
        }
    }
}
