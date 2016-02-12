/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/** 
 *
 * @author Andrés
 */
public class Ventas extends javax.swing.JFrame {
    DefaultTableModel modelo;
    
    public Ventas() {        
        initComponents();        
        numeroVentas();
        recuperarCajeros();
        recuperarClientes();
        setearTabla();
        bloquearbotones();
        txtFecha.setText(fecha());        
        txtBarra.requestFocus();
    }
    public void nuevo(){
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
    }
    public void setearTabla(){
        String []titulos={"CÓDIGO","PRODUCTO","MARCA","CANTIDAD","COSTO UNITARIO","SUBTOTAL"};
        modelo=new DefaultTableModel(null,titulos);
        tblDatos.setModel(modelo);
    }
    public int recuperarNumero(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        int valor=0;
        sql="SELECT COUNT(num_ven) AS VALOR FROM ventas";
        try{
           Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                valor=rs.getInt("VALOR");
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT "+e);
        }
        return valor;
    }
    public void numeroVentas(){
        if(recuperarNumero()!=0){
            conexion cc= new conexion();
            Connection cn=cc.conectar();
            String sql;            
            sql="SELECT LAST_VALUE+1 AS NV FROM ventas_num_ven_seq";
        try{
           Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNumero.setText(String.valueOf(rs.getInt("NV")));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT "+e);
        }
        }else
            txtNumero.setText("1");
    }
    public void recuperarClientes(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT ci_cli FROM CLIENTES";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbCliente.addItem(rs.getString("ci_cli"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public void recuperarCajeros(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT ci_caj FROM CAJEROS";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbCajero.addItem(rs.getString("ci_caj"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public String fecha(){
        Date now = new Date();
        SimpleDateFormat cambiarfecha = new SimpleDateFormat("dd-MM-YYYY");
        return cambiarfecha.format(now);
    }
    public void clienteDatos(String ci){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT nom_cli,ape_cli,dir_cli FROM CLIENTES WHERE CI_CLI ='"+ci+"'";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNomCli.setText(rs.getString("nom_cli")+" "+rs.getString("ape_cli"));
                txtDirCli.setText(rs.getString("dir_cli"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }        
    }
    public void cajerosDatos(String ci){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT nom_caj,ape_caj FROM CAJEROS WHERE CI_CAJ ='"+ci+"'";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNomCaj.setText(rs.getString("nom_caj")+" "+rs.getString("ape_caj"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public void guardar(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String fec,ci_cli,ci_caj;
        int total;
        fec=txtFecha.getText();
        total=Integer.valueOf(txtTotal.getText());        
        ci_cli=cbCliente.getSelectedItem().toString();
        ci_caj=cbCajero.getSelectedItem().toString();
        
        String sql="INSERT INTO VENTAS VALUES('"+fec+",'"+ci_cli+"','"+ci_caj+"','"+total+"')";
        
       try {
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se inserto correctamente "); 
            }           
        } 
       catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se puede insertar la información"+ex);
        }
    }
    public void ingresarDetalles(){
        conexion c=new conexion();
        Connection cn=c.conectar();
        String sql;
        int nfil,nped;
        nped=Integer.valueOf(txtNumero.getText());
        sql="INSERT INTO DETALLE_VENTA VALUES(?,?,?,?)";
        nfil=tblDatos.getRowCount();
        for(int i=0;i<nfil;i++){
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setInt(1,nped);
                psd.setString(2,String.valueOf(tblDatos.getValueAt(i,0)));
                psd.setInt(3,Integer.valueOf(tblDatos.getValueAt(i, 3).toString()));
                psd.setFloat(4,Float.valueOf(tblDatos.getValueAt(i, 5).toString()));
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se inserto correctamente","INFORMACIÓN",JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    public void cargarProductoComprado(){    
    conexion cc= new conexion();
    Connection cn=cc.conectar();    
    String sql;
    Object registros[]=new Object[6];
    sql="SELECT*FROM productos where cod_barras='"+txtBarra.getText()+"'";
    try{
        Statement psd=cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        if(rs.next()){
            registros[0]=rs.getString("cod_prod");
            if(repetido(registros[0].toString())==0){
                registros[1]=rs.getString("nom_prod");
                registros[2]=rs.getString("mar_prod");
                registros[3]=1;
                registros[4]=rs.getString("puv");
                registros[5]=Float.valueOf(registros[4].toString())*Integer.valueOf(registros[3].toString());
                modelo.addRow(registros);
                tblDatos.setModel(modelo);
                total();
            }else{
                setRepetido(registros[0].toString());
            }                        
        }else{
            JOptionPane.showMessageDialog(null, "NO EXISTE EL PRODUCTO","ADVERVENCIA",JOptionPane.INFORMATION_MESSAGE);
            txtBarra.setText("");
            txtBarra.requestFocus();
        }
    }catch(NumberFormatException | SQLException e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
   public void cargarProductoComprado(String cod){    
    conexion cc= new conexion();
    Connection cn=cc.conectar();
    String sql;
    Object registros[]=new Object[6];
    sql="SELECT*FROM productos where cod_barras='"+cod+"'";
    try{
        Statement psd=cn.createStatement();
        ResultSet rs=psd.executeQuery(sql);
        if(rs.next()){
            registros[0]=rs.getString("cod_prod");
            if(repetido(registros[0].toString())==0){
                registros[1]=rs.getString("nom_prod");
                registros[2]=rs.getString("mar_prod");
                registros[3]=1;
                registros[4]=rs.getString("puv");
                registros[5]=Float.valueOf(registros[4].toString())*Integer.valueOf(registros[3].toString());
                modelo.addRow(registros);
                tblDatos.setModel(modelo);
                total();
            }else{
                setRepetido(registros[0].toString());
            }                        
        }else{
            JOptionPane.showMessageDialog(null, "NO EXISTE EL PRODUCTO","ADVERVENCIA",JOptionPane.INFORMATION_MESSAGE);
            txtBarra.setText("");
            txtBarra.requestFocus();
        }
    }catch(NumberFormatException | SQLException e){
        JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
    }
    }
   public int repetido(String comp){
       int nreg,av,n=0;
       nreg=tblDatos.getRowCount();
       for(int i=0;i<nreg;i++){
           if(tblDatos.getValueAt(i, 0).equals(comp)){
               av=Integer.valueOf(tblDatos.getValueAt(i, 3).toString());               
               n=av+1;
           }
       }
       System.out.println(n);
       return n;
   }
   public void setRepetido(String comp){
       int nreg,av;
       float st;
       nreg=tblDatos.getRowCount();
       for(int i=0;i<nreg;i++){
           if(tblDatos.getValueAt(i, 0).equals(comp)){
               av=Integer.valueOf(tblDatos.getValueAt(i, 3).toString());
               st=(av+1)*Float.valueOf(tblDatos.getValueAt(i, 4).toString());
               tblDatos.setValueAt(av+1, i,3);
               tblDatos.setValueAt(st, i,5);
               total();
           }
       }       
   }
   public void borrar(){
        if (JOptionPane.showConfirmDialog(null,
            "¿Esta seguro que desea eliminar este registro?","ELIMINAR",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
        try {
            conexion cc= new conexion();
            Connection cn=cc.conectar();
            String sql;
            sql="DELETE FROM VENTAS WHERE num_ven='"+txtNumero.getText()+"'";
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if (n>0){
                JOptionPane.showMessageDialog(null, "Registro borrado correctamente");                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        }
    }
   public void total(){
        int nfil=tblDatos.getRowCount();
        float tot=0;
        for(int i=0;i<nfil;i++){
            tot=tot+Float.valueOf(String.valueOf(tblDatos.getValueAt(i,5)));
        }
        txtTotal.setText(String.valueOf(tot));
    }
   private void bloquearbotones(){        
        btnBorrar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setEnabled(false);        
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
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cbCajero = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtNomCli = new javax.swing.JTextField();
        txtDirCli = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNomCaj = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtBarra = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VENTAS");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Fecha factura:");

        cbCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClienteActionPerformed(evt);
            }
        });

        jLabel2.setText("Cliente:");

        jLabel6.setText("Total:");

        txtTotal.setEditable(false);

        jLabel1.setText("Cajero:");

        cbCajero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCajeroActionPerformed(evt);
            }
        });

        jLabel7.setText("Número:");

        txtNumero.setEditable(false);

        txtNomCli.setEditable(false);

        txtDirCli.setEditable(false);

        txtFecha.setEditable(false);

        jLabel4.setText("Nombre:");

        jLabel5.setText("Dirección:");

        jLabel8.setText("Nombre:");

        txtNomCaj.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNumero)
                    .addComponent(cbCliente, 0, 173, Short.MAX_VALUE)
                    .addComponent(txtNomCli)
                    .addComponent(txtDirCli))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFecha)
                    .addComponent(cbCajero, 0, 190, Short.MAX_VALUE)
                    .addComponent(txtNomCaj)
                    .addComponent(txtTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cbCajero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtNomCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtNomCaj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDirCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addGap(35, 35, 35)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setText("Producto:");

        txtBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarraActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnBuscar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBuscar)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(185, 185, 185)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
      guardar();
      ingresarDetalles();
      this.dispose();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
       this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        borrar();
        this.dispose();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void cbClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClienteActionPerformed
        // TODO add your handling code here:
        clienteDatos(String.valueOf(cbCliente.getSelectedItem()));
    }//GEN-LAST:event_cbClienteActionPerformed

    private void cbCajeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCajeroActionPerformed
        // TODO add your handling code here:
        cajerosDatos(String.valueOf(cbCajero.getSelectedItem()));
    }//GEN-LAST:event_cbCajeroActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        cargarProductoComprado();
        txtBarra.setText("");
        txtBarra.requestFocus();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarraActionPerformed
        // TODO add your handling code here:
        cargarProductoComprado(txtBarra.getText());
        txtBarra.requestFocus();
    }//GEN-LAST:event_txtBarraActionPerformed

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
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox cbCajero;
    private javax.swing.JComboBox cbCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtBarra;
    private javax.swing.JTextField txtDirCli;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNomCaj;
    private javax.swing.JTextField txtNomCli;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables


}
