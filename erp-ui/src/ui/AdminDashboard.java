package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.AuthUser;
import service.AdminService;

public class AdminDashboard extends JFrame {
   private AuthUser currentUser;
   private AdminService adminService;

   public AdminDashboard(AuthUser var1) {
      this.currentUser = var1;

      try {
         this.adminService = new AdminService();
      } catch (SQLException var3) {
         JOptionPane.showMessageDialog(this, "Error initializing admin service: " + var3.getMessage());
         return;
      }

      this.setTitle("Admin Dashboard - " + var1.getUsername());
      this.setSize(600, 400);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.initUI();
      this.setVisible(true);
   }

   private void initUI() {
      Container var1 = this.getContentPane();
      var1.setLayout(new BorderLayout());
      JPanel var2 = new JPanel(new FlowLayout(2));
      JButton var3 = new JButton("Logout");
      var3.addActionListener((var1x) -> {
         this.logout();
      });
      var2.add(var3);
      var1.add(var2, "North");
      JPanel var4 = new JPanel(new GridLayout(4, 1, 10, 10));
      var4.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      JButton var5 = new JButton("Create User");
      var5.addActionListener((var1x) -> {
         this.openCreateUserDialog();
      });
      JButton var6 = new JButton("Create Course");
      var6.addActionListener((var1x) -> {
         this.openCreateCourseDialog();
      });
      JButton var7 = new JButton("Create Section");
      var7.addActionListener((var1x) -> {
         this.openCreateSectionDialog();
      });
      JButton var8 = new JButton("Toggle Maintenance Mode");
      var8.addActionListener((var1x) -> {
         this.toggleMaintenance();
      });
      var4.add(var5);
      var4.add(var6);
      var4.add(var7);
      var4.add(var8);
      var1.add(var4, "Center");
   }

   private void logout() {
      new LoginFrame();
      this.dispose();
   }

   private void openCreateUserDialog() {
      JTextField var1 = new JTextField();
      JPasswordField var2 = new JPasswordField();
      JComboBox var3 = new JComboBox(new String[]{"admin", "instructor", "student"});
      JComboBox var4 = new JComboBox(new String[]{"active", "inactive"});
      Object[] var5 = new Object[]{"Username:", var1, "Password:", var2, "Role:", var3, "Status:", var4};
      int var6 = JOptionPane.showConfirmDialog(this, var5, "Create User", 2);
      if (var6 == 0) {
         try {
            String var7 = var1.getText();
            String var8 = new String(var2.getPassword());
            String var9 = (String)var3.getSelectedItem();
            String var10 = (String)var4.getSelectedItem();
            AuthUser var11 = this.adminService.createAuthUser(var7, var9, var8, var10);
            JOptionPane.showMessageDialog(this, "User created successfully! User ID: " + var11.getUserId());
         } catch (SQLException var12) {
            JOptionPane.showMessageDialog(this, "Error creating user: " + var12.getMessage());
         }
      }

   }

   private void openCreateCourseDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      Object[] var4 = new Object[]{"Course ID:", var1, "Course Name:", var2, "Credits:", var3};
      int var5 = JOptionPane.showConfirmDialog(this, var4, "Create Course", 2);
      if (var5 == 0) {
         try {
            String var6 = var1.getText();
            String var7 = var2.getText();
            int var8 = Integer.parseInt(var3.getText());
            boolean var9 = this.adminService.createCourse(var6, var7, var8);
            if (var9) {
               JOptionPane.showMessageDialog(this, "Course created successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to create course.");
            }
         } catch (NumberFormatException var10) {
            JOptionPane.showMessageDialog(this, "Invalid credits value.");
         } catch (SQLException var11) {
            JOptionPane.showMessageDialog(this, "Error creating course: " + var11.getMessage());
         }
      }

   }

   private void openCreateSectionDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      JTextField var4 = new JTextField();
      JTextField var5 = new JTextField();
      JTextField var6 = new JTextField();
      JTextField var7 = new JTextField();
      Object[] var8 = new Object[]{"Course ID:", var1, "Instructor ID:", var2, "Day/Time:", var3, "Room:", var4, "Capacity:", var5, "Semester:", var6, "Year:", var7};
      int var9 = JOptionPane.showConfirmDialog(this, var8, "Create Section", 2);
      if (var9 == 0) {
         try {
            String var10 = var1.getText();
            int var11 = Integer.parseInt(var2.getText());
            String var12 = var3.getText();
            String var13 = var4.getText();
            int var14 = Integer.parseInt(var5.getText());
            String var15 = var6.getText();
            int var16 = Integer.parseInt(var7.getText());
            boolean var17 = this.adminService.createSection(var10, var11, var12, var13, var14, var15, var16);
            if (var17) {
               JOptionPane.showMessageDialog(this, "Section created successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to create section.");
            }
         } catch (NumberFormatException var18) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
         } catch (SQLException var19) {
            JOptionPane.showMessageDialog(this, "Error creating section: " + var19.getMessage());
         }
      }

   }

   private void toggleMaintenance() {
      try {
         this.adminService.enableMaintenance();
         JOptionPane.showMessageDialog(this, "Maintenance mode toggled.");
      } catch (SQLException var2) {
         JOptionPane.showMessageDialog(this, "Error toggling maintenance: " + var2.getMessage());
      }

   }
}

