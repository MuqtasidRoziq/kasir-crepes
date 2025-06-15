package formAdmin;

import java.awt.*;
import java.io.File;
import org.jfree.chart.*;
import java.sql.*;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import konektor.koneksi;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
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
            

            // Query untuk produk terlaris
            String queryProdukTerlaris = "SELECT nama_produk, SUM(jumlah) AS jumlah FROM transaksi_detail GROUP BY nama_produk ORDER BY jumlah DESC LIMIT 1;";
            try (PreparedStatement ps = connection.prepareStatement(queryProdukTerlaris); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lbProdukTerlaris.setText(rs.getString("nama_produk"));
                }
            }
             //query kinerja
            String queryKasirAktif = "SELECT u.username, COUNT(t.id_transaksi) AS jumlah_transaksi FROM transaksi t "
                    + "JOIN user u ON t.id_user = u.id_user WHERE u.role = 'kasir' "
                    + "GROUP BY u.username ORDER BY jumlah_transaksi DESC "
                    + "LIMIT 1;";

            try (PreparedStatement ps = connection.prepareStatement(queryKasirAktif); ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    lbKasirAktif.setText(rs.getString("username") );
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
            System.out.println("Executing query: " + query);

            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int bulan = rs.getInt("bulan");
                    double total = rs.getDouble("total_penjualan");
                    String namaBulan = getNamaBulan(bulan);
                    dataset.addValue(total, "",namaBulan); // Simpler label
                    System.out.println("Data added: " + namaBulan + " = " + total);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Jika dataset kosong, tambahkan data dummy
        if (dataset.getRowCount() == 0) {
            dataset.addValue(100, "Penjualan", "Jan");
            dataset.addValue(150, "Penjualan", "Feb");
            System.out.println("Menggunakan data dummy untuk testing");
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "",
                "Bulan",
                "Total",
                dataset
        );

        // Kustomisasi garis
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setBaseShapesVisible(true);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 450));
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);

        // Debug panel
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
        JFreeChart chart = ChartFactory.createLineChart(
                "",
                "Nama Produk",
                "Total Penjualan",
                dataset
        );

        
        // Kustomisasi garis
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setBaseShapesVisible(true);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 450));
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        
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

        formKinerjaKasir = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblkinerja = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        txttransaksi = new javax.swing.JLabel();
        tglawal1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        btnprint1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        tglakhir1 = new com.toedter.calendar.JDateChooser();
        perbulan1 = new com.toedter.calendar.JMonthChooser();
        pertahun1 = new com.toedter.calendar.JYearChooser();
        formProdukTerlaris = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabProdukLaris = new javax.swing.JTable();
        tglawal = new com.toedter.calendar.JDateChooser();
        tglakhir = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        perbulan = new com.toedter.calendar.JMonthChooser();
        btnprint = new javax.swing.JButton();
        pertahun = new com.toedter.calendar.JYearChooser();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        txttotalterjual = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnProdukTerjual = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbKasirAktif = new javax.swing.JLabel();
        btnShowProdukTerjual = new javax.swing.JButton();
        pnProdukTerlaris = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lbProdukTerlaris = new javax.swing.JLabel();
        btnShowProdukTerlaris = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        pnGrafikPenjualan = new javax.swing.JPanel();
        pnProdukLaris = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Kinerja Kasir");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblkinerja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama Kasir", "Total Transaksi", "Pendapatan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblkinerja);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total Transaksi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bell MT", 1, 14))); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(164, 72));

        txttransaksi.setBackground(new java.awt.Color(204, 204, 204));
        txttransaksi.setFont(new java.awt.Font("Bodoni Bk BT", 1, 20)); // NOI18N
        txttransaksi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        tglawal1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tglawal1PropertyChange(evt);
            }
        });

        jButton2.setText("refresh");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnprint1.setText("Print");
        btnprint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprint1ActionPerformed(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("SD");

        tglakhir1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tglakhir1PropertyChange(evt);
            }
        });

        perbulan1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                perbulan1PropertyChange(evt);
            }
        });

        pertahun1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pertahun1PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tglawal1, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(perbulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pertahun1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tglakhir1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnprint1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnprint1)))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tglawal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(tglakhir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(perbulan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pertahun1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout formKinerjaKasirLayout = new javax.swing.GroupLayout(formKinerjaKasir.getContentPane());
        formKinerjaKasir.getContentPane().setLayout(formKinerjaKasirLayout);
        formKinerjaKasirLayout.setHorizontalGroup(
            formKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formKinerjaKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        formKinerjaKasirLayout.setVerticalGroup(
            formKinerjaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formKinerjaKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Produk Terlaris");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        tglawal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tglawalPropertyChange(evt);
            }
        });

        tglakhir.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tglakhirPropertyChange(evt);
            }
        });

        jLabel5.setText("SD");

        perbulan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                perbulanPropertyChange(evt);
            }
        });

        btnprint.setText("Print");
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });

        pertahun.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pertahunPropertyChange(evt);
            }
        });

        jButton1.setText("refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Produk Terjual"));

        txttotalterjual.setBackground(new java.awt.Color(255, 255, 255));
        txttotalterjual.setFont(new java.awt.Font("Bodoni Bk BT", 1, 20)); // NOI18N
        txttotalterjual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttotalterjual, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttotalterjual, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tglawal, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perbulan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnprint, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pertahun, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tglakhir, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnprint)))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tglawal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tglakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(perbulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pertahun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout formProdukTerlarisLayout = new javax.swing.GroupLayout(formProdukTerlaris.getContentPane());
        formProdukTerlaris.getContentPane().setLayout(formProdukTerlarisLayout);
        formProdukTerlarisLayout.setHorizontalGroup(
            formProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formProdukTerlarisLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnProdukTerjual.setBackground(new java.awt.Color(153, 153, 255));
        pnProdukTerjual.setPreferredSize(new java.awt.Dimension(216, 96));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setText("Kinerja Kasir");

        lbKasirAktif.setFont(new java.awt.Font("Baskerville Old Face", 1, 22)); // NOI18N
        lbKasirAktif.setText("Nama");

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
                    .addComponent(lbKasirAktif, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnShowProdukTerjual))
                .addContainerGap(341, Short.MAX_VALUE))
        );
        pnProdukTerjualLayout.setVerticalGroup(
            pnProdukTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdukTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbKasirAktif, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowProdukTerjual)
                .addContainerGap())
        );

        pnProdukTerlaris.setBackground(new java.awt.Color(153, 153, 255));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Produk Terlaris");

        lbProdukTerlaris.setFont(new java.awt.Font("Baskerville Old Face", 1, 22)); // NOI18N
        lbProdukTerlaris.setText("Rasa");

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
                    .addComponent(lbProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnProdukTerlarisLayout.createSequentialGroup()
                        .addGroup(pnProdukTerlarisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(btnShowProdukTerlaris))
                        .addGap(0, 334, Short.MAX_VALUE)))
                .addContainerGap())
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
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnGrafikPenjualanLayout.setVerticalGroup(
            pnGrafikPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
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
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(pnGrafikPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnProdukTerlaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnProdukLaris, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnProdukTerjual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
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
                .addGap(10, 10, 10))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Dasboard");
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
        kinerjakasir();
        formKinerjaKasir.pack();
        formKinerjaKasir.setLocationRelativeTo(this);
        formKinerjaKasir.setModal(true);
        formKinerjaKasir.setVisible(true);
    }//GEN-LAST:event_btnShowProdukTerjualActionPerformed

    private void tglawal1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tglawal1PropertyChange
        if (tglawal1.getDate() != null || tglakhir1.getDate() != null) {
            perbulan1.setEnabled(false);
            pertahun1.setEnabled(false);
        } else {
            perbulan1.setEnabled(true);
            pertahun1.setEnabled(true);
        }

    }//GEN-LAST:event_tglawal1PropertyChange

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      // TODO add your handling code here:
        tglawal1.setDate(null);
        tglakhir1.setDate(null);
        perbulan1.setMonth(new java.util.Date().getMonth()); // default ke bulan sekarang
        pertahun1.setYear(new java.util.Date().getYear() + 1900); // default ke tahun sekarang

        DefaultTableModel model = (DefaultTableModel) tblkinerja.getModel();
        model.setRowCount(0); // Kosongkan tabel
        kinerjakasir();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnprint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprint1ActionPerformed
        fungsiprintKinerjaKasir();
    }//GEN-LAST:event_btnprint1ActionPerformed

    private void tglakhir1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tglakhir1PropertyChange
        if ("date".equals(evt.getPropertyName())) {
            if (tglawal1.getDate() != null && tglakhir1.getDate() != null) {
                kinerjaKasirPerTanggal(); // ✅ Panggil hanya jika keduanya terisi
                perbulan1.setEnabled(false);
                pertahun1.setEnabled(false);
            }
        }

        // Jika salah satu tanggal dikosongkan lagi, aktifkan kembali bulan/tahun
        if (tglawal1.getDate() == null && tglakhir1.getDate() == null) {
            perbulan1.setEnabled(true);
            pertahun1.setEnabled(true);
        }
    }//GEN-LAST:event_tglakhir1PropertyChange

    private void perbulan1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_perbulan1PropertyChange
        if ("month".equals(evt.getPropertyName())) {
            kinerjaKasirPerBulan();
        }
    }//GEN-LAST:event_perbulan1PropertyChange

    private void pertahun1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pertahun1PropertyChange
        // TODO add your handling code here:
        if ("month".equals(evt.getPropertyName())) {
            kinerjaKasirPerBulan();
        }
    }//GEN-LAST:event_pertahun1PropertyChange

    private void tglawalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tglawalPropertyChange
        if (tglawal.getDate() != null || tglakhir.getDate() != null) {
            perbulan.setEnabled(false);
            pertahun.setEnabled(false);
        } else {
            perbulan.setEnabled(true);
            pertahun.setEnabled(true);
        }
    }//GEN-LAST:event_tglawalPropertyChange

    private void tglakhirPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tglakhirPropertyChange

        if ("date".equals(evt.getPropertyName())) {
            if (tglawal.getDate() != null && tglakhir.getDate() != null) {
                produkpertanggal(); // ✅ Panggil hanya jika keduanya terisi
                perbulan.setEnabled(false);
                pertahun.setEnabled(false);
            }
        }

        // Jika salah satu tanggal dikosongkan lagi, aktifkan kembali bulan/tahun
        if (tglawal.getDate() == null && tglakhir.getDate() == null) {
            perbulan.setEnabled(true);
            pertahun.setEnabled(true);
        }
    }//GEN-LAST:event_tglakhirPropertyChange

    private void perbulanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_perbulanPropertyChange
        if ("month".equals(evt.getPropertyName())) {
            tampilkanPerBulan();
        }
    }//GEN-LAST:event_perbulanPropertyChange

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
        fungsiprint();
    }//GEN-LAST:event_btnprintActionPerformed

    private void pertahunPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pertahunPropertyChange
        if ("month".equals(evt.getPropertyName())) {
            tampilkanPerBulan();
        }
    }//GEN-LAST:event_pertahunPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tglawal.setDate(null);
        tglakhir.setDate(null);
        perbulan.setMonth(new java.util.Date().getMonth()); // default ke bulan sekarang
        pertahun.setYear(new java.util.Date().getYear() + 1900); // default ke tahun sekarang

        DefaultTableModel model = (DefaultTableModel) tabProdukLaris.getModel();
        model.setRowCount(0); // Kosongkan tabel

        // Optional: tampilkan semua data lagi
        produkTerlaris();

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowProdukTerjual;
    private javax.swing.JButton btnShowProdukTerlaris;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnprint1;
    private javax.swing.JDialog formKinerjaKasir;
    private javax.swing.JDialog formProdukTerlaris;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbKasirAktif;
    private javax.swing.JLabel lbProdukTerlaris;
    private com.toedter.calendar.JMonthChooser perbulan;
    private com.toedter.calendar.JMonthChooser perbulan1;
    private com.toedter.calendar.JYearChooser pertahun;
    private com.toedter.calendar.JYearChooser pertahun1;
    private javax.swing.JPanel pnGrafikPenjualan;
    private javax.swing.JPanel pnProdukLaris;
    private javax.swing.JPanel pnProdukTerjual;
    private javax.swing.JPanel pnProdukTerlaris;
    private javax.swing.JTable tabProdukLaris;
    private javax.swing.JTable tblkinerja;
    private com.toedter.calendar.JDateChooser tglakhir;
    private com.toedter.calendar.JDateChooser tglakhir1;
    private com.toedter.calendar.JDateChooser tglawal;
    private com.toedter.calendar.JDateChooser tglawal1;
    private javax.swing.JLabel txttotalterjual;
    private javax.swing.JLabel txttransaksi;
    // End of variables declaration//GEN-END:variables

//  PRODUK TERLASIR 
    public void produkTerlaris() {
        DefaultTableModel model = (DefaultTableModel) tabProdukLaris.getModel();
        model.setRowCount(0);
        int totalSemua = 0; // Buat hitung total

        try {
            Connection con = koneksi.getConnection();
            String query = "SELECT d.nama_produk, SUM(d.jumlah) AS total "
                    + "FROM transaksi_detail d "
                    + "JOIN transaksi t ON d.id_transaksi = t.id_transaksi "
                    + "GROUP BY d.nama_produk "
                    + "ORDER BY total DESC";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String namaProduk = rs.getString("nama_produk");
                int totalJual = rs.getInt("total");
                totalSemua += totalJual;
                model.addRow(new Object[]{namaProduk, totalJual});
            }
            txttotalterjual.setText(String.valueOf(totalSemua));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//  PRODUK TERLARIS PERTANGGAL
    public void produkpertanggal() {
        DefaultTableModel model = (DefaultTableModel) tabProdukLaris.getModel();
        model.setRowCount(0);
        int totalSemua = 0;

        if (tglawal.getDate() == null || tglakhir.getDate() == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dariTanggal = sdf.format(tglawal.getDate());
        String sampaiTanggal = sdf.format(tglakhir.getDate());

        try {
            Connection con = koneksi.getConnection();
            String query = "SELECT d.nama_produk, SUM(d.jumlah) AS total "
                    + "FROM transaksi_detail d "
                    + "JOIN transaksi t ON d.id_transaksi = t.id_transaksi "
                    + "WHERE DATE(t.tanggal) BETWEEN ? AND ? "
                    + "GROUP BY d.nama_produk "
                    + "ORDER BY total DESC";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, dariTanggal);
            pst.setString(2, sampaiTanggal);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String namaProduk = rs.getString("nama_produk");
                int totalJual = rs.getInt("total");
                totalSemua += totalJual;
                model.addRow(new Object[]{namaProduk, totalJual});
            }
            txttotalterjual.setText(String.valueOf(totalSemua));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//  PRODUKSI TERLARIS PERBULAN 
    public void tampilkanPerBulan() {
        int bulan = perbulan.getMonth() + 1;
        int tahun = pertahun.getYear();
        String tanggalAwal = String.format("%04d-%02d-01", tahun, bulan);
        String tanggalAkhir = String.format("%04d-%02d-%02d", tahun, bulan,
                YearMonth.of(tahun, bulan).lengthOfMonth());
        DefaultTableModel model = (DefaultTableModel) tabProdukLaris.getModel();
        model.setRowCount(0);
        int totalSemua = 0;

        try {
            Connection con = koneksi.getConnection();
            String query = "SELECT d.nama_produk, SUM(d.jumlah) AS total "
                    + "FROM transaksi_detail d "
                    + "JOIN transaksi t ON d.id_transaksi = t.id_transaksi "
                    + "WHERE DATE(t.tanggal) BETWEEN ? AND ? "
                    + "GROUP BY d.nama_produk "
                    + "ORDER BY total DESC";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, tanggalAwal);
            pst.setString(2, tanggalAkhir);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String namaProduk = rs.getString("nama_produk");
                int totalJual = rs.getInt("total");
                totalSemua += totalJual;
                model.addRow(new Object[]{namaProduk, totalJual});
            }
            txttotalterjual.setText(String.valueOf(totalSemua));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//  KINERJA KASIR 
    private void kinerjakasir() {
        DefaultTableModel model = (DefaultTableModel) tblkinerja.getModel();
        model.setRowCount(0); // Reset tabel

        try {
            Connection con = koneksi.getConnection();

            // --- Tampilkan ke Tabel Kinerja Kasir ---
            String query = "SELECT "
                    + "u.username AS nama_kasir, "
                    + "COUNT(t.id_transaksi) AS jumlah_transaksi, "
                    + "SUM(t.subtotal) AS total_pendapatan "
                    + "FROM transaksi t "
                    + "JOIN user u ON t.id_user = u.id_user "
                    + "WHERE u.role = 'kasir' "
                    + "GROUP BY u.username "
                    + "ORDER BY total_pendapatan DESC";

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String namaKasir = rs.getString("nama_kasir");
                int jumlahTransaksi = rs.getInt("jumlah_transaksi");
                int totalPendapatan = rs.getInt("total_pendapatan");

                model.addRow(new Object[]{namaKasir, jumlahTransaksi, totalPendapatan});
            }

            // --- Set Total Transaksi ke txttransaksi ---
            String queryTotal = "SELECT COUNT(id_transaksi) AS total FROM transaksi";
            try (PreparedStatement ps = con.prepareStatement(queryTotal); ResultSet rs2 = ps.executeQuery()) {
                if (rs2.next()) {
                    txttransaksi.setText(String.valueOf(rs2.getInt("total")));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Gagal memuat data kinerja kasir!");
        }
    }

//  KINERJA PERTANGGAL
    private void kinerjaKasirPerTanggal() {
        DefaultTableModel model = (DefaultTableModel) tblkinerja.getModel();
        model.setRowCount(0);
        if (tglawal1.getDate() == null || tglakhir1.getDate() == null) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dari = sdf.format(tglawal1.getDate());
        String sampai = sdf.format(tglakhir1.getDate());

        try {
            Connection con = koneksi.getConnection();
            String query = "SELECT u.username AS nama_kasir, COUNT(t.id_transaksi) AS jumlah_transaksi, SUM(t.subtotal) AS total_pendapatan "
                    + "FROM transaksi t JOIN user u ON t.id_user = u.id_user "
                    + "WHERE u.role = 'kasir' AND DATE(t.tanggal) BETWEEN ? AND ? "
                    + "GROUP BY u.username ORDER BY jumlah_transaksi DESC";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, dari);
            pst.setString(2, sampai);
            ResultSet rs = pst.executeQuery();

            int totalTransaksi = 0;
            while (rs.next()) {
                String namaKasir = rs.getString("nama_kasir");
                int jumlah = rs.getInt("jumlah_transaksi");
                int pendapatan = rs.getInt("total_pendapatan");
                totalTransaksi += jumlah;
                model.addRow(new Object[]{namaKasir, jumlah, pendapatan});
            }
            txttransaksi.setText(String.valueOf(totalTransaksi));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  KINERJA PERBULAN 
    private void kinerjaKasirPerBulan() {
        DefaultTableModel model = (DefaultTableModel) tblkinerja.getModel();
        model.setRowCount(0);

        int bulan = perbulan1.getMonth() + 1;
        int tahun = pertahun1.getYear();
        String tanggalAwal = String.format("%04d-%02d-01", tahun, bulan);
        String tanggalAkhir = String.format("%04d-%02d-%02d", tahun, bulan,
                YearMonth.of(tahun, bulan).lengthOfMonth());

        try {
            Connection con = koneksi.getConnection();
            String query = "SELECT u.username AS nama_kasir, COUNT(t.id_transaksi) AS jumlah_transaksi, SUM(t.subtotal) AS total_pendapatan "
                    + "FROM transaksi t JOIN user u ON t.id_user = u.id_user "
                    + "WHERE u.role = 'kasir' AND DATE(t.tanggal) BETWEEN ? AND ? "
                    + "GROUP BY u.username ORDER BY jumlah_transaksi DESC";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, tanggalAwal);
            pst.setString(2, tanggalAkhir);
            ResultSet rs = pst.executeQuery();

            int totalTransaksi = 0;
            while (rs.next()) {
                String namaKasir = rs.getString("nama_kasir");
                int jumlah = rs.getInt("jumlah_transaksi");
                int pendapatan = rs.getInt("total_pendapatan");
                totalTransaksi += jumlah;
                model.addRow(new Object[]{namaKasir, jumlah, pendapatan});
            }
            txttransaksi.setText(String.valueOf(totalTransaksi));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  PRINT PRODUKSI TERLARIS
    private void fungsiprint() {
        java.util.Date tglAwal = tglawal.getDate();
        java.util.Date tglAkhir = tglakhir.getDate();
        int bulanDipilih = perbulan.getMonth();
        int tahunDipilih = pertahun.getYear();

        boolean pakaiTanggal = (tglAwal != null && tglAkhir != null);
        boolean pakaiBulan = (tglAwal == null && tglAkhir == null);

        if (!pakaiTanggal && !pakaiBulan) {
            JOptionPane.showMessageDialog(this, "⚠️ Silakan pilih salah satu filter: Per Tanggal atau Per Bulan!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan Excel");
        int userSelection = chooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".xls")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xls");
            }

            try {
                WritableWorkbook workbook = Workbook.createWorkbook(fileToSave);
                WritableSheet sheet = workbook.createSheet("Data Produk Terlaris", 0);
                TableModel model = tabProdukLaris.getModel();

                // Format Judul
                WritableFont fontJudul = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat formatJudul = new WritableCellFormat(fontJudul);
                formatJudul.setAlignment(Alignment.CENTRE);
                formatJudul.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                formatJudul.setBackground(Colour.SKY_BLUE);
                formatJudul.setWrap(true);
                formatJudul.setBorder(Border.ALL, BorderLineStyle.THIN);

                // Format Header Kolom (tanpa warna)
                WritableFont fontHeader = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
                formatHeader.setAlignment(Alignment.CENTRE);
                formatHeader.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN);

                // Format Isi Tengah
                WritableCellFormat isiTengahFormat = new WritableCellFormat();
                isiTengahFormat.setAlignment(Alignment.CENTRE);
                isiTengahFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                isiTengahFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

                // Format Isi Kiri (untuk nama produk)
                WritableCellFormat isiKiriFormat = new WritableCellFormat();
                isiKiriFormat.setAlignment(Alignment.LEFT);
                isiKiriFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
                isiKiriFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

                WritableCellFormat formatTengahKotak = new WritableCellFormat();
                formatTengahKotak.setAlignment(Alignment.CENTRE); // Rata tengah horizontal
                formatTengahKotak.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // Rata tengah vertikal
                formatTengahKotak.setBorder(Border.ALL, BorderLineStyle.THIN);

                // Judul Tabel
                String headerJudul = "";
                if (pakaiTanggal) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                    headerJudul = "Data Penjualan Tanggal " + sdf.format(tglAwal) + " s.d. " + sdf.format(tglAkhir);
                } else if (pakaiBulan) {
                    String[] namaBulan = new DateFormatSymbols().getMonths();
                    headerJudul = "Data Penjualan Bulan " + namaBulan[bulanDipilih] + " " + tahunDipilih;
                }

                sheet.mergeCells(0, 0, model.getColumnCount() - 1, 0);
                sheet.addCell(new jxl.write.Label(0, 0, headerJudul, formatJudul));
                sheet.setRowView(0, 500); // Set tinggi judul = 25 (500 = 25pt)

                // Header kolom
                for (int i = 0; i < model.getColumnCount(); i++) {
                    if (i == 0) {
                        sheet.setColumnView(i, 50); // Nama Produk lebar = 50
                    } else {
                        sheet.setColumnView(i, 20); // Kolom lain normal
                    }
                    sheet.addCell(new jxl.write.Label(i, 1, model.getColumnName(i), formatHeader));
                }
                sheet.setRowView(1, 500); // Set tinggi header = 25 (satuan 1/20 pt)

                // Data isi tabel
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        if (col == 0) {
                            sheet.addCell(new jxl.write.Label(col, row + 2, value.toString(), isiKiriFormat));
                        } else {
                            sheet.addCell(new jxl.write.Label(col, row + 2, value.toString(), isiTengahFormat));
                        }
                    }
                }

                // Hitung total terjual dari kolom ke-1 (index 1)
                int totalTerjual = 0;
                for (int row = 0; row < model.getRowCount(); row++) {
                    totalTerjual += Integer.parseInt(model.getValueAt(row, 1).toString());
                }
                txttotalterjual.setText(String.valueOf(totalTerjual)); // # Isi ke textbox (kalau perlu)

                // Tambah baris total terjual di bawah data
                int totalRow = model.getRowCount() + 2; // # Baris setelah data
                sheet.setRowView(totalRow, 600); // # Set tinggi baris = 30 pt

                // # Kolom Nama Produk dikosongkan (index 0)
                sheet.addCell(new jxl.write.Label(0, totalRow, ""));

                // # Kolom jumlah terjual (index 1) isi total terjual
                sheet.addCell(new jxl.write.Label(1, totalRow, txttotalterjual.getText(), isiTengahFormat));
                sheet.setColumnView(3, 25); // Kolom Total Value (49 Terjual)
                // # Kolom samping kanan (index 2 & 3) bisa isi label “Total:” dan hasil total
                sheet.addCell(new jxl.write.Label(3, 1, "Total: " + txttotalterjual.getText() + " Terjual", formatHeader)); // # Kolom ke-3

                workbook.write();
                workbook.close();
                JOptionPane.showMessageDialog(this, "✅ File berhasil disimpan:\n" + fileToSave.getAbsolutePath());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "❌ Gagal export: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

//  PRINT KINERJA 
    private void fungsiprintKinerjaKasir() {
    java.util.Date tglAwal = tglawal1.getDate();
    java.util.Date tglAkhir = tglakhir1.getDate();
    int bulanDipilih = perbulan1.getMonth();
    int tahunDipilih = pertahun1.getYear();
    boolean pakaiTanggal = (tglAwal != null && tglAkhir != null);
    boolean pakaiBulan = (tglAwal == null && tglAkhir == null);

    if (!pakaiTanggal && !pakaiBulan) {
        JOptionPane.showMessageDialog(this, "⚠️ Silakan pilih salah satu filter: Per Tanggal atau Per Bulan!");
        return;
    }

    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Simpan Laporan Excel");
    int userSelection = chooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = chooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".xls")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".xls");
        }

        try {
            WritableWorkbook workbook = Workbook.createWorkbook(fileToSave);
            WritableSheet sheet = workbook.createSheet("Data Kinerja Kasir", 0);
            TableModel model = tblkinerja.getModel();

            // Format-format sel
            WritableFont fontJudul = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat formatJudul = new WritableCellFormat(fontJudul);
            formatJudul.setAlignment(Alignment.CENTRE);
            formatJudul.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formatJudul.setBackground(Colour.SKY_BLUE);
            formatJudul.setWrap(true);
            formatJudul.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont fontHeader = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat formatHeader = new WritableCellFormat(fontHeader);
            formatHeader.setAlignment(Alignment.CENTRE);
            formatHeader.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            formatHeader.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat isiTengahFormat = new WritableCellFormat();
            isiTengahFormat.setAlignment(Alignment.CENTRE);
            isiTengahFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            isiTengahFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableCellFormat isiKiriFormat = new WritableCellFormat();
            isiKiriFormat.setAlignment(Alignment.LEFT);
            isiKiriFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
            isiKiriFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

            // Judul laporan
            String headerJudul = "";
            if (pakaiTanggal) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                headerJudul = "Kinerja Kasir Tanggal " + sdf.format(tglAwal) + " s.d. " + sdf.format(tglAkhir);
            } else if (pakaiBulan) {
                String[] namaBulan = new DateFormatSymbols().getMonths();
                headerJudul = "Kinerja Kasir Bulan " + namaBulan[bulanDipilih] + " " + tahunDipilih;
            }
            sheet.mergeCells(0, 0, model.getColumnCount() - 1, 0);
            sheet.addCell(new jxl.write.Label(0, 0, headerJudul, formatJudul));
            sheet.setRowView(0, 500);

            // Header tabel
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.setColumnView(i, 25);
                sheet.addCell(new jxl.write.Label(i, 1, model.getColumnName(i), formatHeader));
            }
            sheet.setRowView(1, 500);

            // Data tabel
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    if (col == 0) {
                        sheet.addCell(new jxl.write.Label(col, row + 2, value.toString(), isiKiriFormat));
                    } else {
                        sheet.addCell(new jxl.write.Label(col, row + 2, value.toString(), isiTengahFormat));
                    }
                }
            }

            // Total transaksi
            int totalTransaksi = 0;
            for (int row = 0; row < model.getRowCount(); row++) {
                totalTransaksi += Integer.parseInt(model.getValueAt(row, 1).toString());
            }
            txttransaksi.setText(String.valueOf(totalTransaksi));
      
            int totalPendapatan = 0;
            for (int row = 0; row < model.getRowCount(); row++) {
                totalPendapatan += Integer.parseInt(model.getValueAt(row, 2).toString()); // asumsi kolom 2 = pendapatan
            }
            int totalRow = model.getRowCount() + 2;
          
            sheet.addCell(new jxl.write.Label(4, 1, "Total: " + txttransaksi.getText() + " Transaksi", formatHeader));
            sheet.setColumnView(4, 20); // Lebar kolom untuk keterangan
            sheet.addCell(new jxl.write.Label(5, 1, "Total Pendapatan: Rp. " + totalPendapatan, formatHeader));
            sheet.setColumnView(5, 30); // Lebar kolom untuk keterangan
            DecimalFormat formatRupiah = new DecimalFormat("#,##0");
            String pendapatanFormatted = "Rp. " + formatRupiah.format(totalPendapatan);
            
            workbook.write();
            workbook.close();

            JOptionPane.showMessageDialog(this, "✅ File berhasil disimpan:\n" + fileToSave.getAbsolutePath());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Gagal export: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
}
   

}
