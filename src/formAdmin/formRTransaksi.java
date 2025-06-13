package formAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;

/**
 *
 * @author muqta
 */
public class formRTransaksi extends javax.swing.JPanel {

    /**
     * Creates new form formTransaksi
     */
    public formRTransaksi() {
        initComponents();
        historyTransaction();
        loadKasir();
        lbldatetime.setEditable(false);
        txtSubtotal.setEnabled(false);
        txtTotalBayar.setEnabled(false);
        txtKembalian.setEnabled(false);
        btnDetailTransaksi.setEnabled(false);

        tabRiwayat.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && tabRiwayat.getSelectedRow() != -1) {
                    btnDetailTransaksi.setEnabled(true);

                } else {
                    btnDetailTransaksi.setEnabled(false);

                }
            }
        });
    }

// Riwayat Transaksi //
    private void historyTransaction() {
        DefaultTableModel tbl = (DefaultTableModel) tabRiwayat.getModel();
        tbl.setRowCount(0); // Clear tabel lama

        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT "
                    + "t.id_transaksi, "
                    + "t.tanggal, "
                    + "u.username, "
                    + "SUM(dt.jumlah) AS jumlah_item, "
                    + "SUM(dt.harga_produk) AS harga_produk, "
                    + "SUM(dt.harga_produk * dt.jumlah) AS subtotal "
                    + "FROM transaksi t "
                    + "JOIN user u ON t.id_user = u.id_user "
                    + "JOIN transaksi_detail dt ON t.id_transaksi = dt.id_transaksi "
                    + "GROUP BY t.id_transaksi, t.tanggal, u.username "
                    + "ORDER BY t.tanggal DESC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat tampilFormat = new SimpleDateFormat("dd-MMMM-yyyy");

            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String tanggal = rs.getString("tanggal");
                String namaKasir = rs.getString("username");
                int jumlahItem = rs.getInt("jumlah_item");
                double totalHarga = rs.getDouble("harga_produk");
                double subTotal = rs.getDouble("subtotal");

                // Format tanggal
                try {
                    tanggal = tampilFormat.format(dbFormat.parse(tanggal));
                } catch (Exception e) {
                    tanggal = "Invalid Date";
                }

                Object[] row = {
                    idTransaksi,
                    tanggal,
                    namaKasir,
                    jumlahItem,
                    totalHarga,
                    subTotal
                };
                tbl.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat riwayat transaksi admin!\n" + e.getMessage());
        }
    }

// Riwayat Transaksi End // 
// Search History //
    private void searchHistory() {
        String keyword = jTextField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabRiwayat.getModel();

        if (keyword.isEmpty()) {
            historyTransaction();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setRowCount(0);

        String sql = "SELECT t.id_transaksi, t.tanggal, t.subtotal, t.total_bayar, t.kembalian,u.username, "
                + "td.nama_produk, td.harga_produk, td.jumlah, td.total "
                + "FROM transaksi t "
                + "JOIN transaksi_detail td ON t.id_transaksi = td.id_transaksi "
                + "JOIN user u ON t.id_user = u.id_user "
                + "WHERE t.id_transaksi LIKE ? OR u.username LIKE ? OR td.nama_produk LIKE ? "
                + "ORDER BY t.tanggal DESC";

        try (Connection conn = koneksi.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {

            // Mengganti tanda tanya dengan kata kunci pencarian
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);  // Pencarian berdasarkan id_transaksi
            pst.setString(2, searchPattern);  // Pencarian berdasarkan nama_user
            pst.setString(3, searchPattern);  // Pencarian berdasarkan nama_produk

            ResultSet rs = pst.executeQuery();

            // Menambahkan hasil pencarian ke dalam tabel
            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String tanggalTransaksi = rs.getString("tanggal_transaksi");
                String namaUser = rs.getString("nama_user");
                String namaProduk = rs.getString("nama_produk");
                double hargaProduk = rs.getDouble("harga_produk");
                int jumlah = rs.getInt("jumlah");
                double subtotal = rs.getDouble("subtotal");

                // Menambahkan baris hasil pencarian ke tabel
                Object[] row = {idTransaksi, tanggalTransaksi, namaUser, namaProduk,
                    hargaProduk, jumlah, subtotal};
                model.addRow(row);

                // Pesan jika tidak ada data ditemukan
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Data Transaksi tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                    historyTransaction(); // Tampilkan kembali data asli jika tidak ada hasil
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error searching transaction history: " + e.getMessage());
        }
    }

// Search History End //
// Memuat daftar nama kasir ke JComboBox
    private void loadKasir() {
        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT username FROM user WHERE role = 'kasir'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            pilihKasir.addItem("-- Pilih Kasir --");
            while (rs.next()) {
                pilihKasir.addItem(rs.getString("username"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading cashier list: " + e.getMessage());
        }
    }

    private void filterKasir() {
        String selectedKasir = pilihKasir.getSelectedItem().toString();
        DefaultTableModel model = (DefaultTableModel) tabRiwayat.getModel();
        model.setRowCount(0); // Hapus data pada tabel

        if (selectedKasir.equals("-- Pilih Kasir --")) {
            historyTransaction(); // Tampilkan semua transaksi jika "-- Pilih Kasir --" dipilih
            return;
        }

        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT "
                    + "t.id_transaksi, "
                    + "t.tanggal, "
                    + "u.username, "
                    + "SUM(dt.jumlah) AS jumlah_item, "
                    + "SUM(dt.harga_produk) AS harga_produk, "
                    + "SUM(dt.harga_produk * dt.jumlah) AS subtotal "
                    + "FROM transaksi t "
                    + "JOIN user u ON t.id_user = u.id_user "
                    + "JOIN transaksi_detail dt ON t.id_transaksi = dt.id_transaksi "
                    + "WHERE u.username = ? "
                    + "GROUP BY t.id_transaksi, t.tanggal, u.username "
                    + "ORDER BY t.tanggal DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, selectedKasir);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ubahFormat = new SimpleDateFormat("dd-MMMM-yyyy");

            while (rs.next()) {
                String idTransaksi = rs.getString("id_transaksi");
                String tanggal = rs.getString("tanggal");
                String namaKasir = rs.getString("username");
                int jumlahItem = rs.getInt("jumlah_item");
                double totalHarga = rs.getDouble("harga_produk");
                double subTotal = rs.getDouble("subtotal");

                // Format tanggal
                try {
                    tanggal = ubahFormat.format(dbFormat.parse(tanggal));
                } catch (Exception e) {
                    tanggal = "Invalid Date";
                }

                Object[] row = {
                    idTransaksi,
                    tanggal,
                    namaKasir,
                    jumlahItem,
                    totalHarga,
                    subTotal
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi: " + e.getMessage());
        }
    }

    private void loadTransactionDetails(String idTransaksi) {
        // Clear previous data in the table
        DefaultTableModel model = (DefaultTableModel) tblRiwayatDetail.getModel();
        model.setRowCount(0);

        try {
            Connection conn = koneksi.getConnection();
            // Query to fetch transaction details
            String sql = "SELECT t.id_transaksi, t.tanggal, t.subtotal, t.total_bayar, t.kembalian, u.username, "
                    + "td.nama_produk, td.harga_produk, td.jumlah, td.total "
                    + "FROM transaksi t "
                    + "JOIN transaksi_detail td ON t.id_transaksi = td.id_transaksi "
                    + "JOIN user u ON t.id_user = u.id_user "
                    + "WHERE t.id_transaksi = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, idTransaksi);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Adjusted format to include time
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");

            // Variables to store transaction-level data
            String tanggal = "";
            double subTotal = 0.0;
            double totalBayar = 0.0;
            double kembalian = 0.0;
            boolean hasData = false; // Flag to check if data exists

            while (rs.next()) {
                hasData = true; // Data exists, set flag

                // Get transaction-level data (only once, assuming consistent data)
                if (tanggal.isEmpty()) {
                    tanggal = rs.getString("tanggal");
                    subTotal = rs.getDouble("subtotal");
                    totalBayar = rs.getDouble("total_bayar");
                    kembalian = rs.getDouble("kembalian");

                    // Format the date
                    try {
                        tanggal = displayFormat.format(dbFormat.parse(tanggal));
                    } catch (Exception e) {
                        tanggal = "Invalid Date";
                    }

                    // Set text fields in the GUI
                    lbldatetime.setText(tanggal);
                    txtSubtotal.setText(String.format("Rp %.2f", subTotal));
                    txtTotalBayar.setText(String.format("Rp %.2f", totalBayar));
                    txtKembalian.setText(String.format("Rp %.2f", kembalian));
                }

                // Get product details
                String namaKasir = rs.getString("username");
                String namaProduk = rs.getString("nama_produk");
                int jumlah = rs.getInt("jumlah");
                double hargaProduk = rs.getDouble("harga_produk");
                double totalHarga = rs.getDouble("total");

                // Add row to the product table
                Object[] row = {namaKasir, namaProduk, jumlah, hargaProduk, totalHarga};
                model.addRow(row);
            }

            // If no data is found, show a message and do not display the dialog
            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No details found for transaction ID: " + idTransaksi,
                        "Information", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit the method without showing the dialog
            }

            // Show the dialog with the transaction details
            formDetailTransaksi.pack();
            formDetailTransaksi.setLocationRelativeTo(this);
            formDetailTransaksi.setModal(true);
            formDetailTransaksi.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading transaction details: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formDetailTransaksi = new javax.swing.JDialog();
        pnHead = new javax.swing.JPanel();
        pnHeader = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        pnBG = new javax.swing.JPanel();
        lbldatetime = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRiwayatDetail = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txtKembalian = new javax.swing.JTextField();
        txtTotalBayar = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        lblSubtotal = new javax.swing.JLabel();
        lblTotalBayar = new javax.swing.JLabel();
        lblKembalian = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabRiwayat = new javax.swing.JTable();
        pilihKasir = new javax.swing.JComboBox<>();
        btnDetailTransaksi = new javax.swing.JButton();
        btnCancelDetails = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        pnHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnHead.setPreferredSize(new java.awt.Dimension(304, 80));

        pnHeader.setBackground(new java.awt.Color(255, 255, 255));
        pnHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTambahProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTambahProduk.setText("Detail Produk Transaksi");

        javax.swing.GroupLayout pnHeaderLayout = new javax.swing.GroupLayout(pnHeader);
        pnHeader.setLayout(pnHeaderLayout);
        pnHeaderLayout.setHorizontalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnHeaderLayout.setVerticalGroup(
            pnHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeaderLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTambahProduk)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnHeadLayout = new javax.swing.GroupLayout(pnHead);
        pnHead.setLayout(pnHeadLayout);
        pnHeadLayout.setHorizontalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnHeadLayout.setVerticalGroup(
            pnHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHeadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnBG.setBackground(new java.awt.Color(255, 255, 255));
        pnBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbldatetime.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lbldatetime.setText("Tanggal & waktu");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Detail Pembelian", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Geometr212 BkCn BT", 0, 12))); // NOI18N

        tblRiwayatDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "nama kasir", "nama produk", "jumlah", "harga", "total harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblRiwayatDetail);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Detail Pembayaran", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Geometr212 BkCn BT", 0, 12))); // NOI18N

        txtKembalian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtTotalBayar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtTotalBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalBayarActionPerformed(evt);
            }
        });

        txtSubtotal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtSubtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubtotalActionPerformed(evt);
            }
        });

        lblSubtotal.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        lblSubtotal.setText("Subtotal");

        lblTotalBayar.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        lblTotalBayar.setText("Total Bayar");

        lblKembalian.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        lblKembalian.setText("Kembalian");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(80, 80, 80)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnBGLayout = new javax.swing.GroupLayout(pnBG);
        pnBG.setLayout(pnBGLayout);
        pnBGLayout.setHorizontalGroup(
            pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBGLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(10, 10, 10))
            .addGroup(pnBGLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnBGLayout.createSequentialGroup()
                        .addComponent(lbldatetime, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnBGLayout.setVerticalGroup(
            pnBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBGLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel5)
                .addGap(46, 46, 46)
                .addComponent(lbldatetime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnBG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnBG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formDetailTransaksiLayout = new javax.swing.GroupLayout(formDetailTransaksi.getContentPane());
        formDetailTransaksi.getContentPane().setLayout(formDetailTransaksiLayout);
        formDetailTransaksiLayout.setHorizontalGroup(
            formDetailTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 821, Short.MAX_VALUE)
            .addGroup(formDetailTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(formDetailTransaksiLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(formDetailTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnHead, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        formDetailTransaksiLayout.setVerticalGroup(
            formDetailTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
            .addGroup(formDetailTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(formDetailTransaksiLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(pnHead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tabRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Id Transaksi", "Tanggal Transaksi", "Nama Kasir", "Jumlah item", "Harga", "SubTotal"
            }
        ));
        jScrollPane1.setViewportView(tabRiwayat);

        pilihKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihKasirActionPerformed(evt);
            }
        });

        btnDetailTransaksi.setBackground(new java.awt.Color(0, 0, 255));
        btnDetailTransaksi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnDetailTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        btnDetailTransaksi.setText("Detail");
        btnDetailTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailTransaksiActionPerformed(evt);
            }
        });

        btnCancelDetails.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelDetails.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnCancelDetails.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelDetails.setText("Batal");
        btnCancelDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelDetailsActionPerformed(evt);
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
                        .addComponent(pilihKasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDetailTransaksi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pilihKasir)
                        .addComponent(btnDetailTransaksi)
                        .addComponent(btnCancelDetails)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel2.setText("Riwayat Transaksi");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(192, 192, 192)
                .addComponent(jLabel2)
                .addContainerGap(190, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchHistory();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchHistory();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void pilihKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihKasirActionPerformed
        filterKasir();
    }//GEN-LAST:event_pilihKasirActionPerformed

    private void btnDetailTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailTransaksiActionPerformed
        int selectedRows = tabRiwayat.getSelectedRow();
        if (selectedRows != -1) {
            String idTransaksi = tabRiwayat.getValueAt(selectedRows, 0).toString();
            loadTransactionDetails(idTransaksi);
            formDetailTransaksi.pack();
            formDetailTransaksi.setLocationRelativeTo(this);
            formDetailTransaksi.setModal(true);
            formDetailTransaksi.setVisible(true);
        }
    }//GEN-LAST:event_btnDetailTransaksiActionPerformed

    private void btnCancelDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelDetailsActionPerformed
//        historyTransaction(userId);
    }//GEN-LAST:event_btnCancelDetailsActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void txtTotalBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalBayarActionPerformed

    private void txtSubtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubtotalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelDetails;
    private javax.swing.JButton btnDetailTransaksi;
    private javax.swing.JDialog formDetailTransaksi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblKembalian;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JLabel lblTotalBayar;
    private javax.swing.JTextField lbldatetime;
    private javax.swing.JComboBox<String> pilihKasir;
    private javax.swing.JPanel pnBG;
    private javax.swing.JPanel pnHead;
    private javax.swing.JPanel pnHeader;
    private javax.swing.JTable tabRiwayat;
    private javax.swing.JTable tblRiwayatDetail;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotalBayar;
    // End of variables declaration//GEN-END:variables
}
