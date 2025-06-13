package formAdmin;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;

public class formMenuList extends javax.swing.JPanel {
    private ActionListener act;
    private boolean showing = false;
    public ArrayList<formMenuList> getSubMenu() {
        return subMenu;
    }
    private final ArrayList<formMenuList> subMenu = new ArrayList<>();

    public formMenuList(Icon icon, boolean sbm, Icon iconSub, String listMenu, ActionListener act, formMenuList...subMenu) {
        initComponents();
        lb_icon.setIcon(icon);
        lb_listMenu.setText(listMenu);
        lb_iconSub.setIcon(iconSub);
        lb_iconSub.setVisible(sbm);

        if (act != null) {
            this.act = act;
        }
        this.setSize(new Dimension(Integer.MAX_VALUE, 45));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        this.setMinimumSize(new Dimension(Integer.MAX_VALUE, 45));
        for (formMenuList subMenu1 : subMenu) {
            this.subMenu.add(subMenu1);
            subMenu1.setVisible(false);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_listMenu = new javax.swing.JLabel();
        lb_icon = new javax.swing.JLabel();
        lb_iconSub = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 204, 0));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_listMenu.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        lb_listMenu.setText("Menu Item...");
        add(lb_listMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 17, 120, 38));
        add(lb_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 39, 70));
        add(lb_iconSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 39, 70));
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (showing) {
            hideMenu();
        }else {
            showMenu();
        }
        if (act !=null) {
            act.actionPerformed(null);
        }
    }//GEN-LAST:event_formMousePressed

    private void hideMenu() {
        new Thread (new Runnable() {
            @Override
            public void run() {
                for (int i = subMenu.size() - 1; i >= 0; i--){
                    sleep();
                    subMenu.get(i).setVisible(false);
                    subMenu.get(i).hideMenu();
                }
                getParent().repaint();
                getParent().revalidate();
                showing = false;
            }
        }).start();
    }
    
    private void showMenu() {
        new Thread (new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < subMenu.size(); i++){
                    sleep();
                    subMenu.get(i).setVisible(true);
                }
                showing = true;
                getParent().repaint();
                getParent().revalidate();
                
            }
        }).start();
        
    }
    
    private void sleep() {
        try{
            Thread.sleep(20);
        } catch (Exception e) {
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lb_icon;
    private javax.swing.JLabel lb_iconSub;
    private javax.swing.JLabel lb_listMenu;
    // End of variables declaration//GEN-END:variables
}
