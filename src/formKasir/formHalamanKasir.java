package formKasir;

import konektor.koneksi;
import java.io.FileOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileWriter;
import java.io.IOException;

public class formHalamanKasir extends javax.swing.JPanel {

    private String userId;

    public formHalamanKasir(String userId) {
        this.userId = userId;
        initComponents();
        getNamaKasir(userId);
        idKasir.setEditable(false);
        namaKasir.setEditable(false);
        txtnamaproduk.setEditable(false);
        lblharga.setEditable(false);
        lbltransaksi.setEditable(false);
        lbltanggaltransaksi.setEditable(false);
        lblkembalian.setEditable(false);
        lblsubtotal.setEditable(false);
        prosesTransaksi();
        pendapatanhariini();

        btnHapus.setEnabled(false);
        lblpendapatan.setEditable(false);

//        menonaktifkan jika memilih baris pada tabel
        tabProduk.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && tabProduk.getSelectedRow() != -1) {
                    btnHapus.setEnabled(true);
                } else {
                    btnHapus.setEnabled(false);
                }
            }
        });
    }

    private void btnPilihVarianActionPerformed(java.awt.event.ActionEvent evt) {
        VarianRasa varian = new VarianRasa(this);
        varian.setVisible(true);
    }

    public void setNamaProdukDanHarga(String namaProduk, int harga) {
        txtnamaproduk.setText(namaProduk);
        lblharga.setText(String.valueOf(harga));
    }

    public void setUserId(String id) {
        this.userId = id;
    }

// Fungsi tampilkan pendapatan kotor hari ini sesuai user login
    private void pendapatanhariini() {
        double totalPendapatanHariIni = 0;
        try {
            Connection conn = koneksi.getConnection();
            String sql = "SELECT COALESCE(SUM(td.total), 0) AS totalHariIni "
                    + "FROM transaksi t "
                    + "JOIN transaksi_detail td ON t.id_transaksi = td.id_transaksi "
                    + "WHERE DATE(t.tanggal) = CURDATE() AND t.id_user = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId); // userId yang di-set saat login
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                totalPendapatanHariIni = rs.getDouble("totalHariIni");
            }

            // Format ke mata uang Indonesia
            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            lblpendapatan.setText(nf.format(totalPendapatanHariIni));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan pendapatan kotor!\n" + e.getMessage());
        }
    }

//  Set Nama Kasir //
    private void getNamaKasir(String userId) {
        String query = "SELECT id_user, username FROM user WHERE id_user=?";
        try {
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                idKasir.setText(rs.getString("id_user"));
                namaKasir.setText(rs.getString("username")); // diperbaiki di sini
            } else {
                JOptionPane.showMessageDialog(null, "Data user tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
//  Set Nama Kasir END //

//  Function noTransaksi Otomatis //    
    private String noTransaksi() {
        String urutan = null;

        SimpleDateFormat formatDateTransaksi = new SimpleDateFormat("yyMMdd");
        String dateTransaksi = formatDateTransaksi.format(new Date());

        String sql = "SELECT RIGHT(id_transaksi, 3) AS nomor "
                + "FROM transaksi "
                + "WHERE id_transaksi LIKE 'TRX" + dateTransaksi + "%' "
                + "ORDER BY id_transaksi DESC "
                + "LIMIT 1";
        try {
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int nomor = Integer.parseInt(rs.getString("nomor"));
                nomor++;
                urutan = "TRX" + dateTransaksi + String.format("%03d", nomor);
            } else {
                urutan = "TRX" + dateTransaksi + "001";
            }
        } catch (Exception e) {
            e.printStackTrace();
            urutan = "TRX" + dateTransaksi + "001"; // Default jika terjadi error
        }

        return urutan;
    }
//  Function noTransaksi Otomatis End // 

//   DONEEEEEEEEE
//  Function addProduct //    
    private void addProduct() {
        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatDate.format(new Date());
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID")); // Format hanya untuk lblsubtotal

        if (lbljumlah.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input varian tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            lbljumlah.requestFocus();
        } else {
            try {
                double harga = Double.parseDouble(lblharga.getText());
                int jumlah = Integer.parseInt(lbljumlah.getText());
                double subTotal = harga * jumlah;

                tbl.addRow(new Object[]{
                    tbl.getRowCount() + 1, // Isi kolom No
                    txtnamaproduk.getText(), // Nama Produk
                    harga, // Harga
                    jumlah, // Jumlah
                    subTotal // Total
                });

                double total = 0;
                if (!lblsubtotal.getText().isEmpty()) {
                    String cleaned = lblsubtotal.getText().replace("Rp", "").replace(".", "").replace(",", ".").trim();
                    total = Double.parseDouble(cleaned);
                }
                updateNomor();
                total += subTotal;
                lblsubtotal.setText(nf.format(total)); // tampil sebagai Rp
                lbltransaksi.setText(noTransaksi());
                lbltanggaltransaksi.setText(date);
                
                txtnamaproduk.setText("");
                lblharga.setText("");
                lbljumlah.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Input harga atau jumlah tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
//  Function addProduct End // 

//  ============================================================================
    // Fungsi untuk memproses transaksi
    private void prosesTransaksi() {
        try {
            double bayar = Double.parseDouble(lblbayar.getText().trim());
            String subtotalStr = lblsubtotal.getText().replace("Rp", "").replace(".", "").replace(",", ".").trim();
            double subtotal = Double.parseDouble(subtotalStr);

            // Cek apakah uang cukup
            if (bayar < subtotal) {
                double kekurangan = subtotal - bayar;
                JOptionPane.showMessageDialog(this, "Uang kurang sebesar Rp" + kekurangan, "Pembayaran Gagal", JOptionPane.WARNING_MESSAGE);
                return; // stop proses kalau kurang
            }

            // Hitung kembalian
            double kembalian = bayar - subtotal;
            lblkembalian.setText(String.valueOf(kembalian));

            // Simpan ke database
            simpanData(subtotal, bayar, kembalian);
            simpanDetailTransaksi();

            JOptionPane.showMessageDialog(this, "Proses pembayaran berhasil!\nKembalian: Rp" + kembalian, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            lbltransaksi.setText("");
            lbltanggaltransaksi.setText("");

        } catch (NumberFormatException e) {
        }
    }

//   simpanData
    private void simpanData(double subtotal, double totalBayar, double kembalian) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "INSERT INTO transaksi (id_transaksi, id_user, tanggal, subtotal, total_bayar, kembalian) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, lbltransaksi.getText());
            ps.setString(2, idKasir.getText()); // variabel idKasir harus disiapkan sebelumnya
            ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.setDouble(4, subtotal);
            ps.setDouble(5, totalBayar);
            ps.setDouble(6, kembalian);

            ps.executeUpdate();
//            JOptionPane.showMessageDialog(this, "Data transaksi utama berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi utama\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//  Simpan detail
    private void simpanDetailTransaksi() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO transaksi_detail(id_transaksi, nama_produk, harga_produk, jumlah, total) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);

            DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();
            int jumlahBaris = model.getRowCount();
            System.out.println("Jumlah baris yang akan disimpan: " + jumlahBaris);

            for (int i = 0; i < jumlahBaris; i++) {
                // Kolom 0 = No, jadi mulai dari kolom 1
                String namaProduk = model.getValueAt(i, 1).toString();
                double harga = Double.parseDouble(model.getValueAt(i, 2).toString());
                int jumlah = Integer.parseInt(model.getValueAt(i, 3).toString());
                double total = Double.parseDouble(model.getValueAt(i, 4).toString());

                ps.setString(1, lbltransaksi.getText());
                ps.setString(2, namaProduk);
                ps.setDouble(3, harga);
                ps.setInt(4, jumlah);
                ps.setDouble(5, total);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Detail transaksi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan detail transaksi\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        pendapatanhariini();
        NotaPrint();
        resetHalaman();
    }

//  reset
    private void resetHalaman() {
        txtnamaproduk.setText("");
        lblharga.setText("");
        lbljumlah.setText("");
        lbltanggaltransaksi.setText("");
        lbltransaksi.setText("");
        lblkembalian.setText("");
        lblsubtotal.setText("");
        lblbayar.setText("");
        clearTable(); // Kosongkan tabel
        varian.requestFocus(); // Fokuskan ke input ID Produk
    }

//    private double getSubtotalFromLabel() {
//        String formattedSubtotal = lblsubtotal.getText();
//        formattedSubtotal = formattedSubtotal.replace("Rp ", "").replace(",", "");
//        return Double.parseDouble(formattedSubtotal);
//    }
//
//    private double getPembayaranFromLabel() throws NumberFormatException {
//        String bayarText = lblbayar.getText()
//                .replace("Rp", "")
//                .replaceAll("[^\\d]", "") // hanya ambil angka 0-9
//                .trim();
//
//        if (bayarText.isEmpty()) {
//            throw new NumberFormatException("Input kosong atau tidak valid");
//        }
//
//        System.out.println("DEBUG - Nilai inputBayar setelah dibersihkan: " + bayarText);
//        return Double.parseDouble(bayarText);
//    }

    private void clearTable() {
        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();
        tbl.setRowCount(0);
        // Reset ID transaksi dan tanggal pembelian
        lbltransaksi.setText("ID Transaksi");
        lbltanggaltransaksi.setText("Tanggal");
    }

//  ============================================================================
//  Delete Row in TabProduk //
    private void deleteRow() {
        DefaultTableModel tbl = (DefaultTableModel) tabProduk.getModel();
        int selectedRow = tabProduk.getSelectedRow();

        if (selectedRow != -1) {
            tbl.removeRow(selectedRow);
            double total = 0;
            for (int i = 0; i < tbl.getRowCount(); i++) {
                Object subTotalObj = tbl.getValueAt(i, 4);
                if (subTotalObj != null) {
                    try {
                        double subTotal = Double.parseDouble(subTotalObj.toString());
                        total += subTotal;
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing Subtotal pada baris " + i + ": " + subTotalObj);
                    }
                }
            }
            lblsubtotal.setText(String.format("%.2f", total));

            if (tbl.getRowCount() == 0) {
                lbltransaksi.setText("");
                lbltransaksi.repaint();
                lbltanggaltransaksi.setText("");
                lbltanggaltransaksi.repaint();
                lblbayar.setText("");
                lblbayar.repaint();
                lblkembalian.setText("");
                lblkembalian.repaint();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!");
        }
    }
//  Delete Row in TabProduk End //  

    private void NotaPrint() {
        showNotaPreview(); // Langsung panggil preview
    }

// Method untuk menampilkan preview nota dalam pop-up
    private void showNotaPreview() {
        // Buat dialog untuk preview (gunakan JDialog bukan JFrame)
        JDialog previewDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Preview Nota", true);
        previewDialog.setSize(500, 700);
        previewDialog.setLocationRelativeTo(this);
        previewDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Area text untuk menampilkan nota
        JTextArea notaTextArea = new JTextArea();
        notaTextArea.setEditable(false);
        notaTextArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        notaTextArea.setText(generateNotaText());

        // Scroll pane untuk text area
        JScrollPane scrollPane = new JScrollPane(notaTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Preview Nota"));

        // Panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Tombol Print
        JButton printButton = new JButton("Print");
        printButton.addActionListener((ActionEvent e) -> {
            printNota(notaTextArea);
            // Hapus dispose() agar dialog tetap terbuka setelah print
        });

        // Tombol Save as Text
        JButton saveButton = new JButton("Save as Text");
        saveButton.addActionListener((ActionEvent e) -> {
            saveNotaToText(notaTextArea.getText());
            // Hapus dispose() agar dialog tetap terbuka setelah save
        });

        // Tombol Cancel
        JButton cancelButton = new JButton("Close");
        cancelButton.addActionListener((ActionEvent e) -> {
            previewDialog.dispose();
        });

        // Tambahkan tombol ke panel
        buttonPanel.add(printButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Layout dialog
        
        previewDialog.setLayout(new BorderLayout());
        previewDialog.add(scrollPane, BorderLayout.CENTER);
        previewDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Tampilkan dialog
        previewDialog.pack();
        previewDialog.setLocationRelativeTo(this);
        previewDialog.setModal(true);
        previewDialog.setVisible(true);
    }

// [Method generateNotaText(), printNota(), dan saveNotaToText() tetap sama seperti sebelumnya]
// Method untuk generate text nota
    private String generateNotaText() {
        StringBuilder nota = new StringBuilder();

        // Header 47
        nota.append("               Crepes Mahabbah                \n");
        nota.append("       Ds Padaharja Kec Kramat Kab Tegal      \n");
        nota.append("                   No Telp                    \n");
        nota.append("                +6285229369824                \n");
        nota.append("==============================================\n");
        nota.append(String.format("%-12s: %s\n", "Tanggal", lbltanggaltransaksi.getText()));
        nota.append(String.format("%-12s: %s\n", "ID Transaksi", lbltransaksi.getText()));
        nota.append(String.format("%-12s: %s\n", "Nama Kasir", namaKasir.getText()));

        nota.append("---------------- PEMBELIAN ----------------\n");
        nota.append(" \n");

        // Produk
        for (int i = 0; i < tabProduk.getRowCount(); i++) {
            String namaProduk = tabProduk.getValueAt(i, 1).toString();
            double hargaProduk = Double.parseDouble(tabProduk.getValueAt(i, 2).toString());
            String jumlah = tabProduk.getValueAt(i, 3).toString();
            double totalProduk = Double.parseDouble(tabProduk.getValueAt(i, 4).toString());

            String harga = formatRupiah(hargaProduk);
            String total = formatRupiah(totalProduk);

            nota.append(String.format("%-12s: %s\n", "Nama", namaProduk));
            nota.append(String.format("%-12s: %s\n", "Harga", harga));
            nota.append(String.format("%-12s: %s\n", "Jumlah", jumlah));
            nota.append(String.format("%-12s: %s\n", "Total", total));
            nota.append(" \n"); // Spasi antar produk
        }

        // Footer total
        nota.append("==============================================\n");
        nota.append(String.format("%-12s: %25s\n", "Subtotal", formatRupiah(unformatRupiah(lblsubtotal.getText()))));
        nota.append(String.format("%-12s: %26s\n", "Tunai", formatRupiah(Double.parseDouble(lblbayar.getText()))));
        nota.append(String.format("%-12s: %25s\n", "Kembalian", formatRupiah(Double.parseDouble(lblkembalian.getText()))));
        nota.append("==============================================\n");

        // Terima kasih
        nota.append("            Terimakasih           \n");
        nota.append("        Telah berbelanja di Toko Kami     \n");

        return nota.toString();
    }

// Method untuk print nota langsung ke printer
    private void printNota(JTextArea textArea) {
        try {
            PrinterJob printerJob = PrinterJob.getPrinterJob();

            // Set printable
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Set font untuk printing
                    java.awt.Font printFont = new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10);
                    g2d.setFont(printFont);

                    // Print text
                    String[] lines = textArea.getText().split("\n");
                    int y = 20;
                    for (String line : lines) {
                        g2d.drawString(line, 10, y);
                        y += 15;

                        // Check if we need to go to next page
                        if (y > pageFormat.getImageableHeight() - 20) {
                            return PAGE_EXISTS;
                        }
                    }

                    return PAGE_EXISTS;
                }
            });

            // Show print dialog dan print
            if (printerJob.printDialog()) {
                printerJob.print();
                JOptionPane.showMessageDialog(this, "Nota berhasil dicetak!");
            }

        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak nota: " + e.getMessage());
        }
    }

// Method untuk save nota ke file text
    private void saveNotaToText(String notaContent) {
        // File chooser untuk memilih lokasi save
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Nota Text");
        fileChooser.setSelectedFile(new File("nota_transaksi_" + lbltransaksi.getText() + ".txt"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Pastikan file berekstensi .txt
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt";
            }

            try {
                // Write content to file
                FileWriter writer = new FileWriter(filePath);
                writer.write(notaContent);
                writer.close();

                JOptionPane.showMessageDialog(this, "Nota berhasil disimpan ke: " + filePath);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan nota: " + e.getMessage());
            }
        }
    }
//  

    private void updateNomor() {
        DefaultTableModel model = (DefaultTableModel) tabProduk.getModel();
        // Hapus baris kosong (jika ada)
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            if (model.getValueAt(i, 1) == null || model.getValueAt(i, 1).toString().trim().isEmpty()) {
                model.removeRow(i); // Hapus baris kosong
            }
        }
        // Update nomor urut
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0); // Kolom No adalah kolom pertama (indeks 0)
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTambahProduk2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        bg1 = new javax.swing.JPanel();
        lbNamaProduk = new javax.swing.JLabel();
        txtnamaproduk = new javax.swing.JTextField();
        lbHarga = new javax.swing.JLabel();
        lblharga = new javax.swing.JTextField();
        lbJumlah = new javax.swing.JLabel();
        lbljumlah = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabProduk = new javax.swing.JTable();
        varian = new javax.swing.JButton();
        btlpesanan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblpendapatan = new javax.swing.JTextField();
        lbIdTransaksi1 = new javax.swing.JLabel();
        bg2 = new javax.swing.JPanel();
        lbTotal = new javax.swing.JLabel();
        lblsubtotal = new javax.swing.JTextField();
        lbIdTransaksi = new javax.swing.JLabel();
        lbltransaksi = new javax.swing.JTextField();
        lbTanggal = new javax.swing.JLabel();
        lbBayar = new javax.swing.JLabel();
        lblbayar = new javax.swing.JTextField();
        lbKembalian = new javax.swing.JLabel();
        lblkembalian = new javax.swing.JTextField();
        btnBayar = new javax.swing.JButton();
        lbltanggaltransaksi = new javax.swing.JTextField();
        idKasir = new javax.swing.JTextField();
        namaKasir = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lbIdTransaksi2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTambahProduk2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTambahProduk2.setText("Transaksi");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk2, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTambahProduk2)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bg1.setBackground(new java.awt.Color(255, 255, 255));
        bg1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbNamaProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbNamaProduk.setText("Nama Produk");

        txtnamaproduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamaprodukActionPerformed(evt);
            }
        });

        lbHarga.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbHarga.setText("Harga");

        lblharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblhargaActionPerformed(evt);
            }
        });

        lbJumlah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbJumlah.setText("Jumlah");

        lbljumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbljumlahActionPerformed(evt);
            }
        });

        btnTambah.setBackground(new java.awt.Color(51, 204, 0));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(255, 51, 0));
        btnHapus.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        tabProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Nama Produk", "Harga", "Jumlah", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabProduk);
        if (tabProduk.getColumnModel().getColumnCount() > 0) {
            tabProduk.getColumnModel().getColumn(0).setHeaderValue("No");
        }

        varian.setBackground(new java.awt.Color(255, 153, 0));
        varian.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        varian.setForeground(new java.awt.Color(255, 255, 255));
        varian.setText("Pilih Varian");
        varian.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        varian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                varianActionPerformed(evt);
            }
        });

        btlpesanan.setBackground(new java.awt.Color(255, 0, 0));
        btlpesanan.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btlpesanan.setForeground(new java.awt.Color(255, 255, 255));
        btlpesanan.setText("Batalkan Pesanan");
        btlpesanan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btlpesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btlpesananActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 204, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblpendapatan.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblpendapatan.setText("Rp. ");
        lblpendapatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblpendapatanActionPerformed(evt);
            }
        });

        lbIdTransaksi1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        lbIdTransaksi1.setText("Pendapatan Hari Ini");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbIdTransaksi1)
                        .addGap(0, 119, Short.MAX_VALUE))
                    .addComponent(lblpendapatan))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbIdTransaksi1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblpendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bg1Layout = new javax.swing.GroupLayout(bg1);
        bg1.setLayout(bg1Layout);
        bg1Layout.setHorizontalGroup(
            bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbHarga)
                            .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtnamaproduk, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bg1Layout.createSequentialGroup()
                                    .addComponent(lbJumlah)
                                    .addGap(50, 50, 50)))
                            .addComponent(lbNamaProduk)
                            .addComponent(varian, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bg1Layout.createSequentialGroup()
                        .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbljumlah)
                            .addComponent(lblharga, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btlpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(354, 354, 354)))
                .addContainerGap())
        );
        bg1Layout.setVerticalGroup(
            bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg1Layout.createSequentialGroup()
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(varian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(lbNamaProduk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnamaproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(lbHarga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bg1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)))
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblharga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btlpesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(lbJumlah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbljumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(bg1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(bg1Layout.createSequentialGroup()
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        bg2.setBackground(new java.awt.Color(255, 204, 0));
        bg2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lbTotal.setText("Total Harga");

        lblsubtotal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblsubtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblsubtotalActionPerformed(evt);
            }
        });

        lbIdTransaksi.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdTransaksi.setText("Id Transaksi");

        lbltransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbltransaksiActionPerformed(evt);
            }
        });

        lbTanggal.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbTanggal.setText("Tanggal Pembelian");

        lbBayar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbBayar.setText("Tunai");

        lblbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblbayarActionPerformed(evt);
            }
        });

        lbKembalian.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbKembalian.setText("Kembalian");

        lblkembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblkembalianActionPerformed(evt);
            }
        });

        btnBayar.setBackground(new java.awt.Color(255, 153, 0));
        btnBayar.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("Bayar");
        btnBayar.setPreferredSize(new java.awt.Dimension(72, 25));
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        idKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idKasirActionPerformed(evt);
            }
        });

        namaKasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaKasirActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel1.setText("Nama Kasir");

        lbIdTransaksi2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdTransaksi2.setText("Id Kasir");

        javax.swing.GroupLayout bg2Layout = new javax.swing.GroupLayout(bg2);
        bg2.setLayout(bg2Layout);
        bg2Layout.setHorizontalGroup(
            bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblsubtotal)
                    .addGroup(bg2Layout.createSequentialGroup()
                        .addComponent(lblbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblkembalian))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bg2Layout.createSequentialGroup()
                        .addComponent(idKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(namaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bg2Layout.createSequentialGroup()
                        .addComponent(lbIdTransaksi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbTanggal)
                        .addGap(16, 16, 16))
                    .addGroup(bg2Layout.createSequentialGroup()
                        .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTotal)
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addComponent(lbBayar)
                                .addGap(112, 112, 112)
                                .addComponent(lbKembalian))
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addComponent(lbIdTransaksi2)
                                .addGap(98, 98, 98)
                                .addComponent(jLabel1))
                            .addGroup(bg2Layout.createSequentialGroup()
                                .addComponent(lbltransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbltanggaltransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bg2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        bg2Layout.setVerticalGroup(
            bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bg2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbIdTransaksi2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbIdTransaksi)
                    .addComponent(lbTanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbltransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltanggaltransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbBayar)
                    .addComponent(lbKembalian))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bg2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lbTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblsubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 974, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(bg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(bg2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 691, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(bg2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtnamaprodukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamaprodukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaprodukActionPerformed

    private void lblhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblhargaActionPerformed

    }//GEN-LAST:event_lblhargaActionPerformed

    private void lbljumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbljumlahActionPerformed
        addProduct();
    }//GEN-LAST:event_lbljumlahActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        addProduct();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        deleteRow();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void varianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_varianActionPerformed
        VarianRasa1 varianRasa = new VarianRasa1(this);
        varianRasa.setVisible(true);
    }//GEN-LAST:event_varianActionPerformed

    private void btlpesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btlpesananActionPerformed
        txtnamaproduk.setText("");
        lblharga.setText("");
    }//GEN-LAST:event_btlpesananActionPerformed

    private void lblsubtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblsubtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblsubtotalActionPerformed

    private void lbltransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbltransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lbltransaksiActionPerformed

    private void lblbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblbayarActionPerformed
        prosesTransaksi();
    }//GEN-LAST:event_lblbayarActionPerformed

    private void lblkembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblkembalianActionPerformed

    }//GEN-LAST:event_lblkembalianActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
        prosesTransaksi();
    }//GEN-LAST:event_btnBayarActionPerformed

    private void idKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idKasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idKasirActionPerformed

    private void namaKasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaKasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaKasirActionPerformed

    private void lblpendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblpendapatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblpendapatanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg1;
    private javax.swing.JPanel bg2;
    private javax.swing.JButton btlpesanan;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JTextField idKasir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbBayar;
    private javax.swing.JLabel lbHarga;
    private javax.swing.JLabel lbIdTransaksi;
    private javax.swing.JLabel lbIdTransaksi1;
    private javax.swing.JLabel lbIdTransaksi2;
    private javax.swing.JLabel lbJumlah;
    private javax.swing.JLabel lbKembalian;
    private javax.swing.JLabel lbNamaProduk;
    private javax.swing.JLabel lbTanggal;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JLabel lblTambahProduk2;
    private javax.swing.JTextField lblbayar;
    private javax.swing.JTextField lblharga;
    private javax.swing.JTextField lbljumlah;
    private javax.swing.JTextField lblkembalian;
    private javax.swing.JTextField lblpendapatan;
    private javax.swing.JTextField lblsubtotal;
    private javax.swing.JTextField lbltanggaltransaksi;
    private javax.swing.JTextField lbltransaksi;
    private javax.swing.JTextField namaKasir;
    private javax.swing.JTable tabProduk;
    private javax.swing.JTextField txtnamaproduk;
    private javax.swing.JButton varian;
    // End of variables declaration//GEN-END:variables
public void setNamaProduk(String namaProduk) {
        txtnamaproduk.setText(namaProduk); // isikan ke field Nama Produk

    }
// 

    private String formatRupiah(double value) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        kursIndonesia.setMaximumFractionDigits(2); // Dua angka di belakang koma
        kursIndonesia.setMinimumFractionDigits(2); // Tetap dua angka meskipun nol

        return kursIndonesia.format(value);
    }

    private double unformatRupiah(String value) {
        try {
            value = value.replace("Rp", "").replace(".", "").replace(",", ".").trim();
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
//

}
