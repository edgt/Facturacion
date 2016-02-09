/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andrés
 */
public class Ventas extends javax.swing.JInternalFrame {

    /**
     * Creates new form Ventas
     */
    public Ventas() {
        initComponents();
        cargarTabla();
        clientesNombre();
        cajeroNombre();
        tblDatos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                desbloquearbotonesActualizar();
                desbloquear();
                btnBorrar.setEnabled(true);
                if (tblDatos.getSelectedRow()!=-1){
                    int fila=tblDatos.getSelectedRow();
                    txtNumero.setText(String.valueOf(tblDatos.getValueAt(fila, 0)));
                    jdcFec_rea.setDate(new Date (fechaTabla(String.valueOf(tblDatos.getValueAt(fila, 1)))));
                    cbCliente.setSelectedItem(clientesNombreTabla(String.valueOf(tblDatos.getValueAt(fila, 2))));
                    cbCajero.setSelectedItem(cajeroNombreTabla(String.valueOf(tblDatos.getValueAt(fila, 3))));
                    txtTotal.setText(String.valueOf(tblDatos.getValueAt(fila, 4)));
                }
            }
        });
        bloquear();
        limpiar();
        bloquearbotones();
    }
    
    public int cargarNumero(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        int valor=1;
        sql="SELECT MAX(num_ven) FROM ventas";
        try{
           Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                valor=rs.getInt("max")+1;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT "+e);
        }
        return valor;        
    }
    public String fechaTabla(String fecha){
    return String.valueOf(String.valueOf((fecha.charAt(8))+String.valueOf(fecha.charAt(9)))+"/"
                        +(String.valueOf(fecha.charAt(5))+String.valueOf(fecha.charAt(6)))+"/"
                        +(String.valueOf(fecha.charAt(0))+String.valueOf(fecha.charAt(1))
                        + String.valueOf(fecha.charAt(2))+String.valueOf(fecha.charAt(3))));
}
    
    DefaultTableModel modelo;
    
    public void cargarTabla(String codigo){
    String titulos[]={"NUMERO","FEC VEN","CLIENTE","CAJERO","TOTAL"};
    String[] Registros=new String[5];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM VENTAS WHERE NUM_VEN ='"+codigo+"'";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ven");
            Registros[1]=rs.getString("fec_ven");
            Registros[2]=rs.getString("ci_cli");
            Registros[3]=rs.getString("ci_caj");
            Registros[4]=rs.getString("tot_ven");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
    
    public void cargarTabla(){
    String titulos[]={"NUMERO","FEC VEN","CLIENTE","CAJERO","TOTAL"};
    String[] Registros=new String[5];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM VENTAS";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ven");
            Registros[1]=rs.getString("fec_ven");
            Registros[2]=rs.getString("ci_cli");
            Registros[3]=rs.getString("ci_caj");
            Registros[4]=rs.getString("tot_ven");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }

    public String clientesCodigo(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CLIENTES WHERE nom_cli ='"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("ci_cli");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public String clientesNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CLIENTES";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbCliente.addItem(rs.getString("nom_cli"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
     public String clientesNombreTabla(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CLIENTES WHERE CI_CLI = '"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("nom_cli");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
     
     public String cajeroCodigo(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CAJEROS WHERE NOM_CAJ ='"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("ci_caj");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public String cajeroNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CAJEROS";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbCajero.addItem(rs.getString("nom_caj"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
     public String cajeroNombreTabla(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM CAJEROS WHERE CI_CAJ = '"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("nom_caj");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public void guardar(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String fec_rea,ci_cli,ci_caj;
        int total,numero;
        
        numero=Integer.valueOf(txtNumero.getText());
        fec_rea=formatoFecha(jdcFec_rea);
        total=Integer.valueOf(txtTotal.getText());
        ci_cli=clientesCodigo(cbCliente.getSelectedItem().toString());
        ci_caj=cajeroCodigo(cbCajero.getSelectedItem().toString());
        
        String sql="INSERT INTO VENTAS VALUES("+numero+",'"+fec_rea+"','"+ci_cli+"','"+ci_caj+"',"+total+")";
        
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
    
    public String formatoFecha(JDateChooser fecha){
    String formato ="yyyy-MM-dd"; 
    //Formato
     Date date = fecha.getDate();
     SimpleDateFormat sdf = new SimpleDateFormat(formato);
     return sdf.format(date);
}
    
    public void modificar(){
        conexion cc=new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="UPDATE VENTAS SET fec_ven='"+formatoFecha(jdcFec_rea)+"', "
                            + "tot_ven='"+Integer.valueOf(txtTotal.getText())+"', "
                            + "ci_cli='"+clientesCodigo(cbCliente.getSelectedItem().toString())+"', "
                            + "ci_caj='"+cajeroCodigo(cbCajero.getSelectedItem().toString())+"' "
                +"WHERE num_ven='"+Integer.valueOf(txtNumero.getText())+"'";    
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
        jLabel3 = new javax.swing.JLabel();
        cbCliente = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jdcFec_rea = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        cbCajero = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Fecha realizacion");

        jLabel2.setText("Cliente");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel9.setText("Buscar");

        jLabel6.setText("Total");

        jdcFec_rea.setDateFormatString("yyyy-MM-dd");

        jLabel1.setText("Cajero");

        jLabel7.setText("Numero");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(68, 68, 68)
                        .addComponent(cbCajero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotal)
                            .addComponent(jdcFec_rea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNumero))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jdcFec_rea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbCajero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
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

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jButton1.setText("Detalles Venta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        cargarTabla(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

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

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        if (JOptionPane.showConfirmDialog(null,
            "¿Esta seguro que desea eliminar este registro?","ELIMINAR",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
        try {
            conexion cc= new conexion();
            Connection cn=(Connection) cc.conectar();
            String sql="";
            sql="DELETE FROM VENTAS WHERE num_ven='"+txtNumero.getText()+"'";
            PreparedStatement psd=(PreparedStatement) cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if (n>0){
                JOptionPane.showMessageDialog(null, "Registro borrado correctamente");
                limpiar();
                cargarTabla();
                bloquearbotones();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Detalle_ventas d=new Detalle_ventas();
        Menu.jDesktopPane1.add(d);
        d.setVisible(true);
        d.show();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cbCajero;
    private javax.swing.JComboBox cbCliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFec_rea;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
public void limpiar()
    {
        txtNumero.setText("");
        ((JTextField)jdcFec_rea.getDateEditor().getUiComponent()).setText("");
        txtTotal.setText("");
        cbCliente.setSelectedIndex(0);
        cbCajero.setSelectedIndex(0);
    }
    
    private void bloquear()
    {
        txtNumero.setEnabled(false);
        jdcFec_rea.setEnabled(false);
        txtTotal.setEnabled(false);
        cbCliente.setEnabled(false);
        cbCajero.setEnabled(false);
    }
    
    private void bloquearbotones()
    {
        btnActualizar.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setEnabled(false);       
    }
    
    private void desbloquear ()
    {
        txtNumero.setEnabled(true);
        txtNumero.setText(String.valueOf(cargarNumero()));
        jdcFec_rea.setEnabled(true);
        txtTotal.setEnabled(true);
        cbCliente.setEnabled(true);
        cbCajero.setEnabled(true);
    }
    private void desbloquearbotones()
    {
        btnActualizar.setEnabled(true);
        btnBorrar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(true);       
    }
    
    private void desbloquearbotonesActualizar()
    {
        btnNuevo.setEnabled(true);
        btnActualizar.setEnabled(true);
        
    }

}
