package formKasir;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;

public class formRiwayatTransaksi extends javax.swing.JPanel {

    private String userId;

    public formRiwayatTransaksi(String userId) {
        initComponents();
        this.userId = userId;
        historyTransaction(userId);
        btndetail.setEnabled(false);
       
      tblriwayat.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting() && tblriwayat.getSelectedRow() != -1) {
                btndetail.setEnabled(true);
               
            } else {
                btndetail.setEnabled(false);
                
            }
        }
        });
    }
    
    
// Riwayat Transaksi //
    private void historyTransaction(String userId){
        DefaultTableModel tbl = (DefaultTableModel) tblriwayat.getModel(); 
    tbl.setRowCount(0);

    try {
        Connection conn = koneksi.getConnection();
        String sql = "SELECT "
                + "t.id_transaksi, "
                + "t.tanggal, "
                + "t.total_bayar AS total, "
                + "SUM(td.jumlah) AS jumlah_item, "
                + "t.kembalian "
                + "FROM transaksi t "
                + "JOIN transaksi_detail td ON t.id_transaksi = td.id_transaksi "
                + "WHERE t.id_user = ? "
                + "GROUP BY t.id_transaksi, t.tanggal, t.total_bayar, t.kembalian "
                + "ORDER BY t.tanggal DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, userId);
        ResultSet rs = pst.executeQuery();

        // Format tanggal dari yyyy-MM-dd ke dd-MMMM-yyyy
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tampilFormat = new SimpleDateFormat("dd-MMMM-yyyy");

        while (rs.next()) {
            String idTransaksi = rs.getString("id_transaksi");
            String tanggal = rs.getString("tanggal");
            String total = rs.getString("total");
            int jumlah_item = rs.getInt("jumlah_item");
            double kembalian = rs.getDouble("kembalian");

            // Ubah format tanggal
            try {
                tanggal = tampilFormat.format(dbFormat.parse(tanggal));
            } catch (Exception e) {
                tanggal = "Invalid Date";
            }

            Object[] row = { idTransaksi, tanggal, total, jumlah_item, kembalian };
            tbl.addRow(row);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal memuat riwayat transaksi!\n" + e.getMessage());
    }
    }
// Riwayat Transaksi End // 
    
    
//    Gausah sii ini
// Search History //
//    private void searchHistory(){
//        String keyword = inputSeacrh.getText().trim();
//        DefaultTableModel model = (DefaultTableModel) tabRiwayat.getModel();
//
//        if (keyword.isEmpty()) {
//            historyTransaction(userId);
//            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        
//        model.setRowCount(0);
//
//        String sql = "SELECT t.id_transaksi, t.tanggal_transaksi, u.nama_user, "
//                      + "p.nama_produk, dt.harga_produk, dt.jumlah, dt.subtotal "
//                      + "FROM transaksi t "
//                      + "JOIN detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
//                      + "JOIN user u ON t.id_user = u.id_user "
//                      + "JOIN produk p ON dt.id_produk = p.id_produk "
//                      + "WHERE t.id_user LIKE ? OR t.id_transaksi LIKE ? OR u.nama_user LIKE ? OR p.nama_produk LIKE ? "
//                      + "ORDER BY t.tanggal_transaksi DESC";
//    
//        try (Connection conn = koneksi.getConnection();
//             PreparedStatement pst = conn.prepareStatement(sql)) {
//
//            // Mengganti tanda tanya dengan kata kunci pencarian
//            String searchPattern = "%" + keyword + "%";
//            pst.setString(1, searchPattern);  // Pencarian berdasarkan id_transaksi
//            pst.setString(2, searchPattern);  // Pencarian berdasarkan nama_user
//            pst.setString(3, searchPattern);  // Pencarian berdasarkan nama_produk
//
//            ResultSet rs = pst.executeQuery();
//
//            // Menambahkan hasil pencarian ke dalam tabel
//            while (rs.next()) {
//                String idTransaksi = rs.getString("id_transaksi");
//                String tanggalTransaksi = rs.getString("tanggal_transaksi");
//                String namaUser = rs.getString("nama_user");
//                String namaProduk = rs.getString("nama_produk");
//                double hargaProduk = rs.getDouble("harga_produk");
//                int jumlah = rs.getInt("jumlah");
//                double subtotal = rs.getDouble("subtotal");
//                
//                // Menambahkan baris hasil pencarian ke tabel
//                Object[] row = { idTransaksi, tanggalTransaksi, namaUser, namaProduk, 
//                                 hargaProduk, jumlah, subtotal };
//                model.addRow(row);
//                
//                            // Pesan jika tidak ada data ditemukan
//                if (model.getRowCount() == 0) {
//                    JOptionPane.showMessageDialog(this, "Data Transaksi tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
//                    historyTransaction(userId);
//                }    
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error searching transaction history: " + e.getMessage());
//        }
//    }
//    
// Search History End //
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        pnHeader = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblriwayat = new javax.swing.JTable();
        inputSeacrh6 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        btndetail = new javax.swing.JButton();
        btndetail1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTambahProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTambahProduk.setText("Detail Transaksi");

        javax.swing.GroupLayout pnHeaderLayout = new javax.swing.GroupLayout(pnHeader);
        pnHeader.setLayout(pnHeaderLayout);
        pnHeaderLayout.setHorizontalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnHeaderLayout.setVerticalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTambahProduk)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblriwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id Transaksi", "Tanggal Transaksi", "SubTotal", "Jumlah_Item", "Kembalian"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(tblriwayat);

        inputSeacrh6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSeacrh6inputSeacrhActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(51, 51, 255));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7jButton1ActionPerformed(evt);
            }
        });

        btndetail.setBackground(new java.awt.Color(0, 0, 255));
        btndetail.setFont(new java.awt.Font("Schadow BT", 0, 12)); // NOI18N
        btndetail.setForeground(new java.awt.Color(255, 255, 255));
        btndetail.setText("Detail");
        btndetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndetailActionPerformed(evt);
            }
        });

        btndetail1.setBackground(new java.awt.Color(0, 0, 255));
        btndetail1.setFont(new java.awt.Font("Schadow BT", 0, 12)); // NOI18N
        btndetail1.setForeground(new java.awt.Color(255, 255, 255));
        btndetail1.setText("Batal");
        btndetail1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndetail1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(btndetail)
                        .addGap(18, 18, 18)
                        .addComponent(btndetail1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputSeacrh6, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addGap(34, 34, 34)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputSeacrh6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btndetail)
                        .addComponent(btndetail1))
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel10, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void inputSeacrh6inputSeacrhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSeacrh6inputSeacrhActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputSeacrh6inputSeacrhActionPerformed

    private void jButton7jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7jButton1ActionPerformed

    private void btndetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndetailActionPerformed
        int selectedRow = tblriwayat.getSelectedRow();
        if (selectedRow != -1) {
            String idTransaksi = tblriwayat.getValueAt(selectedRow, 0).toString();
            System.out.println("ID Transaksi yang dikirim: " + idTransaksi); // Debugging

            DetailProduk detailproduk = new DetailProduk(idTransaksi);
            detailproduk.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!");
        }
    }//GEN-LAST:event_btndetailActionPerformed

    private void btndetail1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndetail1ActionPerformed
        historyTransaction(userId);
    }//GEN-LAST:event_btndetail1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btndetail;
    private javax.swing.JButton btndetail1;
    private javax.swing.JTextField inputSeacrh6;
    private javax.swing.JButton jButton7;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JTable tblriwayat;
    // End of variables declaration//GEN-END:variables
}
