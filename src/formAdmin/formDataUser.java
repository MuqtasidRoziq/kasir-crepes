package formAdmin;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import konektor.koneksi;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import loging.loging.ActivityLogger;

public class formDataUser extends javax.swing.JPanel {

    private final String nama;
    
    public formDataUser(String nama) {
        initComponents();
        this.nama =nama;
        loadDataUser();

//      Menonaktifkan tombol Edit dan Delete saat pertama kali dibuka
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

//      Menambahkan ListSelectionListener ke tabel
        tabUSer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                // Cek jika ada baris yang dipilih
                if (!event.getValueIsAdjusting() && tabUSer.getSelectedRow() != -1) {
                    btnEdit.setEnabled(true);
                    btnHapus.setEnabled(true);
                } else {
                    btnEdit.setEnabled(false);
                    btnHapus.setEnabled(false);
                }
            }
        });
    }

// Load Data User //
    private void loadDataUser() {
        DefaultTableModel model = (DefaultTableModel) tabUSer.getModel();
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String query = "SELECT id_user, username, password, role FROM user";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String idUser = rs.getString("id_user");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                // Tambahkan data ke model tabel
                model.addRow(new Object[]{idUser, username, password, role});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//  hapus data user //    
    private void hapusDataUser() {
        int selectedRow = tabUSer.getSelectedRow();
        if (selectedRow != -1) {
            String idUser = tabUSer.getValueAt(selectedRow, 0).toString();
            String namaUser = tabUSer.getValueAt(selectedRow, 1).toString();
            // Konfirmasi dan hapus data dari database berdasarkan ID
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus " + namaUser + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = koneksi.getConnection();
                    Statement st = con.createStatement();
                    String deleteQuery = "DELETE FROM user WHERE id_user = '" + idUser + "'";
                    st.executeUpdate(deleteQuery);
                    ActivityLogger.logDeleteUser(this.nama, namaUser);
                    loadDataUser();
                    JOptionPane.showMessageDialog(this, "Data user berhasil dihapus.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
                    ActivityLogger.logError(this.nama + "gagal menghapus " + namaUser);
                }
            }
        }
    }
//  ungsi hapus data user end //   

//  Fungsi seacrh user //
    private void searchUser() {
        String keyword = jTextField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tabUSer.getModel();

        if (keyword.isEmpty()) {
            // Jika keyword kosong, tampilkan pesan dan muat ulang data asli
            loadDataUser();
            JOptionPane.showMessageDialog(this, "Kolom pencarian harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset tabel sebelum menampilkan hasil pencarian
        model.setRowCount(0);

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();

            String query = "SELECT id_user, username, password, role"
                    + "FROM user WHERE id_user LIKE '%" + keyword + "%' "
                    + "OR username LIKE '%" + keyword + "%' ";

            ResultSet rs = st.executeQuery(query);

            // Jika data ditemukan, tambahkan ke model tabel
            while (rs.next()) {
                String idUser = rs.getString("id_user");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                model.addRow(new Object[]{idUser, username, password, role});
            }

            // Pesan jika tidak ada data ditemukan
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Data user tidak ditemukan.", "Pencarian", JOptionPane.INFORMATION_MESSAGE);
                ActivityLogger.logError(this.nama + "gagal melakukan pencarian");
                loadDataUser(); // Tampilkan kembali data asli jika tidak ada hasil
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari data.");
            ActivityLogger.logError(this.nama + "gagal melakukan pencarian");
        }
    }
//  search user//    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formTambahUser = new javax.swing.JDialog();
        lblUsername = new javax.swing.JLabel();
        btnSaveAddUser = new javax.swing.JButton();
        btnCancelAddUser = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lblTambahUser = new javax.swing.JLabel();
        lblIdRasa = new javax.swing.JLabel();
        txtIdUser = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        selectRole = new javax.swing.JComboBox<>();
        formEditUser = new javax.swing.JDialog();
        btnCancelAddUser1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        lblEditUser = new javax.swing.JLabel();
        lblIdRasa1 = new javax.swing.JLabel();
        txtIdUser1 = new javax.swing.JTextField();
        txtUsername1 = new javax.swing.JTextField();
        txtPassword1 = new javax.swing.JTextField();
        lblPassword1 = new javax.swing.JLabel();
        lblRole1 = new javax.swing.JLabel();
        selectRole1 = new javax.swing.JComboBox<>();
        lblUsername1 = new javax.swing.JLabel();
        btnSaveAddUser1 = new javax.swing.JButton();
        bgDataUSer = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabUSer = new javax.swing.JTable();
        btnHapus1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsername.setText("Username");

        btnSaveAddUser.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveAddUser.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveAddUser.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAddUser.setText("Save");
        btnSaveAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddUserActionPerformed(evt);
            }
        });

        btnCancelAddUser.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelAddUser.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelAddUser.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelAddUser.setText("Cancel");
        btnCancelAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddUserActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        lblTambahUser.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTambahUser.setText("Tambah User");

        lblIdRasa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdRasa.setText("Id User");

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPassword.setText("Password");

        lblRole.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRole.setText("Role");

        selectRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "kasir", " " }));

        javax.swing.GroupLayout formTambahUserLayout = new javax.swing.GroupLayout(formTambahUser.getContentPane());
        formTambahUser.getContentPane().setLayout(formTambahUserLayout);
        formTambahUserLayout.setHorizontalGroup(
            formTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formTambahUserLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(lblTambahUser)
                .addGap(63, 63, 63))
            .addGroup(formTambahUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(selectRole, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIdUser, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblIdRasa, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblUsername, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRole, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(formTambahUserLayout.createSequentialGroup()
                        .addComponent(btnSaveAddUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelAddUser)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formTambahUserLayout.setVerticalGroup(
            formTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formTambahUserLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(lblTambahUser)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdRasa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRole)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectRole, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(formTambahUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveAddUser)
                    .addComponent(btnCancelAddUser))
                .addGap(11, 11, 11))
        );

        btnCancelAddUser1.setBackground(new java.awt.Color(0, 0, 255));
        btnCancelAddUser1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCancelAddUser1.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelAddUser1.setText("Cancel");
        btnCancelAddUser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelAddUser1ActionPerformed(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        lblEditUser.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblEditUser.setText("Edit User");

        lblIdRasa1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblIdRasa1.setText("Id User");

        lblPassword1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPassword1.setText("Password");

        lblRole1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRole1.setText("Role");

        selectRole1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "kasir", " " }));

        lblUsername1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsername1.setText("Username");

        btnSaveAddUser1.setBackground(new java.awt.Color(0, 0, 255));
        btnSaveAddUser1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSaveAddUser1.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAddUser1.setText("Save");
        btnSaveAddUser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddUser1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formEditUserLayout = new javax.swing.GroupLayout(formEditUser.getContentPane());
        formEditUser.getContentPane().setLayout(formEditUserLayout);
        formEditUserLayout.setHorizontalGroup(
            formEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditUserLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(lblEditUser)
                .addGap(88, 88, 88))
            .addGroup(formEditUserLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(selectRole1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIdUser1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblIdRasa1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtUsername1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblUsername1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtPassword1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addComponent(lblPassword1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblRole1, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(formEditUserLayout.createSequentialGroup()
                        .addComponent(btnSaveAddUser1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelAddUser1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        formEditUserLayout.setVerticalGroup(
            formEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formEditUserLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(lblEditUser)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblIdRasa1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblUsername1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPassword1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblRole1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectRole1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(formEditUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveAddUser1)
                    .addComponent(btnCancelAddUser1))
                .addGap(11, 11, 11))
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        bgDataUSer.setBackground(new java.awt.Color(255, 255, 255));
        bgDataUSer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnTambah.setBackground(new java.awt.Color(255, 153, 0));
        btnTambah.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 153, 0));
        btnEdit.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(255, 153, 0));
        btnHapus.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(255, 153, 0));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cari.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tabUSer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id User", "Username", "Password", "Role"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabUSer.setGridColor(new java.awt.Color(0, 0, 0));
        tabUSer.setSelectionBackground(new java.awt.Color(51, 51, 255));
        tabUSer.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tabUSer.setShowGrid(false);
        jScrollPane1.setViewportView(tabUSer);

        btnHapus1.setBackground(new java.awt.Color(255, 153, 0));
        btnHapus1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        btnHapus1.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus1.setText("Batal");
        btnHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapus1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bgDataUSerLayout = new javax.swing.GroupLayout(bgDataUSer);
        bgDataUSer.setLayout(bgDataUSerLayout);
        bgDataUSerLayout.setHorizontalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(bgDataUSerLayout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37))))
        );
        bgDataUSerLayout.setVerticalGroup(
            bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgDataUSerLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bgDataUSerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(bgDataUSer, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Bell MT", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Data User");
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

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        txtIdUser.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        selectRole.setSelectedIndex(0);
        formTambahUser.pack();
        formTambahUser.setLocationRelativeTo(this);
        formTambahUser.setModal(true);
        formTambahUser.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchUser();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int selectedRows = tabUSer.getSelectedRow();
        if (selectedRows != -1) {
            String IdUser = tabUSer.getValueAt(selectedRows, 0).toString();
            String username = tabUSer.getValueAt(selectedRows, 1).toString();
            String password = tabUSer.getValueAt(selectedRows, 2).toString();
            String Role = tabUSer.getValueAt(selectedRows, 3).toString();

            txtIdUser1.setText(IdUser);
            txtUsername1.setText(username);
            txtPassword1.setText(password);
            selectRole1.setSelectedItem(Role);
            formEditUser.pack();
            formEditUser.setLocationRelativeTo(this);
            formEditUser.setModal(true);
            formEditUser.setVisible(true);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusDataUser();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        searchUser();
        btnSearch.requestFocus();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnSaveAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddUserActionPerformed
        String idUser = txtIdUser.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String role = selectRole.getSelectedItem().toString();

        if (idUser.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(formTambahUser, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String insertQuery = "INSERT INTO user (id_user, username, password, role) VALUES ('" + idUser + "', '" + username + "', '" + password + "', '" + role + "')";
            st.executeUpdate(insertQuery);

            formTambahUser.dispose();
            loadDataUser();
            JOptionPane.showMessageDialog(this,username + " berhasil ditambahkan.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(formTambahUser, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveAddUserActionPerformed

    private void btnCancelAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAddUserActionPerformed
        formTambahUser.dispose();
    }//GEN-LAST:event_btnCancelAddUserActionPerformed

    private void btnCancelAddUser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAddUser1ActionPerformed
        formEditUser.dispose();
    }//GEN-LAST:event_btnCancelAddUser1ActionPerformed

    private void btnSaveAddUser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddUser1ActionPerformed
        String idUser = txtIdUser1.getText().trim();
        String username = txtUsername1.getText().trim();
        String password = txtPassword1.getText().trim();
        String role = selectRole1.getSelectedItem().toString();

        if (idUser.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(formEditUser, "Semua kolom harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = koneksi.getConnection();
            Statement st = con.createStatement();
            String updateQuery = "UPDATE user SET username = '" + username + "', password = '" + password + "', role = '" + role + "' WHERE id_user = '" + idUser + "'";
            st.executeUpdate(updateQuery);

            formEditUser.dispose();
            loadDataUser();
            JOptionPane.showMessageDialog(this, username + " berhasil diupdate.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(formEditUser, "Gagal menambahkan data: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSaveAddUser1ActionPerformed

    private void btnHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus1ActionPerformed
        loadDataUser();
        
    }//GEN-LAST:event_btnHapus1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bgDataUSer;
    private javax.swing.JButton btnCancelAddUser;
    private javax.swing.JButton btnCancelAddUser1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnSaveAddUser;
    private javax.swing.JButton btnSaveAddUser1;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambah;
    private javax.swing.JDialog formEditUser;
    private javax.swing.JDialog formTambahUser;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblEditUser;
    private javax.swing.JLabel lblIdRasa;
    private javax.swing.JLabel lblIdRasa1;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPassword1;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblRole1;
    private javax.swing.JLabel lblTambahUser;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsername1;
    private javax.swing.JComboBox<String> selectRole;
    private javax.swing.JComboBox<String> selectRole1;
    private javax.swing.JTable tabUSer;
    private javax.swing.JTextField txtIdUser;
    private javax.swing.JTextField txtIdUser1;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPassword1;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtUsername1;
    // End of variables declaration//GEN-END:variables
}
