/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Andrés
 */
public class conexion {
    Connection conec=null;
    
    public Connection conectar(){
     //   String url = "jdbc:postgresql://localhost:5432/facturacion";
       String url = "jdbc:postgresql://192.168.10.10:5432/Facturacion";
        String password = "root";
        try {
            Class.forName("org.postgresql.Driver");
            conec = DriverManager.getConnection(url, "postgres", "iamedgt2510");            
           // JOptionPane.showMessageDialog(null, "Conexión Exitosa ");
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Conexión Erronea "+e);
        }
        return conec;
    }
}
