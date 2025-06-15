package formAdmin;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import konektor.koneksi;
import javax.swing.event.ListSelectionEvent;

public class formDataRasa extends javax.swing.JPanel {

    public formDataRasa() {
        initComponents();
        loadDataProduk();

//      Menonaktifkan tombol Edit dan Delete saat pertama kali dibuka
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

//      Menambahkan ListSelectionListener ke tabel
        tabProduk.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            // Cek jika ada baris yang dipilih
            if (!event.getValueIsAdjusting() && tabProduk.getSelectedRow() != -1) {
                btnEdit.setEnabled(true);
                btnHapus.setEnabled(true);
            } else {
                btnEdit.setEnabled(false);
                btnHapus.setEnabled(false);
            }
        });
    }

//  Fungsi loadDataProduk //
    private void loadDataProduk() {
        DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT id_rasa, rasa FROM rasa";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String idProduk = rs.getString("id_rasa");
                String namaProduk = rs.getString("rasa");
                // Tambahkan data ke model tabel
                model.addRow(new Object[]{idProduk, namaProduk});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//  Fungsi loadDataProduk END //  
    
//  Fungsi deleteProduk //
    private void hapusDataProduk() {
        int selectedRow = tabProduk.getSelectedRow();
        if (selectedRow != -1) {
            String idProduk = tabProduk.getValueAt(selectedRow, 0).toString();
            String namaRasa = tabProduk.getValueAt(selectedRow, 1).toString();
            // Konfirmasi dan hapus data dari database berdasarkan ID
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus "+ namaRasa + " ?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = koneksi.getConnection();
                    Statement st = con.createStatement();
                    String deleteQuery = "DELETE FROM rasa WHERE id_rasa = '" + idProduk + "'";
                    st.executeUpdate(deleteQuery);
                    loadDataProduk();
                    JOptionPane.showMessageDialog(this, "produk berhasil dihapus.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
                }
            }
        }
    }
//  Fungsi deleteProduk END //
    
//  Fungsi cariProduk //
    private void searchProduk() {
        String keyword = inputSearching.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();

        if (keyword.isEmpty()) {
            loadDataProduk();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();

            // Query untuk mencari data user berdasarkan ID, Nama, atau Email yang cocok dengan kata kunci
            String query = "SELECT id_rasa, rasa "
                    + "FROM rasa WHERE id_rasa LIKE '%" + keyword + "%' "
                    + "OR rasa LIKE '%" + keyword + "%' ";

            ResultSet rs = st.executeQuery(query);

            // Jika data ditemukan, tambahkan ke model tabel
            while (rs.next()) {
                String idRasa = rs.getString("id_rasa");
                String namaRasa = rs.getString("rasa");
                model.addRow(new Object[]{idRasa, namaRasa});
            }

            // Pesan jika tidak ada data ditemukan
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data Rasa tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                loadDataProduk();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data.");
        }
    }
//  Fungsi cariProduk END //  
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FormTambahRasa = new javax.swing.JDialog();
        jSeparator1 = new javax.swing.JSeparator();
        lblTambahRasa = new javax.swing.JLabel();
        lblIdRasa = new javax.swing.JLabel();
        txtIdRasa = new javax.swing.JTextField();
        txtTambahRasa = new javax.swing.JTextField();
        lblRasa = new javax.swing.JLabel();
        btnSaveAdd = new javax.swing.JButton();
        btnCancelAdd = new javax.swing.JButton();
        formEditRasa = new javax.swing.JDialog();
        lblRasa1 = new javax.swing.JLabel();
        btnSaveEdit = new javax.swing.JButton();
        btnCancelEdit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        lblEditRasa = new javax.swing.JLabel();
        lblIdRasa1 = new javax.swing.JLabel();
        txtIdEditRasa = new javax.swing.JTextField();
        txtEditRasa = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        inputSearching = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProduk = new javax.swing.JTable();
        btnHapus1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        lblTambahRasa.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTambahRasa.setText("Tambah Rasa");

        lblIdRasa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdRasa.setText("Id Rasa");

        lblRasa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRasa.setText("Rasa");

        btnSaveAdd.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAdd.setText("Save");
        btnSaveAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddActionPerformed(evt);
            }
        });

        btnCancelAdd.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelAdd.setText("Cancel");
        btnCancelAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FormTambahRasaLayout = new javax.swing.GroupLayout(FormTambahRasa.getContentPane());
        FormTambahRasa.getContentPane().setLayout(FormTambahRasaLayout);
        FormTambahRasaLayout.setHorizontalGroup(
            FormTambahRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FormTambahRasaLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(lblTambahRasa)
                .addGap(63, 63, 63))
            .addGroup(FormTambahRasaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FormTambahRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIdRasa)
                    .addComponent(txtTambahRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRasa)
                    .addGroup(FormTambahRasaLayout.createSequentialGroup()
                        .addComponent(btnSaveAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelAdd)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        FormTambahRasaLayout.setVerticalGroup(
            FormTambahRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FormTambahRasaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTambahRasa)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdRasa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRasa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTambahRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FormTambahRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveAdd)
                    .addComponent(btnCancelAdd))
                .addGap(19, 19, 19))
        );

        lblRasa1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRasa1.setText("Rasa");

        btnSaveEdit.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveEdit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveEdit.setText("Save");
        btnSaveEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveEditActionPerformed(evt);
            }
        });

        btnCancelEdit.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelEdit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelEdit.setText("Cancel");
        btnCancelEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelEditActionPerformed(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        lblEditRasa.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblEditRasa.setText("Edit Rasa");

        lblIdRasa1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdRasa1.setText("Id Rasa");

        javax.swing.GroupLayout formEditRasaLayout = new javax.swing.GroupLayout(formEditRasa.getContentPane());
        formEditRasa.getContentPane().setLayout(formEditRasaLayout);
        formEditRasaLayout.setHorizontalGroup(
            formEditRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditRasaLayout.createSequentialGroup()
                .addContainerGap(88, Short.MAX_VALUE)
                .addComponent(lblEditRasa)
                .addGap(88, 88, 88))
            .addGroup(formEditRasaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formEditRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdEditRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIdRasa1)
                    .addComponent(txtEditRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRasa1)
                    .addGroup(formEditRasaLayout.createSequentialGroup()
                        .addComponent(btnSaveEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelEdit)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formEditRasaLayout.setVerticalGroup(
            formEditRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditRasaLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(lblEditRasa)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdRasa1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdEditRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRasa1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEditRasa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(formEditRasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveEdit)
                    .addComponent(btnCancelEdit))
                .addGap(19, 19, 19))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnTambah.setBackground(new java.awt.Color(255, 153, 0));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 153, 0));
        btnEdit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(255, 153, 0));
        btnHapus.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        inputSearching.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSearchingActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(255, 153, 0));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tabProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id Rasa", "Rasa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabProduk);

        btnHapus1.setBackground(new java.awt.Color(255, 153, 0));
        btnHapus1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus1.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus1.setText("Batal");
        btnHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapus1ActionPerformed(evt);
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
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                        .addComponent(inputSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputSearching, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Data Rasa");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // Kosongkan field input
        txtIdRasa.setText("");
        txtTambahRasa.setText("");
        FormTambahRasa.pack();
        FormTambahRasa.setLocationRelativeTo(this);
        FormTambahRasa.setModal(true);
        FormTambahRasa.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int selectedRow = tabProduk.getSelectedRow();
        if (selectedRow != -1) {
            String IdRasa = tabProduk.getValueAt(selectedRow, 0).toString();
            String Rasa = tabProduk.getValueAt(selectedRow, 1).toString();
            txtIdEditRasa.setText(IdRasa);
            txtEditRasa.setText(Rasa);
            formEditRasa.pack();
            formEditRasa.setLocationRelativeTo(this);
            formEditRasa.setModal(true);
            formEditRasa.setVisible(true);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusDataProduk();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchProduk();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void inputSearchingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSearchingActionPerformed
        searchProduk();
    }//GEN-LAST:event_inputSearchingActionPerformed

    private void btnSaveAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddActionPerformed
        String idRasa = txtIdRasa.getText().trim();
        String namaRasa = txtTambahRasa.getText().trim();

        if (idRasa.isEmpty() || namaRasa.isEmpty()) {
            JOptionPane.showMessageDialog(FormTambahRasa, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String insertQuery = "INSERT INTO rasa (id_rasa, rasa) VALUES ('" + idRasa + "', '" + namaRasa + "')";
            st.executeUpdate(insertQuery);

            FormTambahRasa.dispose();
            loadDataProduk();
            JOptionPane.showMessageDialog(this, "Data rasa berhasil ditambahkan.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(FormTambahRasa, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveAddActionPerformed

    private void btnSaveEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveEditActionPerformed
        String idRasa = txtIdEditRasa.getText().trim();
        String namaRasa = txtEditRasa.getText().trim();

        if (namaRasa.isEmpty()) {
            JOptionPane.showMessageDialog(formEditRasa, "Kolom Rasa harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection con = null;
        Statement st = null;
        try {
            con = koneksi.getConnection();
            st = con.createStatement();
            String updateQuery = "UPDATE rasa SET rasa = '" + namaRasa + "' WHERE id_rasa = '" + idRasa + "'";
            st.executeUpdate(updateQuery);

            formEditRasa.dispose();
            loadDataProduk();
            JOptionPane.showMessageDialog(this, "Data rasa berhasil diperbarui.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(formEditRasa, "Gagal memperbarui data: " + e.getMessage());
        } 
    }//GEN-LAST:event_btnSaveEditActionPerformed

    private void btnCancelEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelEditActionPerformed
        formEditRasa.dispose();
    }//GEN-LAST:event_btnCancelEditActionPerformed

    private void btnCancelAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAddActionPerformed
        FormTambahRasa.dispose();
    }//GEN-LAST:event_btnCancelAddActionPerformed

    private void btnHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus1ActionPerformed
        loadDataProduk();
    }//GEN-LAST:event_btnHapus1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog FormTambahRasa;
    private javax.swing.JButton btnCancelAdd;
    private javax.swing.JButton btnCancelEdit;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnSaveAdd;
    private javax.swing.JButton btnSaveEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JDialog formEditRasa;
    private javax.swing.JTextField inputSearching;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblEditRasa;
    private javax.swing.JLabel lblIdRasa;
    private javax.swing.JLabel lblIdRasa1;
    private javax.swing.JLabel lblRasa;
    private javax.swing.JLabel lblRasa1;
    private javax.swing.JLabel lblTambahRasa;
    private javax.swing.JTable tabProduk;
    private javax.swing.JTextField txtEditRasa;
    private javax.swing.JTextField txtIdEditRasa;
    private javax.swing.JTextField txtIdRasa;
    private javax.swing.JTextField txtTambahRasa;
    // End of variables declaration//GEN-END:variables
}
