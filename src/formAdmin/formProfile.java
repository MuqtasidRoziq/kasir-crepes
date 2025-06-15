/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formAdmin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import konektor.koneksi;

public class formProfile extends javax.swing.JPanel {

//    private int userId;

    public formProfile(String userId) {
        initComponents();
        getProfile(userId);
    }
    
//  set profil user yang login //
    private void getProfile(String userId){
        String query = "SELECT * FROM user WHERE id_user=?";
        
         try {
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                idUser.setText(rs.getString("id_user"));
                usernameUser.setText(rs.getString("username"));
                passwordUser.setText("******"); 
                roleUser.setText(rs.getString("role"));
            } else {
                JOptionPane.showMessageDialog(null, "Data user tidak ditemukan!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
//  set profil user yang login end //
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgContent = new javax.swing.JPanel();
        iconUser = new javax.swing.JLabel();
        bgUser = new javax.swing.JPanel();
        lbIdUser = new javax.swing.JLabel();
        pnId = new javax.swing.JPanel();
        idUser = new javax.swing.JLabel();
        lbRole = new javax.swing.JLabel();
        pnRole = new javax.swing.JPanel();
        roleUser = new javax.swing.JLabel();
        lbUsername = new javax.swing.JLabel();
        pnUsername = new javax.swing.JPanel();
        usernameUser = new javax.swing.JLabel();
        lbPassword = new javax.swing.JLabel();
        pnPassword = new javax.swing.JPanel();
        passwordUser = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        bgContent.setBackground(new java.awt.Color(255, 255, 255));
        bgContent.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        iconUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Admin-removebg-preview.png"))); // NOI18N

        bgUser.setBackground(new java.awt.Color(255, 255, 255));

        lbIdUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbIdUser.setText("Id User");

        pnId.setBackground(new java.awt.Color(255, 255, 255));
        pnId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        idUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        idUser.setText("Id");

        javax.swing.GroupLayout pnIdLayout = new javax.swing.GroupLayout(pnId);
        pnId.setLayout(pnIdLayout);
        pnIdLayout.setHorizontalGroup(
            pnIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnIdLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(idUser)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        pnIdLayout.setVerticalGroup(
            pnIdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnIdLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(idUser)
                .addContainerGap())
        );

        lbRole.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbRole.setText("Role");

        pnRole.setBackground(new java.awt.Color(255, 255, 255));
        pnRole.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        roleUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        roleUser.setText("Role");

        javax.swing.GroupLayout pnRoleLayout = new javax.swing.GroupLayout(pnRole);
        pnRole.setLayout(pnRoleLayout);
        pnRoleLayout.setHorizontalGroup(
            pnRoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRoleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roleUser)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnRoleLayout.setVerticalGroup(
            pnRoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnRoleLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(roleUser)
                .addContainerGap())
        );

        lbUsername.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbUsername.setText("Username");

        pnUsername.setBackground(new java.awt.Color(255, 255, 255));
        pnUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        usernameUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        usernameUser.setText("Username");

        javax.swing.GroupLayout pnUsernameLayout = new javax.swing.GroupLayout(pnUsername);
        pnUsername.setLayout(pnUsernameLayout);
        pnUsernameLayout.setHorizontalGroup(
            pnUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnUsernameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameUser)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnUsernameLayout.setVerticalGroup(
            pnUsernameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnUsernameLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(usernameUser)
                .addContainerGap())
        );

        lbPassword.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        lbPassword.setText("Password");

        pnPassword.setBackground(new java.awt.Color(255, 255, 255));
        pnPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        passwordUser.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        passwordUser.setText("Password");

        javax.swing.GroupLayout pnPasswordLayout = new javax.swing.GroupLayout(pnPassword);
        pnPassword.setLayout(pnPasswordLayout);
        pnPasswordLayout.setHorizontalGroup(
            pnPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPasswordLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(passwordUser)
                .addContainerGap(132, Short.MAX_VALUE))
        );
        pnPasswordLayout.setVerticalGroup(
            pnPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnPasswordLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(passwordUser)
                .addContainerGap())
        );

        javax.swing.GroupLayout bgUserLayout = new javax.swing.GroupLayout(bgUser);
        bgUser.setLayout(bgUserLayout);
        bgUserLayout.setHorizontalGroup(
            bgUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgUserLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(bgUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbIdUser)
                    .addComponent(pnId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbRole)
                    .addComponent(lbUsername)
                    .addComponent(pnRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbPassword)
                    .addComponent(pnUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(385, Short.MAX_VALUE))
        );
        bgUserLayout.setVerticalGroup(
            bgUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgUserLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lbIdUser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(lbRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout bgContentLayout = new javax.swing.GroupLayout(bgContent);
        bgContent.setLayout(bgContentLayout);
        bgContentLayout.setHorizontalGroup(
            bgContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(iconUser)
                .addGap(28, 28, 28)
                .addComponent(bgUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        bgContentLayout.setVerticalGroup(
            bgContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgContentLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(bgContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconUser, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                    .addComponent(bgUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        add(bgContent, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Profil");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 948, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgContent;
    private javax.swing.JPanel bgUser;
    private javax.swing.JLabel iconUser;
    private javax.swing.JLabel idUser;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbIdUser;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JLabel passwordUser;
    private javax.swing.JPanel pnId;
    private javax.swing.JPanel pnPassword;
    private javax.swing.JPanel pnRole;
    private javax.swing.JPanel pnUsername;
    private javax.swing.JLabel roleUser;
    private javax.swing.JLabel usernameUser;
    // End of variables declaration//GEN-END:variables
}
