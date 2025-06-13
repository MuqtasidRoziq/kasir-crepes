package formAdmin;
import java.awt.*;
import org.jfree.chart.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;
import org.jfree.data.category.DefaultCategoryDataset;

public class formPenjualan extends javax.swing.JPanel {

    private Connection connection;

    public formPenjualan() {
        initComponents();
        connection = konektor.koneksi.getConnection();
        loadData();
    }

    private void loadData() {
        try {
            // Query untuk total produk terjual
            String queryProdukTerjual = "SELECT SUM(jumlah) AS jumlah FROM transaksi_detail";
            try (PreparedStatement ps = connection.prepareStatement(queryProdukTerjual); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbProdukTerjual.setText(String.valueOf(rs.getInt("jumlah")));
                }
            }

            // Query untuk produk terlaris
            String queryProdukTerlaris = "SELECT nama_produk, SUM(jumlah) AS jumlah FROM transaksi_detail GROUP BY nama_produk ORDER BY jumlah DESC LIMIT 1;";
            try (PreparedStatement ps = connection.prepareStatement(queryProdukTerlaris); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbProdukTerlaris.setText(rs.getString("nama_produk"));
                }
            }

            GrafikPenjualan();
            GrafikPenjualanHariIni();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void GrafikPenjualan() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String query = "SELECT MONTH(tanggal) AS bulan, SUM(subtotal) AS total_penjualan FROM transaksi GROUP BY MONTH(tanggal)";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bulan = rs.getInt("bulan");
                    double total = rs.getDouble("total_penjualan");
                    String namaBulan = getNamaBulan(bulan);
                    dataset.addValue(total, "Penjualan", "Bulan " + namaBulan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart("", "Bulan", "", dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        pnGrafikPenjualan.removeAll();
        pnGrafikPenjualan.setLayout(new BorderLayout());
        pnGrafikPenjualan.add(chartPanel, BorderLayout.CENTER);
        pnGrafikPenjualan.revalidate();
        pnGrafikPenjualan.repaint();
    }

    private void GrafikPenjualanHariIni() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String query = "SELECT td.nama_produk, SUM(t.subtotal) AS total_penjualan "
                    + "FROM transaksi_detail td "
                    + "JOIN transaksi t ON td.id_transaksi = t.id_transaksi "
                    + "WHERE DATE(t.tanggal) = CURDATE() "
                    + "GROUP BY td.nama_produk "
                    + "ORDER BY total_penjualan DESC";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String namaProduk = rs.getString("nama_produk");
                    double total = rs.getDouble("total_penjualan");

                    dataset.addValue(total, "Penjualan Hari Ini", namaProduk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "", // Judul chart
                "Nama Produk", // Label X
                "Total Penjualan", // Label Y
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        pnProdukLaris.removeAll();
        pnProdukLaris.setLayout(new BorderLayout());
        pnProdukLaris.add(chartPanel, BorderLayout.CENTER);
        pnProdukLaris.revalidate();
        pnProdukLaris.repaint();
    }

    // Fungsi untuk mengubah bulan angka menjadi nama
    private String getNamaBulan(int bulan) {
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return namaBulan[bulan - 1];
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formProdukTerlaris = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProdukLaris = new javax.swing.JTable();
        formProdukTerjual = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabProduk1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnProdukTerjual = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbProdukTerjual = new javax.swing.JLabel();
        btnShowProdukTerjual = new javax.swing.JButton();
        pnProdukTerlaris = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbProdukTerlaris = new javax.swing.JLabel();
        btnShowProdukTerlaris = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        pnGrafikPenjualan = new javax.swing.JPanel();
        pnProdukLaris = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel2.setText("Produk Terlaris");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(216, 216, 216))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tabProdukLaris.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nama Produk", "Terjual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabProdukLaris);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout formProdukTerlarisLayout = new javax.swing.GroupLayout(formProdukTerlaris.getContentPane());
        formProdukTerlaris.getContentPane().setLayout(formProdukTerlarisLayout);
        formProdukTerlarisLayout.setHorizontalGroup(
            formProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(formProdukTerlarisLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        formProdukTerlarisLayout.setVerticalGroup(
            formProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        tabProduk1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tanggal Pembelian", "Nama Produk", "Terjual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabProduk1);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel6.setText("Produk Terjual");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(239, 239, 239)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel6)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formProdukTerjualLayout = new javax.swing.GroupLayout(formProdukTerjual.getContentPane());
        formProdukTerjual.getContentPane().setLayout(formProdukTerjualLayout);
        formProdukTerjualLayout.setHorizontalGroup(
            formProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        formProdukTerjualLayout.setVerticalGroup(
            formProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        pnProdukTerjual.setBackground(new java.awt.Color(153, 153, 255));
        pnProdukTerjual.setPreferredSize(new java.awt.Dimension(216, 96));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Produk Terjual");

        lbProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 36)); // NOI18N
        lbProdukTerjual.setText("0");

        btnShowProdukTerjual.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerjual.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerjual.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerjual.setText("detail");
        btnShowProdukTerjual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowProdukTerjualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnProdukTerjualLayout = new javax.swing.GroupLayout(pnProdukTerjual);
        pnProdukTerjual.setLayout(pnProdukTerjualLayout);
        pnProdukTerjualLayout.setHorizontalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnShowProdukTerjual))
                .addContainerGap(333, Short.MAX_VALUE))
        );
        pnProdukTerjualLayout.setVerticalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbProdukTerjual, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowProdukTerjual)
                .addContainerGap())
        );

        pnProdukTerlaris.setBackground(new java.awt.Color(153, 153, 255));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Produk Terlaris");

        lbProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lbProdukTerlaris.setText("Jenis Rasa");

        btnShowProdukTerlaris.setBackground(new java.awt.Color(51, 51, 255));
        btnShowProdukTerlaris.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnShowProdukTerlaris.setForeground(new java.awt.Color(255, 255, 255));
        btnShowProdukTerlaris.setText("detail");
        btnShowProdukTerlaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowProdukTerlarisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnProdukTerlarisLayout = new javax.swing.GroupLayout(pnProdukTerlaris);
        pnProdukTerlaris.setLayout(pnProdukTerlarisLayout);
        pnProdukTerlarisLayout.setHorizontalGroup(
            pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(btnShowProdukTerlaris)
                    .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnProdukTerlarisLayout.setVerticalGroup(
            pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowProdukTerlaris)
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel8.setText("Grafik Penjualan Perbulan");

        pnGrafikPenjualan.setBackground(new java.awt.Color(255, 255, 255));
        pnGrafikPenjualan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnGrafikPenjualanLayout = new javax.swing.GroupLayout(pnGrafikPenjualan);
        pnGrafikPenjualan.setLayout(pnGrafikPenjualanLayout);
        pnGrafikPenjualanLayout.setHorizontalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        pnGrafikPenjualanLayout.setVerticalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
        );

        pnProdukLaris.setBackground(new java.awt.Color(255, 255, 255));
        pnProdukLaris.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnProdukLarisLayout = new javax.swing.GroupLayout(pnProdukLaris);
        pnProdukLaris.setLayout(pnProdukLarisLayout);
        pnProdukLarisLayout.setHorizontalGroup(
            pnProdukLarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnProdukLarisLayout.setVerticalGroup(
            pnProdukLarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel10.setText("Grafik Penjualan Hari Ini");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(pnProdukTerjual, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                            .addComponent(pnProdukLaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnProdukTerjual, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnProdukLaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel9.setText("Dashboard ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 756, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowProdukTerlarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProdukTerlarisActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProdukLaris.getModel();
        model.setRowCount(0); // Reset tabel

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT nama_produk, SUM(jumlah) AS total FROM transaksi_detail GROUP BY nama_produk ORDER BY total DESC";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String namaProduk = rs.getString("nama_produk");
                String totalJual = rs.getString("total");

                model.addRow(new Object[]{namaProduk, totalJual});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formProdukTerlaris.pack();
        formProdukTerlaris.setLocationRelativeTo(this);
        formProdukTerlaris.setModal(true);
        formProdukTerlaris.setVisible(true);
    }//GEN-LAST:event_btnShowProdukTerlarisActionPerformed

    private void btnShowProdukTerjualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProdukTerjualActionPerformed
        DefaultTableModel model = (DefaultTableModel) tabProduk1.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT t.tanggal, td.nama_produk, SUM(td.jumlah) AS total FROM transaksi_detail td JOIN transaksi t ON td.id_transaksi = t.id_transaksi GROUP BY t.tanggal, td.nama_produk ORDER BY total DESC";
            ResultSet rs = st.executeQuery(query);

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ubahFormat = new SimpleDateFormat("dd-MMMM-yyyy");

            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                String namaProduk = rs.getString("nama_produk");
                String totalJual = rs.getString("total");

                try {
                    tanggal = ubahFormat.format(dbFormat.parse(tanggal));
                } catch (Exception e) {
                    tanggal = "Invalid Date";
                }

                // Tambahkan data ke model tabel
                model.addRow(new Object[]{tanggal, namaProduk, totalJual});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formProdukTerjual.pack();
        formProdukTerjual.setLocationRelativeTo(this);
        formProdukTerjual.setModal(true);
        formProdukTerjual.setVisible(true);
    }//GEN-LAST:event_btnShowProdukTerjualActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowProdukTerjual;
    private javax.swing.JButton btnShowProdukTerlaris;
    private javax.swing.JDialog formProdukTerjual;
    private javax.swing.JDialog formProdukTerlaris;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbProdukTerjual;
    private javax.swing.JLabel lbProdukTerlaris;
    private javax.swing.JPanel pnGrafikPenjualan;
    private javax.swing.JPanel pnProdukLaris;
    private javax.swing.JPanel pnProdukTerjual;
    private javax.swing.JPanel pnProdukTerlaris;
    private javax.swing.JTable tabProduk1;
    private javax.swing.JTable tabProdukLaris;
    // End of variables declaration//GEN-END:variables
}
