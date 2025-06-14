/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package formKasir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import konektor.koneksi;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class VarianRasa1 extends javax.swing.JFrame {
    
    private int batasRasa = 0;
    private JCheckBox[] daftarCheckbox;
    private formHalamanKasir formKasir;
    private int hargaVarian = 0;

    public VarianRasa1(){
        initComponents();
    }
     public VarianRasa1(formHalamanKasir formKasir) {

        initComponents();
        initDropdownMenu();
        tampilkanCheckboxDariDatabase();
        this.formKasir = formKasir;
     
    }

     //  done  
    private void tampilkanCheckboxDariDatabase() {
        pnlrasa.removeAll();
        pnlrasa.setLayout(new java.awt.GridLayout(0, 2)); // biar rapi 2 kolom

        java.util.List<JCheckBox> checkboxList = new ArrayList<>();

        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT id_rasa, rasa FROM rasa";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String namaRasa = rs.getString("rasa");

                JCheckBox cb = new JCheckBox(namaRasa);
                cb.addActionListener(e -> updateCheckboxState());

                pnlrasa.add(cb);
                // simpen ke list
                checkboxList.add(cb);
            }

            daftarCheckbox = checkboxList.toArray(new JCheckBox[0]);

            pnlrasa.revalidate();
            pnlrasa.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal ambil data rasa: " + e.getMessage());
        }

    }
//  done    

    private void initDropdownMenu() {
        JPopupMenu menu = new JPopupMenu();

        try (Connection conn = koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi ke database gagal.");
            }

            String sql = "SELECT id_varian, varian, harga FROM varian";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String namaVarian = rs.getString("varian");
                int jumlahRasa = hitungJumlahRasaDariNamaVarian(namaVarian);
                int harga = rs.getInt("harga");

                JMenuItem item = new JMenuItem(namaVarian);
                item.addActionListener(e -> {
                    btnvarian.setText(namaVarian);
                    hargaVarian = harga;
                    setBatasRasa(jumlahRasa);
                });
                menu.add(item);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        btnvarian.addActionListener(evt -> {
            menu.show(btnvarian, 0, btnvarian.getHeight());
        });
    }

    // Fungsi buat hitung batas rasa berdasarkan nama varian (misal: "Rasa 1" -> 1)
    private int hitungJumlahRasaDariNamaVarian(String namaVarian) {
        if (namaVarian.contains("1")) {
            return 1;
        }
        if (namaVarian.contains("2")) {
            return 2;
        }
        if (namaVarian.contains("3")) {
            return 3;
        }
        if (namaVarian.contains("4")) {
            return 4;
        }
        if (namaVarian.contains("5")) {
            return 5;
        }

        return 1; // default
    }

    private void setBatasRasa(int jumlah) {
        batasRasa = jumlah;

        for (JCheckBox cb : daftarCheckbox) {
            cb.setSelected(false); // reset semua pilihan
            cb.setEnabled(true);   // aktifkan semua
        }
    }

    private void updateCheckboxState() {
        int terpilih = 0;
        for (JCheckBox cb : daftarCheckbox) {
            if (cb.isSelected()) {
                terpilih++;
            }
        }

        boolean disableOthers = (terpilih >= batasRasa);
        for (JCheckBox cb : daftarCheckbox) {
            if (!cb.isSelected()) {
                cb.setEnabled(!disableOthers);
            }

        }
        // jumlah pilihan sesuai batas, langsung eksekusi 
        if (terpilih == batasRasa) {
            simpan();
        }

    }

    private void simpan() {
        String varian = btnvarian.getText();
        List<String> rasaTerpilih = new ArrayList<>();

        for (JCheckBox cb : daftarCheckbox) {
            if (cb.isSelected()) {
                rasaTerpilih.add(cb.getText());
            }
        }

        if (rasaTerpilih.size() != batasRasa) {
            JOptionPane.showMessageDialog(this,
                    "Kamu harus pilih " + batasRasa + " rasa sesuai varian yang dipilih.");
            return;
        }

        String namaProduk = batasRasa + " Rasa (" + String.join(", ", rasaTerpilih) + ")";

        // simpen nama produk + harga ke formHalamanKasir
        formKasir.setNamaProdukDanHarga(namaProduk, hargaVarian);
        this.dispose();
    }


  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblTambahProduk = new javax.swing.JLabel();
        body = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnvarian = new javax.swing.JButton();
        pnlrasa = new javax.swing.JPanel();
        footer = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnkembali = new javax.swing.JButton();
        btnsimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        header.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBackground(new java.awt.Color(255, 204, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        lblTambahProduk.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        lblTambahProduk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTambahProduk.setText("Varian Rasa");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTambahProduk, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblTambahProduk)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        body.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnvarian.setBackground(new java.awt.Color(255, 153, 0));
        btnvarian.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnvarian.setText("Pilih Varian");
        btnvarian.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnvarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnvarianActionPerformed(evt);
            }
        });

        pnlrasa.setBackground(new java.awt.Color(255, 204, 0));
        pnlrasa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlrasa.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlrasaLayout = new javax.swing.GroupLayout(pnlrasa);
        pnlrasa.setLayout(pnlrasaLayout);
        pnlrasaLayout.setHorizontalGroup(
            pnlrasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        pnlrasaLayout.setVerticalGroup(
            pnlrasaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 157, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlrasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnvarian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlrasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnvarian, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(body, java.awt.BorderLayout.CENTER);

        footer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel6.setBackground(new java.awt.Color(255, 204, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnkembali.setBackground(new java.awt.Color(255, 0, 0));
        btnkembali.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnkembali.setForeground(new java.awt.Color(255, 255, 255));
        btnkembali.setText("Kembali");
        btnkembali.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkembaliActionPerformed(evt);
            }
        });

        btnsimpan.setBackground(new java.awt.Color(0, 204, 0));
        btnsimpan.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnsimpan.setText("Simpan");
        btnsimpan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnkembali, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnkembali, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(footerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        getContentPane().add(footer, java.awt.BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnvarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnvarianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnvarianActionPerformed

    private void btnkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkembaliActionPerformed
        dispose();
    }//GEN-LAST:event_btnkembaliActionPerformed

    private void btnsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanActionPerformed

    }//GEN-LAST:event_btnsimpanActionPerformed


    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new VarianRasa1().setVisible(true); // Pastikan ini hanya untuk testing
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    private javax.swing.JButton btnkembali;
    private javax.swing.JButton btnsimpan;
    private javax.swing.JButton btnvarian;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel header;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblTambahProduk;
    private javax.swing.JPanel pnlrasa;
    // End of variables declaration//GEN-END:variables


}
