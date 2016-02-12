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

    DefaultTableModel modelo;
    float total = 0;

    public Ventas() {
        initComponents();
        txtTotal.setText(String.valueOf(total));
        cargarTabla();
        clientesNombre();
        cajeroNombre();
        cargarNumero();
        btnActualizar.setEnabled(false);
        btnGuardar.setEnabled(false);

    }

    public int cargarNumero() {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        int valor = 1;
        sql = "SELECT MAX(num_ven) FROM ventas";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                valor = rs.getInt("max") + 1;
                txtCantidad.setText(String.valueOf(valor));
                JOptionPane.showMessageDialog(null, "se obtuvo " + valor);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT " + e);
        }
        return valor;
    }

    public String fechaTabla(String fecha) {
        return String.valueOf(String.valueOf((fecha.charAt(8)) + String.valueOf(fecha.charAt(9))) + "/"
                + (String.valueOf(fecha.charAt(5)) + String.valueOf(fecha.charAt(6))) + "/"
                + (String.valueOf(fecha.charAt(0)) + String.valueOf(fecha.charAt(1))
                + String.valueOf(fecha.charAt(2)) + String.valueOf(fecha.charAt(3))));
    }

    public void cargarCroductoComprado() {
        String titulos[] = {"CODIGO", "CODIGO DE BARRA", "NOMBRE", "MARCA", "PUV", "Sub-total"};
        Object[] Registros = new String[5];
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        modelo = new DefaultTableModel(null, titulos);
        String sql = "";
        sql = "SELECT*FROM productos where cod_barras='" + txtBarra.getText() + "'";

        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {

                Registros[1] = rs.getString("cod_prod");
                Registros[2] = txtBarra.getText();
                Registros[3] = rs.getString("nom_prod");
                Registros[4] = rs.getString("puv");
                Registros[5] = (Float.valueOf(rs.getString("puv"))) * (Integer.valueOf(txtCantidad.getText()));
                modelo.addRow(Registros);
                tblDatos.setModel(modelo);
                total = total + (Float.valueOf(rs.getString("puv"))) * (Integer.valueOf(txtCantidad.getText()));
                txtTotal.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
    }

    public void cargarTabla(String codigo) {
        String titulos[] = {"NUMERO", "FEC VEN", "CLIENTE", "CAJERO", "TOTAL"};
        String[] Registros = new String[5];
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        modelo = new DefaultTableModel(null, titulos);
        String sql = "";
        sql = "SELECT*FROM VENTAS WHERE NUM_VEN ='" + codigo + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                Registros[0] = rs.getString("num_ven");
                Registros[1] = rs.getString("fec_ven");
                Registros[2] = rs.getString("ci_cli");
                Registros[3] = rs.getString("ci_caj");
                Registros[4] = rs.getString("tot_ven");
                modelo.addRow(Registros);
                tblDatos.setModel(modelo);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
    }

    public void cargarTabla() {
        String titulos[] = {"NUMERO", "FEC VEN", "CLIENTE", "CAJERO", "TOTAL"};
        String[] Registros = new String[5];
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        modelo = new DefaultTableModel(null, titulos);
        String sql = "";
        sql = "SELECT*FROM VENTAS";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                Registros[0] = rs.getString("num_ven");
                Registros[1] = rs.getString("fec_ven");
                Registros[2] = rs.getString("ci_cli");
                Registros[3] = rs.getString("ci_caj");
                Registros[4] = rs.getString("tot_ven");
                modelo.addRow(Registros);
                tblDatos.setModel(modelo);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
    }

    public String clientesCodigo(String codigo) {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CLIENTES WHERE nom_cli ='" + codigo + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("ci_cli");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public String clientesNombre() {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CLIENTES";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                cbCliente.addItem(rs.getString("nom_cli"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public String clientesNombreTabla(String codigo) {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CLIENTES WHERE CI_CLI = '" + codigo + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("nom_cli");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public String cajeroCodigo(String codigo) {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CAJEROS WHERE NOM_CAJ ='" + codigo + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("ci_caj");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public String cajeroNombre() {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CAJEROS";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                cbCajero.addItem(rs.getString("nom_caj"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public String cajeroNombreTabla(String codigo) {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "SELECT*FROM CAJEROS WHERE CI_CAJ = '" + codigo + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("nom_caj");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el SELECT" + e);
        }
        return "";
    }

    public void guardarVentas() {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String fec_rea, ci_cli, ci_caj;
        int total, numero;

        numero = Integer.valueOf(txtNumero.getText());
        fec_rea = formatoFecha(jdcFec_rea);
        total = Integer.valueOf(txtTotal.getText());
        ci_cli = clientesCodigo(cbCliente.getSelectedItem().toString());
        ci_caj = cajeroCodigo(cbCajero.getSelectedItem().toString());

        String sql = "INSERT INTO VENTAS VALUES(" + numero + ",'" + fec_rea + "','" + ci_cli + "','" + ci_caj + "'," + total + ")";

        try {
            PreparedStatement psd = (PreparedStatement) cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se inserto correctamente ");
                cargarTabla();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se puede insertar la información" + ex);
        }
    }

    public void IngresarDetalles() {
        conexion c = new conexion();
        Connection cn = c.conectar();
        int nfil;
        String sql = "INSERT INTO DETALLE_VENTA VALUES(num_ven,cod_prod,can_ven,subt)";
        nfil = tblDatos.getRowCount();
        for (int i = 0; i < nfil; i++) {
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, txtNumero.getText());
                psd.setString(2, String.valueOf(tblDatos.getValueAt(i, 0)));
                psd.setString(3, txtCantidad.getText());
                psd.setString(4, String.valueOf(tblDatos.getValueAt(i, 5)));

                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se inserto correctamente", "INFORMACIÓN", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public String formatoFecha(JDateChooser fecha) {
        String formato = "yyyy-MM-dd";
        //Formato
        Date date = fecha.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(date);
    }

    public void modificar() {
        conexion cc = new conexion();
        Connection cn = (Connection) cc.conectar();
        String sql = "";
        sql = "UPDATE VENTAS SET fec_ven='" + formatoFecha(jdcFec_rea) + "', "
                + "tot_ven='" + Integer.valueOf(txtTotal.getText()) + "', "
                + "ci_cli='" + clientesCodigo(cbCliente.getSelectedItem().toString()) + "', "
                + "ci_caj='" + cajeroCodigo(cbCajero.getSelectedItem().toString()) + "' "
                + "WHERE num_ven='" + Integer.valueOf(txtNumero.getText()) + "'";
        try {
            PreparedStatement psd = (PreparedStatement) cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizo correctamente");
                cargarTabla();
            }
        } catch (Exception ex) {
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
        btnSalir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtBarra = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Fecha realizacion");

        jLabel2.setText("Cliente");

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
                            .addComponent(jdcFec_rea, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                            .addComponent(txtNumero))))
                .addContainerGap())
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
                .addGap(94, 94, 94))
        );

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1448435812_add_cross_new_plus_create.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1448435968_editor-floopy-dish-save-glyph.png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1448435952_update.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1448435988_close_square_black.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/1448436039_sign-out.png"))); // NOI18N
        btnSalir.setText("Salir");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDatos);

        jLabel9.setText("Producto");

        txtBarra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBarraKeyReleased(evt);
            }
        });

        jButton1.setText("Detalles Venta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Cantidad");

        jButton2.setText("REGUISTRAR COMPRA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(txtCantidad)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBarraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarraKeyReleased
        cargarTabla(txtBarra.getText());
    }//GEN-LAST:event_txtBarraKeyReleased

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        conexion op = new conexion();
        op.conectar();
        desbloquear();
        desbloquearbotones();
        btnActualizar.setEnabled(false);
        btnNuevo.setEnabled(false);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarVentas();
        IngresarDetalles();


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        modificar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        bloquearbotones();
        limpiar();
        bloquear();
        btnNuevo.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Detalle_ventas d = new Detalle_ventas();
        Menu.jDesktopPane1.add(d);
        d.setVisible(true);
        d.show();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        cargarCroductoComprado();
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cbCajero;
    private javax.swing.JComboBox cbCliente;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcFec_rea;
    private javax.swing.JTable tblDatos;
    private javax.swing.JTextField txtBarra;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
public void limpiar() {
        txtNumero.setText("");
        ((JTextField) jdcFec_rea.getDateEditor().getUiComponent()).setText("");
        txtTotal.setText("");
//        cbCliente.setSelectedIndex(0);
//        cbCajero.setSelectedIndex(0);
    }

    private void bloquear() {
        txtNumero.setEnabled(false);
        jdcFec_rea.setEnabled(false);
        txtTotal.setEnabled(false);
        cbCliente.setEnabled(false);
        cbCajero.setEnabled(false);
    }

    private void bloquearbotones() {
        btnActualizar.setEnabled(false);
        btnGuardar.setEnabled(false);
    }

    private void desbloquear() {
        txtNumero.setEnabled(true);
        txtNumero.setText(String.valueOf(cargarNumero()));
        jdcFec_rea.setEnabled(true);
        txtTotal.setEnabled(true);
        cbCliente.setEnabled(true);
        cbCajero.setEnabled(true);
    }

    private void desbloquearbotones() {
        btnActualizar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(true);
    }

    private void desbloquearbotonesActualizar() {
        btnNuevo.setEnabled(true);
        btnActualizar.setEnabled(true);

    }

}
