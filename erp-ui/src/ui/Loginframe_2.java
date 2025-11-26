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

public class LoginFrame extends JFrame {
   Container c;
   JLabel l1;
   JLabel l2;
   JTextField user;
   JPasswordField password;
   JButton btn;

   public LoginFrame() {
      this.setTitle("Login Form");
      this.setSize(300, 250);
      this.setVisible(true);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.c = this.getContentPane();
      this.c.setLayout((LayoutManager)null);
      this.l1 = new JLabel("Username");
      this.l2 = new JLabel("Password");
      this.l1.setBounds(10, 50, 100, 20);
      this.l2.setBounds(10, 100, 100, 20);
      this.c.add(this.l1);
      this.c.add(this.l2);
      this.user = new JTextField();
      this.user.setBounds(120, 50, 120, 20);
      this.c.add(this.user);
      this.password = new JPasswordField();
      this.password.setBounds(120, 100, 120, 20);
      this.c.add(this.password);
      this.btn = new JButton("Login");
      this.btn.setBounds(100, 150, 70, 20);
      this.btn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            LoginFrame.this.login();
         }
      });
      this.c.add(this.btn);
   }

   private void login() {
      String var1 = this.user.getText();
      String var2 = new String(this.password.getPassword());
      if (!var1.isEmpty() && !var2.isEmpty()) {
         try {
            AuthService var3 = new AuthService();
            AuthUser var4 = var3.login(var1, var2);
            if (var4 == null) {
               JOptionPane.showMessageDialog(this, "Invalid username or password.");
               return;
            }

            this.setVisible(false);
            String var5 = var4.getRole();
            if ("admin".equalsIgnoreCase(var5)) {
               new AdminDashboard(var4);
            } else if ("instructor".equalsIgnoreCase(var5)) {
               new InstructorDashboard(var4);
            } else if ("student".equalsIgnoreCase(var5)) {
               new StudentDashboard(var4);
            } else {
               JOptionPane.showMessageDialog(this, "Unknown role: " + var5);
               this.setVisible(true);
            }
         } catch (SQLException var6) {
            JOptionPane.showMessageDialog(this, "Login error: " + var6.getMessage());
         }

      } else {
         JOptionPane.showMessageDialog(this, "Please enter username and password.");
      }
   }

   public static void main(String[] var0) {
      new LoginFrame();
   }
}
