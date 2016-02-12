/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Andrés
 */
public class Pedidos extends javax.swing.JInternalFrame {
    DefaultTableModel modelo,detalle;
    /**
     * Creates new form Pedidos
     */
    public Pedidos() {
        initComponents();
        numeroPedido();
        recuperarBodegueros();
        recuperarVendedores();        
        bloquearbotones();
        setearTabla();        
    }
    public void nuevo(){
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);        
        btnProductos.setEnabled(true);
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
        sql="SELECT COUNT(num_ped) AS VALOR FROM pedido";
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
    public void numeroPedido(){
        if(recuperarNumero()!=0){
            conexion cc= new conexion();
            Connection cn=cc.conectar();
            String sql;            
            sql="SELECT LAST_VALUE+1 AS NP FROM pedido_num_ped_seq";
        try{
           Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNumero.setText(String.valueOf(rs.getInt("NP")));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT "+e);
        }
        }else
            txtNumero.setText("1");
    }
    public void recuperarBodegueros(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT ci_bod FROM BODEGUEROS";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbBodeguero.addItem(rs.getString("ci_bod"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public void recuperarVendedores(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT ci_ven FROM VENDEDORES";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                cbVendedor.addItem(rs.getString("ci_ven"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public String formatearFecha(Date fecha){        
        SimpleDateFormat cambiarfecha = new SimpleDateFormat("dd-MM-YYYY");
        return cambiarfecha.format(fecha);  
    }    
    public void bodeguerosNombre(String ci){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT nom_bod,ape_bod FROM BODEGUEROS WHERE CI_BOD ='"+ci+"'";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNomApeBod.setText(rs.getString("nom_bod")+" "+rs.getString("ape_bod"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }        
    }
    public void vendedoresNombre(String ci){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql;
        sql="SELECT nom_ven,ape_ven FROM VENDEDORES WHERE CI_VEN ='"+ci+"'";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            if(rs.next()){
                txtNomApeVen.setText(rs.getString("nom_ven")+" "+rs.getString("ape_ven"));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
        }
    }
    public void guardar(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String fec_ent=null,fec_rea,obs,ci_bod,ci_ven;
        int total;
        fec_rea=formatearFecha(jdcFec_rea.getDate());
        if(jdcFec_ent.getDate()!=null)
            fec_rea=formatearFecha(jdcFec_ent.getDate());        
        total=Integer.valueOf(txtTotal.getText());
        if(txtObservacion.getText().isEmpty())
            obs="NINGUNA";
        else
            obs=txtObservacion.getText().toUpperCase();        
        ci_bod=cbBodeguero.getSelectedItem().toString();
        ci_ven=cbVendedor.getSelectedItem().toString();
        
        String sql="INSERT INTO PEDIDO VALUES('"+fec_rea+"','" +fec_ent+ "',"+total+",'"+obs+"','"+ci_bod+"','"+ci_ven+"')";
        
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
        sql="INSERT INTO DETALLE_PEDIDO VALUES(?,?,?,?)";
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
    public void recuperarProductos(){
        conexion cc= new conexion();
        Connection cn=cc.conectar();
        String sql,registros[]=new String[6];
        String []titulos={"CÓDIGO","NOMBRE","MARCA","STOCK","UNIDAD DE MEDIDA","COSTO UNITARIO"};
        detalle=new DefaultTableModel(null,titulos);        
        sql="SELECT * FROM PRODUCTOS";
        try{
            Statement psd=cn.createStatement();
            ResultSet rs=psd.executeQuery(sql);
            while(rs.next()){
                registros[0]=rs.getString("cod_prod");
                registros[1]=rs.getString("nom_prod");
                registros[2]=rs.getString("mar_prod");
                registros[3]=rs.getString("stock");
                registros[4]=rs.getString("um");
                registros[5]=rs.getString("puc");
                detalle.addRow(registros);
            }
            tblProductos.setModel(detalle);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT"+e);
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
    public void modificar(){
        conexion cc=new conexion();
        Connection cn=cc.conectar();
        String fec_ent,obs,ci_bod,ci_ven,sql;        
        fec_ent=formatearFecha(jdcFec_ent.getDate());        
        if(txtObservacion.getText().isEmpty())
            obs="NINGUNA";
        else
            obs=txtObservacion.getText().toUpperCase();        
        ci_bod=cbBodeguero.getSelectedItem().toString();
        ci_ven=cbVendedor.getSelectedItem().toString();
        sql="UPDATE PEDIDO SET fec_ent='"+fec_ent+"', "
                            + "obs='"+obs+"', "
                            + "ci_bod='"+ci_bod+"', "
                            + "ci_ven='"+ci_ven+"' "
                +"WHERE num_ped='"+Integer.valueOf(txtNumero.getText())+"'";
        try {
            PreparedStatement psd=cn.prepareStatement(sql);     
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se actualizo correctamente");                
            }
      }catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex); 
      }
    }
    public void Actualizar(){
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);        
        jdcFec_rea.setEnabled(false);
    }
    public void borrar(){
        if (JOptionPane.showConfirmDialog(null,
            "¿Esta seguro que desea eliminar este registro?","ELIMINAR",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
        try {
            conexion cc= new conexion();
            Connection cn=cc.conectar();
            String sql;
            sql="DELETE FROM PEDIDO WHERE num_ped='"+txtNumero.getText()+"'";
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
    private void bloquearbotones(){
        btnActualizar.setEnabled(false);
        btnBorrar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setEnabled(false);        
        btnProductos.setEnabled(false);
    }
    public void detallePedido(int n,int np){
        Object[] fila=new Object[6];
        fila[0]=tblProductos.getValueAt(n, 0);
        fila[1]=tblProductos.getValueAt(n, 1);
        fila[2]=tblProductos.getValueAt(n, 2);
        fila[3]=np;
        fila[4]=tblProductos.getValueAt(n, 5);
        fila[5]=np*Float.valueOf((fila[4].toString()));
        modelo.addRow(fila);
        tblDatos.setModel(modelo);
        jDialog1.dispose();        
        total();        
    }   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        tbnIngresar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbBodeguero = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
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
        txtNomApeBod = new javax.swing.JTextField();
        txtNomApeVen = new javax.swing.JTextField();
        btnProductos = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();

        jDialog1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog1.setTitle("PRODUCTOS");
        jDialog1.setModal(true);

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblProductos);

        tbnIngresar.setText("Ingresar");
        tbnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbnIngresarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tbnIngresar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbnIngresar)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ADMINISTRACIÓN DE PEDIDOS");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBorrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBorrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Fecha realización:");

        jLabel4.setText("Fecha entrega:");

        cbBodeguero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBodegueroActionPerformed(evt);
            }
        });

        jLabel2.setText("Bodeguero:");

        jLabel5.setText("Observación:");

        jLabel6.setText("Total:");

        txtTotal.setEditable(false);

        jdcFec_rea.setDateFormatString("yyyy-MM-dd");

        jdcFec_ent.setDateFormatString("yyyy-MM-dd");

        jLabel1.setText("Vendedor:");

        cbVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVendedorActionPerformed(evt);
            }
        });

        jLabel7.setText("Número:");

        txtNumero.setEditable(false);

        txtNomApeBod.setEditable(false);

        txtNomApeVen.setEditable(false);

        btnProductos.setText("Producto");
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNomApeVen, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                            .addComponent(txtObservacion)
                            .addComponent(jdcFec_rea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdcFec_ent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotal)
                            .addComponent(txtNumero)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbBodeguero, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNomApeBod))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnProductos)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
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
                    .addComponent(jLabel2)
                    .addComponent(txtNomApeBod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNomApeVen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnProductos)
                .addContainerGap())
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
        ingresarDetalles();
        this.dispose();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        modificar();
        this.dispose();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        borrar();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void cbBodegueroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBodegueroActionPerformed
        // TODO add your handling code here:
        bodeguerosNombre(String.valueOf(cbBodeguero.getSelectedItem()));
    }//GEN-LAST:event_cbBodegueroActionPerformed

    private void cbVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVendedorActionPerformed
        // TODO add your handling code here:
        vendedoresNombre(String.valueOf(cbVendedor.getSelectedItem()));
    }//GEN-LAST:event_cbVendedorActionPerformed

    private void tbnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbnIngresarActionPerformed
        // TODO add your handling code here:
        int np=0;
        String can=JOptionPane.showInputDialog(null,"INGRESE LA CANTIDAD DE PRODUCTOS","INGRESO",JOptionPane.INFORMATION_MESSAGE);
        try {
            np=Integer.valueOf(can);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ADVERTENCIA","INGRESE UN NÚMERO",JOptionPane.WARNING_MESSAGE);
        }
        if(np>0){
            detallePedido(tblProductos.getSelectedRow(), np);
        }
    }//GEN-LAST:event_tbnIngresarActionPerformed

    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        // TODO add your handling code here:
        recuperarProductos();
        jDialog1.setBounds(100,100,550,350);
        jDialog1.setLocationRelativeTo(null);
        jDialog1.setVisible(true);
    }//GEN-LAST:event_btnProductosActionPerformed

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
    private javax.swing.JButton btnProductos;
    private javax.swing.JComboBox cbBodeguero;
    private javax.swing.JComboBox cbVendedor;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdcFec_ent;
    private com.toedter.calendar.JDateChooser jdcFec_rea;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTable tblProductos;
    private javax.swing.JButton tbnIngresar;
    private javax.swing.JTextField txtNomApeBod;
    private javax.swing.JTextField txtNomApeVen;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtObservacion;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables

}
