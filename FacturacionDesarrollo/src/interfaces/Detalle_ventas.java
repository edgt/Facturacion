/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andrés
 */
public class Detalle_ventas extends javax.swing.JInternalFrame {

    /**
     * Creates new form Detalle_ventas
     */
    public Detalle_ventas() {
        initComponents();
        cargarTabla();
        ventasNombre();
        productosNombre();
        tblDatos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                desbloquearbotonesActualizar();
                desbloquear();
                if (tblDatos.getSelectedRow()!=-1){
                    int fila=tblDatos.getSelectedRow();
                    cbNumero.setSelectedItem(String.valueOf(tblDatos.getValueAt(fila, 0)));
                    cbProducto.setSelectedItem(productosNombreTabla(String.valueOf(tblDatos.getValueAt(fila, 1))));
                    txtCantidad.setText(String.valueOf(tblDatos.getValueAt(fila, 2)));
                    txtSubtotal.setText(String.valueOf(tblDatos.getValueAt(fila, 3)));
                }
            }
        });
        bloquear();
        limpiar();
        bloquearbotones();
    }
    
    DefaultTableModel modelo;
    
    public void cargarTabla(){
    String titulos[]={"VENTA","PRODUCTO","CANTIDAD","SUBTOTAL"};
    String[] Registros=new String[4];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM DETALLE_VENTA";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ven");
            Registros[1]=rs.getString("cod_prod");
            Registros[2]=rs.getString("cant_ven");
            Registros[3]=rs.getString("subt");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
    
    public void cargarTabla(String numero,String codigo){
    String titulos[]={"VENTA","PRODUCTO","CANTIDAD","SUBTOTAL"};
    String[] Registros=new String[4];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM DETALLE_VENTA WHERE num_ven ='"+numero+"' AND cod_prod='"+codigo+"'";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ven");
            Registros[1]=rs.getString("cod_prod");
            Registros[2]=rs.getString("cant_ven");
            Registros[3]=rs.getString("subt");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
    
    public String ventasNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM ventas";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbNumero.addItem(rs.getString("num_ven"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
     
     public String productosCodigo(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM productos WHERE NOM_PROD ='"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("cod_prod");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public String productosNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM PRODUCTOS";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbProducto.addItem(rs.getString("nom_prod"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
     public String productosNombreTabla(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM PRODUCTOS WHERE COD_PROD = '"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("nom_prod");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public void guardar(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String cod_prod;
        int subtotal,cantidad,num_ven;
        
        cantidad=Integer.valueOf(txtCantidad.getText());
        subtotal=Integer.valueOf(txtSubtotal.getText());
        num_ven=Integer.valueOf(cbNumero.getSelectedItem().toString());
        cod_prod=productosCodigo(cbProducto.getSelectedItem().toString());
        
        String sql="INSERT INTO DETALLE_VENTA VALUES("+num_ven+",'"+cod_prod+"','"+cantidad+"','"+subtotal+"')";
        
       try {
            PreparedStatement psd=(PreparedStatement) cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se inserto correctamente "); 
                cargarTabla();
            }           
        } 
       catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede insertar la información"+ex);
        }
    }
       
    public void modificar(){
        conexion cc=new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="UPDATE DETALLE_VENTA SET cant_ven='"+txtCantidad.getText()+"', "
                            + "subt='"+txtSubtotal.getText()+"' "
                +"WHERE num_ven='"+Integer.valueOf(cbNumero.getSelectedItem().toString())+"' "
                + "AND cod_prod='"+productosCodigo(cbProducto.getSelectedItem().toString())+"'";    
        try {
            PreparedStatement psd=(PreparedStatement) cn.prepareStatement(sql);     
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se actualizo correctamente");
                cargarTabla();
            }
      }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex); 
      }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        txtPedido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbNumero = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cbProducto = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtProducto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel4.setText("Cantidad");

        jLabel8.setText("Subtotal");

        txtPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPedidoKeyReleased(evt);
            }
        });

        jLabel9.setText("Pedido");

        jLabel2.setText("Numero Pedido");

        jLabel1.setText("Producto");

        jLabel3.setText("Producto");

        txtProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel8))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSubtotal)
                            .addComponent(cbProducto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCantidad)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtProducto)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cbProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        btnNuevo.setText("     Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("    Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setText("   Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSalir.setText("        Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addGap(50, 50, 50))
        );

        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDatos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPedidoKeyReleased
        cargarTabla(txtPedido.getText(),txtProducto.getText());
    }//GEN-LAST:event_txtPedidoKeyReleased

    private void txtProductoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductoKeyReleased
        cargarTabla(txtPedido.getText(),txtProducto.getText());
    }//GEN-LAST:event_txtProductoKeyReleased

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        conexion op = new conexion();
        op.conectar();
        desbloquear();
        desbloquearbotones();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        modificar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquearbotones();
        limpiar();
        bloquear(); 
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Detalle_ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Detalle_ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Detalle_ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Detalle_ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Detalle_ventas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cbNumero;
    private javax.swing.JComboBox cbProducto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtPedido;
    private javax.swing.JTextField txtProducto;
    private javax.swing.JTextField txtSubtotal;
    // End of variables declaration//GEN-END:variables
public void limpiar()
    {
        txtCantidad.setText("");
        txtSubtotal.setText("");
        cbNumero.setSelectedIndex(0);
        cbProducto.setSelectedIndex(0);
    }
    
    private void bloquear()
    {
        txtCantidad.setEnabled(false);
        txtSubtotal.setEnabled(false);
        cbNumero.setEnabled(false);
        cbProducto.setEnabled(false);
    }
    
    private void bloquearbotones()
    {
        btnActualizar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setEnabled(false);       
    }
    
    private void desbloquear ()
    {
        txtCantidad.setEnabled(true);
        txtSubtotal.setEnabled(true);
        cbNumero.setEnabled(true);
        cbProducto.setEnabled(true);
    }
    private void desbloquearbotones()
    {
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(true);       
    }
    
    private void desbloquearbotonesActualizar()
    {
        btnNuevo.setEnabled(true);
        btnActualizar.setEnabled(true);
        
    }

}
