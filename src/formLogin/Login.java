package formLogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import konektor.koneksi;
import formAdmin.formUtama;
import formKasir.formMenuUtama;

import java.awt.Frame;
import loging.loging.ActivityLogger;

public class Login extends javax.swing.JFrame {
    
    public Login() {
        initComponents();
        inputUsername.requestFocus();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        lbUsername = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        inputUsername = new javax.swing.JTextField();
        btnExit = new javax.swing.JButton();
        inputPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lbLogin1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(587, 385));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        background.setBackground(new java.awt.Color(255, 204, 0));

        lbUsername.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbUsername.setText("USERNAME");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel3.setText("PASSWORD");

        inputUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inputUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputUsernameFocusLost(evt);
            }
        });
        inputUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputUsernameActionPerformed(evt);
            }
        });

        btnExit.setBackground(new java.awt.Color(255, 0, 0));
        btnExit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("EXIT");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        inputPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                inputPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                inputPasswordFocusLost(evt);
            }
        });
        inputPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputPasswordActionPerformed(evt);
            }
        });

        btnLogin.setBackground(new java.awt.Color(0, 204, 0));
        btnLogin.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("LOGIN");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lbLogin1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        lbLogin1.setText("LOGIN");

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                .addComponent(lbUsername)
                .addGap(249, 249, 249))
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLogin))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGap(242, 242, 242)
                        .addComponent(jLabel3)))
                .addGap(136, 136, 136))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                .addComponent(lbLogin1)
                .addGap(218, 218, 218))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(lbLogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(lbUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExit)
                    .addComponent(btnLogin))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(562, 385));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void inputUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputUsernameActionPerformed
        inputPassword.requestFocus();
    }//GEN-LAST:event_inputUsernameActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void inputPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputPasswordActionPerformed
        inputUsername.requestFocus();
        login();
    }//GEN-LAST:event_inputPasswordActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formMousePressed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        login();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void inputUsernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputUsernameFocusGained
        String inputU = inputUsername.getText();
        if(inputU.equals("username")){
            inputUsername.setText("");
        }
    }//GEN-LAST:event_inputUsernameFocusGained

    private void inputUsernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputUsernameFocusLost
        String inputU = inputUsername.getText();
        if(inputU.equals("") ||inputU.equals("masukan username")){
            inputUsername.setText("username");
        }
    }//GEN-LAST:event_inputUsernameFocusLost
 
    private void inputPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputPasswordFocusGained
        String inputP= inputPassword.getText();
        if(inputP.equals("*****")){
            inputPassword.setText("");
        }
    }//GEN-LAST:event_inputPasswordFocusGained

    private void inputPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_inputPasswordFocusLost
        String inputP = inputPassword.getText();
        if(inputP.equals("") ||inputP.equals("masukan password")){
            inputPassword.setText("*****");
        }
    }//GEN-LAST:event_inputPasswordFocusLost

    // Fungsi Login Start //
    private void login() {
    Connection conn = koneksi.getConnection(); 
    String sql = "SELECT * FROM user WHERE username=? AND password=?";   
    String username = inputUsername.getText();
    String password = new String(inputPassword.getPassword());

    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();          

        if (rs.next()) {
            String userId = rs.getString("id_user");
            String role = rs.getString("role");
            String userName = rs.getString("username");
            String Password = rs.getString("password");

            // Karena nama dan email tidak ada di DB, bisa pakai default
            String nama = userName;
            String email = "-";

            if (role.equalsIgnoreCase("admin")) {                    
                JOptionPane.showMessageDialog(this, "Berhasil login sebagai admin");
                ActivityLogger.logLogin(nama);
                formUtama mainMenu = new formUtama(username);
                mainMenu.setVisible(true);
                mainMenu.setUser(userId, username, password, role);
                mainMenu.setExtendedState(Frame.MAXIMIZED_BOTH);
                this.dispose();

            } else if (role.equalsIgnoreCase("kasir")) {
                JOptionPane.showMessageDialog(this, "Berhasil login sebagai kasir");
                ActivityLogger.logLogin(nama);
                formMenuUtama mainMenu = new formMenuUtama();
                mainMenu.setVisible(true);
                mainMenu.setUser(userId, nama, email, role, userName, Password);
                mainMenu.setExtendedState(Frame.MAXIMIZED_BOTH);
                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah");
                ActivityLogger.logError(nama + " gagal login karena peran tidak dikenali.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username atau password salah");
            ActivityLogger.logError("Gagal login karena username/password salah.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        ActivityLogger.logError("Kesalahan login pada user " + username + ": " + e.getMessage());
    }  
}
    
    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel background;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JPasswordField inputPassword;
    private javax.swing.JTextField inputUsername;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbLogin1;
    private javax.swing.JLabel lbUsername;
    // End of variables declaration//GEN-END:variables
}
