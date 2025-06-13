
package formAdmin;
import formLogin.Login;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
public class formUtama extends javax.swing.JFrame {

    private String userId;
    private String role;
    private String userName;
    private String Password;

    public formUtama(String username) {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        execute();
        this.userName = username;
        lblUsername.setText(username);
    }
//  set nama user yang login start //    
    public void setUser(String userId, String userName, String Password, String role) {
        this.userId = userId;
        this.userName = userName;
        this.Password = Password;
        this.role = role;
    }
    private void execute() {
        ImageIcon iconHome = new ImageIcon(getClass().getResource("/image/home.png"));
        ImageIcon iconProfile = new ImageIcon(getClass().getResource("/image/profile.png"));
        ImageIcon iconDataUser = new ImageIcon(getClass().getResource("/image/data user.png"));
        ImageIcon iconDataProduk = new ImageIcon(getClass().getResource("/image/data barang.png"));
        ImageIcon iconTransaksi = new ImageIcon(getClass().getResource("/image/transaksi.png"));
        ImageIcon iconLogout = new ImageIcon(getClass().getResource("/image/logout.png"));

        
//      Set menu list // 
        
        formMenuList menuHome = new formMenuList(iconHome, false, null, "Home", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formPenjualan());
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuProfile = new formMenuList(iconProfile, false, null, "Profil", (ActionEvent e) ->{
            isiContent.removeAll();
            isiContent.add(new formProfile(userId));
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuDataUser = new formMenuList(iconDataUser, false, null, "Data User", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formDataUser(userName));
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuDataRasa = new formMenuList(iconDataProduk, false, null, "Data Rasa", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formDataRasa());
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuDatavarian = new formMenuList(iconDataProduk, false, null, "Data varian", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formDataVariant());
            isiContent.repaint();
            isiContent.revalidate();
        });
        formMenuList menuTransaksi = new formMenuList(iconTransaksi, false, null, "Riwayat", (ActionEvent e) -> {
            isiContent.removeAll();
            isiContent.add(new formRTransaksi());
            isiContent.repaint();
            isiContent.revalidate();
        });
        
        formMenuList menuLogout = new formMenuList(iconLogout, false, null, "Logout", (ActionEvent e) -> {
            Login login = new Login();
            login.setVisible(true);
            dispose();
        });
        
        listMenuItem.add(menuHome);
        listMenuItem.add(menuProfile);
        listMenuItem.add(menuDataUser);
        listMenuItem.add(menuDataRasa);
        listMenuItem.add(menuDatavarian);
        listMenuItem.add(menuTransaksi);
        listMenuItem.add(menuLogout);
        
        listMenuItem.revalidate();
        listMenuItem.repaint();
    }
    
//  set menu item end //
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header = new javax.swing.JPanel();
        header1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        listMenu = new javax.swing.JPanel();
        listMenuItem = new javax.swing.JPanel();
        content = new javax.swing.JPanel();
        isiContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        header.setPreferredSize(new java.awt.Dimension(842, 100));

        header1.setBackground(new java.awt.Color(255, 255, 255));
        header1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/market-icons-15733-removebg-preview.png"))); // NOI18N
        jLabel1.setText("Toko Kami");

        lblUsername.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lblUsername.setText("Nama User");

        javax.swing.GroupLayout header1Layout = new javax.swing.GroupLayout(header1);
        header1.setLayout(header1Layout);
        header1Layout.setHorizontalGroup(
            header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(lblUsername)
                .addContainerGap())
        );
        header1Layout.setVerticalGroup(
            header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(header1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, header1Layout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(header1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        listMenu.setPreferredSize(new java.awt.Dimension(255, 449));

        listMenuItem.setBackground(new java.awt.Color(255, 204, 0));
        listMenuItem.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        listMenuItem.setLayout(new javax.swing.BoxLayout(listMenuItem, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout listMenuLayout = new javax.swing.GroupLayout(listMenu);
        listMenu.setLayout(listMenuLayout);
        listMenuLayout.setHorizontalGroup(
            listMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listMenuItem, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
        );
        listMenuLayout.setVerticalGroup(
            listMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(listMenuItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(listMenu, java.awt.BorderLayout.LINE_START);

        isiContent.setBackground(new java.awt.Color(255, 255, 255));
        isiContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        isiContent.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
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
        isiContent.add(new formPenjualan());
        isiContent.repaint();
        isiContent.revalidate();
    }//GEN-LAST:event_formWindowOpened

    
    public static void main(String args[], String username) {
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
            java.util.logging.Logger.getLogger(formUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new formUtama(username).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel isiContent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel listMenu;
    private javax.swing.JPanel listMenuItem;
    // End of variables declaration//GEN-END:variables

}
