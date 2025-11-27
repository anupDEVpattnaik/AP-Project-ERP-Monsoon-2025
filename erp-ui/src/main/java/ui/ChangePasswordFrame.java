package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.AuthUser;
import service.AuthService;

public class ChangePasswordFrame extends JFrame {
   Container c;
   JLabel l1;
   JLabel l2;
   JLabel l3;
   JPasswordField oldPassword;
   JPasswordField newPassword;
   JPasswordField confirmPassword;
   JButton btn;
   AuthUser currentUser;

   public ChangePasswordFrame(AuthUser user) {
      this.currentUser = user;
      this.setTitle("Change Password - " + user.getUsername());
      this.setSize(350, 250);
      this.setVisible(true);
      this.setDefaultCloseOperation(2);
      this.setLocationRelativeTo((Component)null);
      this.c = this.getContentPane();
      this.c.setLayout((LayoutManager)null);
      this.l1 = new JLabel("Old Password");
      this.l2 = new JLabel("New Password");
      this.l3 = new JLabel("Confirm New Password");
      this.l1.setBounds(10, 50, 120, 20);
      this.l2.setBounds(10, 100, 120, 20);
      this.l3.setBounds(10, 150, 150, 20);
      this.c.add(this.l1);
      this.c.add(this.l2);
      this.c.add(this.l3);
      this.oldPassword = new JPasswordField();
      this.oldPassword.setBounds(160, 50, 120, 20);
      this.c.add(this.oldPassword);
      this.newPassword = new JPasswordField();
      this.newPassword.setBounds(160, 100, 120, 20);
      this.c.add(this.newPassword);
      this.confirmPassword = new JPasswordField();
      this.confirmPassword.setBounds(160, 150, 120, 20);
      this.c.add(this.confirmPassword);
      this.btn = new JButton("Change Password");
      this.btn.setBounds(100, 180, 140, 20);
      this.btn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ChangePasswordFrame.this.changePassword();
         }
      });
      this.c.add(this.btn);
   }

   private void changePassword() {
      String var1 = new String(this.oldPassword.getPassword());
      String var2 = new String(this.newPassword.getPassword());
      String var3 = new String(this.confirmPassword.getPassword());
      if (!var1.isEmpty() && !var2.isEmpty() && !var3.isEmpty()) {
         if (!var2.equals(var3)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            return;
         }

         try {
            AuthService var4 = new AuthService();
            boolean var5 = var4.changePassword(this.currentUser.getUserId(), var1, var2);
            if (var5) {
               JOptionPane.showMessageDialog(this, "Password changed successfully!");
               this.setVisible(false);
            } else {
               JOptionPane.showMessageDialog(this, "Password change failed.");
            }
         } catch (SQLException var6) {
            JOptionPane.showMessageDialog(this, "Error changing password: " + var6.getMessage());
         }

      } else {
         JOptionPane.showMessageDialog(this, "Please fill all fields.");
      }
   }
}
