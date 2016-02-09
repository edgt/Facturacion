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
public class Pedidos extends javax.swing.JInternalFrame {

    /**
     * Creates new form Pedidos
     */
    public Pedidos() {
        initComponents();
        cargarTabla();
        bodeguerosNombre();
        vendedorNombre();
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
                    jdcFec_ent.setDate(new Date (fechaTabla(String.valueOf(tblDatos.getValueAt(fila, 2)))));
                    txtTotal.setText(String.valueOf(tblDatos.getValueAt(fila, 3)));
                    txtObservacion.setText(String.valueOf(tblDatos.getValueAt(fila, 4)));
                    cbBodeguero.setSelectedItem(bodeguerosNombreTabla(String.valueOf(tblDatos.getValueAt(fila, 5))));
                    cbVendedor.setSelectedItem(vendedorNombreTabla(String.valueOf(tblDatos.getValueAt(fila, 6))));
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
        sql="SELECT MAX(num_ped) FROM pedido";
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
    String titulos[]={"NUMERO","FEC REA","FEC ENT","TOTAL","OBSERVACION","BODEGUERO","VENDEDOR"};
    String[] Registros=new String[7];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM PEDIDO WHERE NUM_PED ='"+codigo+"'";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ped");
            Registros[1]=rs.getString("fec_rea");
            Registros[2]=rs.getString("fec_ent");
            Registros[3]=rs.getString("total");
            Registros[4]=rs.getString("obs");
            Registros[5]=rs.getString("ci_bod");
            Registros[6]=rs.getString("ci_ven");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
    
    public void cargarTabla(){
    String titulos[]={"NUMERO","FEC REA","FEC ENT","TOTAL","OBSERVACION","BODEGUERO","VENDEDOR"};
    String[] Registros=new String[7];
    conexion cc= new conexion();
    Connection cn=(Connection) cc.conectar();
    modelo=new DefaultTableModel(null, titulos);
    String sql="";
    sql="SELECT*FROM PEDIDO WHERE";
    try{
        Statement psd=(Statement) cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        while(rs.next()){
            Registros[0]=rs.getString("num_ped");
            Registros[1]=rs.getString("fec_rea");
            Registros[2]=rs.getString("fec_ent");
            Registros[3]=rs.getString("total");
            Registros[4]=rs.getString("obs");
            Registros[5]=rs.getString("ci_bod");
            Registros[6]=rs.getString("ci_ven");
            modelo.addRow(Registros);
            tblDatos.setModel(modelo);
        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }

    public String bodeguerosCodigo(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM BODEGUEROS WHERE NOM_BOD ='"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("ci_bod");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public String bodeguerosNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM BODEGUEROS";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbBodeguero.addItem(rs.getString("nom_bod"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
     public String bodeguerosNombreTabla(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM BODEGUEROS WHERE CI_BOD = '"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("nom_bod");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
     
     public String vendedorCodigo(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM VENDEDORES WHERE NOM_VEN ='"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("ci_ven");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public String vendedorNombre(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM VENDEDORES";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbVendedor.addItem(rs.getString("nom_ven"));
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
     public String vendedorNombreTabla(String codigo){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String sql="";
        sql="SELECT*FROM VENDEDORES WHERE CI_VEN = '"+codigo+"'";
        try{
            Statement psd=(Statement) cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                return rs.getString("nom_ven");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
        return "";
    }
    
    public void guardar(){
        conexion cc= new conexion();
        Connection cn=(Connection) cc.conectar();
        String fec_ent,fec_rea,obs,ci_bod,ci_ven;
        int total,numero;
        
        numero=Integer.valueOf(txtNumero.getText());
        fec_ent=formatoFecha(jdcFec_ent);
        fec_rea=formatoFecha(jdcFec_rea);
        total=Integer.valueOf(txtTotal.getText());
        obs=txtObservacion.getText().toUpperCase();
        ci_bod=bodeguerosCodigo(cbBodeguero.getSelectedItem().toString());
        ci_ven=vendedorCodigo(cbVendedor.getSelectedItem().toString());
        
        String sql="INSERT INTO PEDIDO VALUES("+numero+",'"+fec_rea+"','" +fec_ent+ "',"+total+",'"+obs+"','"+ci_bod+"','"+ci_ven+"')";
        
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
        sql="UPDATE PEDIDO SET fec_rea='"+formatoFecha(jdcFec_rea)+"', "
                            + "fec_ent='"+formatoFecha(jdcFec_ent)+"', "
                            + "obs='"+txtObservacion.getText()+"', "
                            + "total='"+Integer.valueOf(txtTotal.getText())+"', "
                            + "ci_bod='"+bodeguerosCodigo(cbBodeguero.getSelectedItem().toString())+"', "
                            + "ci_ven='"+vendedorCodigo(cbVendedor.getSelectedItem().toString())+"' "
                +"WHERE num_ped='"+Integer.valueOf(txtNumero.getText())+"'";    
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

        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbBodeguero = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtObservacion = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jdcFec_rea = new com.toedter.calendar.JDateChooser();
        jdcFec_ent = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        cbVendedor = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        jButton1.setText("Detalles Pedido");
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
                .addGap(22, 22, 22)
                .addComponent(jButton1)
                .addContainerGap(31, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        jLabel3.setText("Fecha realizacion");

        jLabel4.setText("Fecha entrega");

        jLabel2.setText("Bodeguero");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel9.setText("Buscar");

        jLabel5.setText("Observacion");

        jLabel6.setText("Total");

        jdcFec_rea.setDateFormatString("yyyy-MM-dd");

        jdcFec_ent.setDateFormatString("yyyy-MM-dd");

        jLabel1.setText("Vendedores");

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
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 129, Short.MAX_VALUE))
                            .addComponent(cbVendedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbBodeguero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtObservacion)
                            .addComponent(jdcFec_rea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdcFec_ent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotal)
                            .addComponent(txtNumero))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jdcFec_rea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jdcFec_ent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbBodeguero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            sql="DELETE FROM PEDIDO WHERE num_ped='"+txtNumero.getText()+"'";
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

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        cargarTabla(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Detalle_pedido d=new Detalle_pedido();
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
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pedidos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pedidos().setVisible(true);
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
    private javax.swing.JComboBox cbBodeguero;
    private javax.swing.JComboBox cbVendedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFec_ent;
    private com.toedter.calendar.JDateChooser jdcFec_rea;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtObservacion;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
public void limpiar()
    {
        txtNumero.setText("");
        ((JTextField)jdcFec_ent.getDateEditor().getUiComponent()).setText("");
        ((JTextField)jdcFec_rea.getDateEditor().getUiComponent()).setText("");
        txtTotal.setText("");
        txtObservacion.setText("");
        cbBodeguero.setSelectedIndex(0);
        cbVendedor.setSelectedIndex(0);
    }
    
    private void bloquear()
    {
        txtNumero.setEnabled(false);
        jdcFec_ent.setEnabled(false);
        jdcFec_rea.setEnabled(false);
        txtTotal.setEnabled(false);
        txtObservacion.setEnabled(false);
        cbBodeguero.setEnabled(false);
        cbVendedor.setEnabled(false);
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
        jdcFec_ent.setEnabled(true);
        jdcFec_rea.setEnabled(true);
        txtTotal.setEnabled(true);
        txtObservacion.setEnabled(true);
        cbBodeguero.setEnabled(true);
        cbVendedor.setEnabled(true);
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
