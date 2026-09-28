
import Conexion.Conexion;
import Dominio.Cliente;
import javax.persistence.EntityManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author USER
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    EntityManager em = Conexion.crearConexion();
        
        try{
            Cliente nuevoCliente = new Cliente();
            
            em.getTransaction().begin();
            
            em.persist(nuevoCliente);
            
            em.getTransaction().commit();
        }catch( Exception e){
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al realizar la transacción: " + e.getMessage());
            e.printStackTrace();
        
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            Conexion.cerrarConexion();
        }
        
    }
    
}
