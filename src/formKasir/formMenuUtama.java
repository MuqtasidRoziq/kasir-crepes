package formKasir;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import formAdmin.formMenuList;
import formLogin.Login;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;

/**
 *
 * @author r
 */
public class formMenuUtama extends javax.swing.JFrame {

    private String userId;
    private String nama;
    private String email;
    private String role;
    private String userName;
    private String Password;
    
    public formMenuUtama() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);   
        execute();
        Date(); 
        
    }
    
    //  set nama user yang login start //    
    public void setUser(String userId, String nama, String email, String role, String userName, String Password) {
        this.userId = userId;
        this.nama = nama;
        this.email = email;
        this.role = role;
        this.userName = userName;
        this.Password = Password;
        lb_u.setText("Selamat Datang, " + this.userName ); // Perbarui label
        lblrole.setText(this.role ); // Perbarui label
  
    }
//  set nama user yang login end //
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lb_u = new javax.swing.JLabel();
        lblrole = new javax.swing.JLabel();
        lb_tanggal = new javax.swing.JLabel();
        menuList = new javax.swing.JPanel();
        listMenuItemKasir = new javax.swing.JPanel();
        content = new javax.swing.JPanel();
        isiContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 94));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logoKasir.png"))); // NOI18N
        jLabel1.setText("Toko Kami");

        lb_u.setFont(new java.awt.Font("Schadow BT", 1, 14)); // NOI18N
        lb_u.setText("Nama User");

        lblrole.setFont(new java.awt.Font("Schadow BT", 1, 18)); // NOI18N
        lblrole.setText("Role");

        lb_tanggal.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        lb_tanggal.setText("Tanggal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblrole, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_u)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 279, Short.MAX_VALUE)
                        .addComponent(lb_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblrole)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_u)
                            .addComponent(lb_tanggal)))
                    .addComponent(jLabel1))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        menuList.setPreferredSize(new java.awt.Dimension(255, 448));

        listMenuItemKasir.setBackground(new java.awt.Color(153, 153, 255));
        listMenuItemKasir.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        listMenuItemKasir.setLayout(new javax.swing.BoxLayout(listMenuItemKasir, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout menuListLayout = new javax.swing.GroupLayout(menuList);
        menuList.setLayout(menuListLayout);
        menuListLayout.setHorizontalGroup(
            menuListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuListLayout.createSequentialGroup()
                .addComponent(listMenuItemKasir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        menuListLayout.setVerticalGroup(
            menuListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listMenuItemKasir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(menuList, java.awt.BorderLayout.LINE_START);

        isiContent.setBackground(new java.awt.Color(255, 255, 255));
        isiContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        isiContent.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(isiContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(isiContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(content, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        isiContent.add(new formHalamanKasir(userId));
        isiContent.repaint();
        isiContent.revalidate();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(formMenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formMenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formMenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formMenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formMenuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    private javax.swing.JPanel header;
    private javax.swing.JPanel isiContent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_tanggal;
    private javax.swing.JLabel lb_u;
    private javax.swing.JLabel lblrole;
    private javax.swing.JPanel listMenuItemKasir;
    private javax.swing.JPanel menuList;
    // End of variables declaration//GEN-END:variables

    private void execute() {
        
        // gambar icon //
        ImageIcon iconkasir = new ImageIcon(getClass().getResource("/image/kasir.png"));
        ImageIcon iconRiwayatTransaksi = new ImageIcon(getClass().getResource("/image/riwayat.png"));
        ImageIcon iconProfile = new ImageIcon(getClass().getResource("/image/profile.png"));
        ImageIcon iconLogout = new ImageIcon(getClass().getResource("/image/logout.png"));
        
        // list menu //
        formMenuList menuKasir = new formMenuList(iconkasir, false, null, "kasir", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formHalamanKasir(userId));
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuRiwayatTransaksi = new formMenuList(iconRiwayatTransaksi, false, null, "Riwayat", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formRiwayatTransaksi(userId));
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuProfile = new formMenuList(iconProfile, false, null, "Profile", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formProfileKasir(userId));
            isiContent.repaint();
            isiContent.revalidate();
        });
        
        formMenuList Logout = new formMenuList(iconLogout, false, null, "Logout", (ActionEvent e) -> {
            Login login = new Login();
            login.setVisible(true);
            dispose();
        });
        
        // panggil addMenu //
        listMenuItemKasir.add(menuKasir);
        listMenuItemKasir.add(menuRiwayatTransaksi);
        listMenuItemKasir.add(menuProfile);
        listMenuItemKasir.add(Logout);
          
        listMenuItemKasir.revalidate();
        listMenuItemKasir.repaint();
    }
    private void Date() {
        Date TanggalSekarang = new Date();
        SimpleDateFormat TanggalWaktu = new SimpleDateFormat("dd - MM - yyyy");
        String tanggal = TanggalWaktu.format(TanggalSekarang);
        lb_tanggal.setText(tanggal);
    }
}
