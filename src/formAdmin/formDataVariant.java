/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formAdmin;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import konektor.koneksi;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author muqta
 */
public class formDataVariant extends javax.swing.JPanel {

    public formDataVariant() {
        initComponents();
        loadDataVarian();

//      Menonaktifkan tombol Edit dan Delete saat pertama kali dibuka
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

//      Menambahkan ListSelectionListener ke tabel
        tabVarian.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Cek jika ada baris yang dipilih
                if (!event.getValueIsAdjusting() && tabVarian.getSelectedRow() != -1) {
                    btnEdit.setEnabled(true);
                    btnHapus.setEnabled(true);
                } else {
                    btnEdit.setEnabled(false);
                    btnHapus.setEnabled(false);
                }
            }
        });
    }

// Load Data User //
    private void loadDataVarian() {
        DefaultTableModel model = (DefaultTableModel) tabVarian.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            java.sql.Statement st = con.createStatement();
            String query = "SELECT id_varian, varian, harga FROM varian";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String idVarian = rs.getString("id_varian");
                String varian = rs.getString("varian");
                String harga = rs.getString("harga");

                // Tambahkan data ke model tabel
                model.addRow(new Object[]{idVarian, varian, harga});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//  Load Data User end //      

//  hapus data user //    
    private void hapusDataVarian() {
        int selectedRow = tabVarian.getSelectedRow();
        if (selectedRow != -1) {
            String idUser = tabVarian.getValueAt(selectedRow, 0).toString();
            String varian = tabVarian.getValueAt(selectedRow, 1).toString();
            // Konfirmasi dan hapus data dari database berdasarkan ID
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus " + varian + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = koneksi.getConnection();
                    java.sql.Statement st = con.createStatement();
                    String deleteQuery = "DELETE FROM varian WHERE id_varian = '" + idUser + "'";
                    st.executeUpdate(deleteQuery);
                    loadDataVarian();
                    JOptionPane.showMessageDialog(this, varian + " berhasil dihapus.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
                }
            }
        }
    }
//  ungsi hapus data user end //   

//  Fungsi seacrh user //
    private void searchVarian() {
        String keyword = jTextField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabVarian.getModel();

        if (keyword.isEmpty()) {
            // Jika keyword kosong, tampilkan pesan dan muat ulang data asli
            loadDataVarian();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset tabel sebelum menampilkan hasil pencarian
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            java.sql.Statement st = con.createStatement();

            String query = "SELECT id_varian , varian, harga"
                    + "FROM varian WHERE id_varian LIKE '%" + keyword + "%' ";

            ResultSet rs = st.executeQuery(query);

            // Jika data ditemukan, tambahkan ke model tabel
            while (rs.next()) {
                String idVarian = rs.getString("id_varian");
                String varian = rs.getString("varian");
                String harga = rs.getString("harga");
                model.addRow(new Object[]{idVarian, varian, harga});
            }

            // Pesan jika tidak ada data ditemukan
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data varian tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                loadDataVarian(); // Tampilkan kembali data asli jika tidak ada hasil
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data.");
        }
    }
//  search user//    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formTambahVarian = new javax.swing.JDialog();
        jSeparator1 = new javax.swing.JSeparator();
        lblTambahRasa = new javax.swing.JLabel();
        lblIdVarian = new javax.swing.JLabel();
        txtIdVarian = new javax.swing.JTextField();
        txtVarian = new javax.swing.JTextField();
        lblVarian = new javax.swing.JLabel();
        btnSaveAddVarian = new javax.swing.JButton();
        btnCancelAddVarian = new javax.swing.JButton();
        txtHarga = new javax.swing.JTextField();
        lblHarga = new javax.swing.JLabel();
        formEditVarian = new javax.swing.JDialog();
        jSeparator2 = new javax.swing.JSeparator();
        lblTambahRasa1 = new javax.swing.JLabel();
        lblIdVarian1 = new javax.swing.JLabel();
        txtIdVarian1 = new javax.swing.JTextField();
        txtVarian1 = new javax.swing.JTextField();
        lblVarian1 = new javax.swing.JLabel();
        btnSaveAddVarian1 = new javax.swing.JButton();
        btnCancelAddVarian1 = new javax.swing.JButton();
        txtHarga1 = new javax.swing.JTextField();
        lblHarga1 = new javax.swing.JLabel();
        lbDataUser = new javax.swing.JLabel();
        bgDataUSer = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabVarian = new javax.swing.JTable();

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        lblTambahRasa.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTambahRasa.setText("Tambah Varian");

        lblIdVarian.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdVarian.setText("Id Varian");

        lblVarian.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblVarian.setText("Varian");

        btnSaveAddVarian.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveAddVarian.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveAddVarian.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAddVarian.setText("Save");
        btnSaveAddVarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddVarianActionPerformed(evt);
            }
        });

        btnCancelAddVarian.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelAddVarian.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelAddVarian.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelAddVarian.setText("Cancel");
        btnCancelAddVarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddVarianActionPerformed(evt);
            }
        });

        lblHarga.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblHarga.setText("Harga");

        javax.swing.GroupLayout formTambahVarianLayout = new javax.swing.GroupLayout(formTambahVarian.getContentPane());
        formTambahVarian.getContentPane().setLayout(formTambahVarianLayout);
        formTambahVarianLayout.setHorizontalGroup(
            formTambahVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formTambahVarianLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(lblTambahRasa)
                .addGap(63, 63, 63))
            .addGroup(formTambahVarianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formTambahVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIdVarian)
                    .addComponent(txtVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVarian)
                    .addGroup(formTambahVarianLayout.createSequentialGroup()
                        .addComponent(btnSaveAddVarian)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelAddVarian))
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHarga))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formTambahVarianLayout.setVerticalGroup(
            formTambahVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formTambahVarianLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(lblTambahRasa)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdVarian)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblVarian)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formTambahVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveAddVarian)
                    .addComponent(btnCancelAddVarian))
                .addGap(17, 17, 17))
        );

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        lblTambahRasa1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTambahRasa1.setText("Edit Varian");

        lblIdVarian1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdVarian1.setText("Id Varian");

        lblVarian1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblVarian1.setText("Varian");

        btnSaveAddVarian1.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveAddVarian1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveAddVarian1.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAddVarian1.setText("Save");
        btnSaveAddVarian1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddVarian1ActionPerformed(evt);
            }
        });

        btnCancelAddVarian1.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelAddVarian1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelAddVarian1.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelAddVarian1.setText("Cancel");
        btnCancelAddVarian1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddVarian1ActionPerformed(evt);
            }
        });

        lblHarga1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblHarga1.setText("Harga");

        javax.swing.GroupLayout formEditVarianLayout = new javax.swing.GroupLayout(formEditVarian.getContentPane());
        formEditVarian.getContentPane().setLayout(formEditVarianLayout);
        formEditVarianLayout.setHorizontalGroup(
            formEditVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditVarianLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(lblTambahRasa1)
                .addGap(63, 63, 63))
            .addGroup(formEditVarianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formEditVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdVarian1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIdVarian1)
                    .addComponent(txtVarian1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVarian1)
                    .addGroup(formEditVarianLayout.createSequentialGroup()
                        .addComponent(btnSaveAddVarian1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelAddVarian1))
                    .addComponent(txtHarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHarga1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formEditVarianLayout.setVerticalGroup(
            formEditVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditVarianLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTambahRasa1)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdVarian1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdVarian1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblVarian1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtVarian1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblHarga1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(formEditVarianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveAddVarian1)
                    .addComponent(btnCancelAddVarian1))
                .addGap(17, 17, 17))
        );

        lbDataUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbDataUser.setText("Data Varian");

        bgDataUSer.setBackground(new java.awt.Color(255, 255, 255));
        bgDataUSer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnTambah.setBackground(new java.awt.Color(51, 51, 255));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(51, 51, 255));
        btnEdit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(51, 51, 255));
        btnHapus.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(51, 51, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tabVarian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id Varian", "Varian", "Harga"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabVarian.setGridColor(new java.awt.Color(0, 0, 0));
        tabVarian.setSelectionBackground(new java.awt.Color(51, 51, 255));
        tabVarian.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tabVarian.setShowGrid(false);
        jScrollPane1.setViewportView(tabVarian);

        javax.swing.GroupLayout bgDataUSerLayout = new javax.swing.GroupLayout(bgDataUSer);
        bgDataUSer.setLayout(bgDataUSerLayout);
        bgDataUSerLayout.setHorizontalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37))))
        );
        bgDataUSerLayout.setVerticalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bgDataUSer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbDataUser)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbDataUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bgDataUSer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        txtIdVarian.setText("");
        txtVarian.setText("");
        txtHarga.setText("");
        formTambahVarian.pack();
        formTambahVarian.setLocationRelativeTo(this);
        formTambahVarian.setModal(true);
        formTambahVarian.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int selectedRows = tabVarian.getSelectedRow();
        if (selectedRows != -1) {
            String IdVarian = tabVarian.getValueAt(selectedRows, 0).toString();
            String Varian = tabVarian.getValueAt(selectedRows, 1).toString();
            String Harga = tabVarian.getValueAt(selectedRows, 2).toString();

            txtIdVarian1.setText(IdVarian);
            txtVarian1.setText(Varian);
            txtHarga1.setText(Harga);
            formEditVarian.pack();
            formEditVarian.setLocationRelativeTo(this);
            formEditVarian.setModal(true);
            formEditVarian.setVisible(true);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusDataVarian();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchVarian();
        btnSearch.requestFocus();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchVarian();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnSaveAddVarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddVarianActionPerformed
        String idVarian = txtIdVarian.getText().trim();
        String varian = txtVarian.getText().trim();
        String harga = txtHarga.getText().trim();

        if (idVarian.isEmpty() || varian.isEmpty() || harga.isEmpty()) {
            JOptionPane.showMessageDialog(formTambahVarian, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String insertQuery = "INSERT INTO varian (id_varian, varian, harga) VALUES ('" + idVarian + "', '" + varian + "', '" + harga + "')";
            st.executeUpdate(insertQuery);

            formTambahVarian.dispose();
            loadDataVarian();
            JOptionPane.showMessageDialog(this, varian + " berhasil ditambahkan.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(formTambahVarian, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveAddVarianActionPerformed

    private void btnCancelAddVarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAddVarianActionPerformed
        formTambahVarian.dispose();
    }//GEN-LAST:event_btnCancelAddVarianActionPerformed

    private void btnSaveAddVarian1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddVarian1ActionPerformed
        String idEditVarian = txtIdVarian1.getText().trim();
        String editVarian = txtVarian1.getText().trim();
        String editHarga = txtHarga1.getText().trim();

        if (idEditVarian.isEmpty() || editVarian.isEmpty() || editHarga.isEmpty()) {
            JOptionPane.showMessageDialog(formEditVarian, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String insertQuery = "INSERT INTO varian (id_varian, varian, harga) VALUES ('" + idEditVarian + "', '" + editVarian + "', '" + editHarga + "')";
            st.executeUpdate(insertQuery);

            formTambahVarian.dispose();
            loadDataVarian();
            JOptionPane.showMessageDialog(this, editVarian + " berhasil di update.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(formTambahVarian, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveAddVarian1ActionPerformed

    private void btnCancelAddVarian1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAddVarian1ActionPerformed
        formEditVarian.dispose();
    }//GEN-LAST:event_btnCancelAddVarian1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgDataUSer;
    private javax.swing.JButton btnCancelAddVarian;
    private javax.swing.JButton btnCancelAddVarian1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSaveAddVarian;
    private javax.swing.JButton btnSaveAddVarian1;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JDialog formEditVarian;
    private javax.swing.JDialog formTambahVarian;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lbDataUser;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblHarga1;
    private javax.swing.JLabel lblIdVarian;
    private javax.swing.JLabel lblIdVarian1;
    private javax.swing.JLabel lblTambahRasa;
    private javax.swing.JLabel lblTambahRasa1;
    private javax.swing.JLabel lblVarian;
    private javax.swing.JLabel lblVarian1;
    private javax.swing.JTable tabVarian;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtHarga1;
    private javax.swing.JTextField txtIdVarian;
    private javax.swing.JTextField txtIdVarian1;
    private javax.swing.JTextField txtVarian;
    private javax.swing.JTextField txtVarian1;
    // End of variables declaration//GEN-END:variables
}
