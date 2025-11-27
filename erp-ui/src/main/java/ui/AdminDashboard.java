package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.CourseDAO;
import model.AuthUser;
import model.Course;
import service.AdminService;
import service.SettingsService;

public class AdminDashboard extends JFrame {
   private AuthUser currentUser;
   private AdminService adminService;
   private CourseDAO courseDAO;
   private SettingsService settingsService;

   public AdminDashboard(AuthUser var1) {
      this.currentUser = var1;

      try {
         this.adminService = new AdminService();
         this.courseDAO = new CourseDAO();
         this.settingsService = new SettingsService();
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
      JButton changePasswordBtn = new JButton("Change Password");
      changePasswordBtn.addActionListener((var1x) -> {
         this.openChangePassword();
      });
      var2.add(var3);
      var2.add(changePasswordBtn);
      var1.add(var2, "North");
      JPanel var4 = new JPanel(new GridLayout(7, 1, 10, 10));
      var4.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
      JButton var5 = new JButton("Create Auth User");
      var5.addActionListener((var1x) -> {
         this.openCreateUserDialog();
      });
      JButton var6 = new JButton("Create Student ERP Profile");
      var6.addActionListener((var1x) -> {
         this.openCreateStudentProfileDialog();
      });
      JButton var7 = new JButton("Create Instructor ERP Profile");
      var7.addActionListener((var1x) -> {
         this.openCreateInstructorProfileDialog();
      });
      JButton var8 = new JButton("Create Course");
      var8.addActionListener((var1x) -> {
         this.openCreateCourseDialog();
      });
      JButton var9 = new JButton("Create Section");
      var9.addActionListener((var1x) -> {
         this.openCreateSectionDialog();
      });
      JButton var10 = new JButton("List Courses");
      var10.addActionListener((var1x) -> {
         this.listCourses();
      });
      JButton var11 = new JButton("Maintenance Menu");
      var11.addActionListener((var1x) -> {
         this.openMaintenanceMenu();
      });
      JButton var12 = new JButton("Unlock User");
      var12.addActionListener((var1x) -> {
         this.openUnlockUserDialog();
      });
      var4.add(var5);
      var4.add(var6);
      var4.add(var7);
      var4.add(var8);
      var4.add(var9);
      var4.add(var10);
      var4.add(var11);
      var4.add(var12);
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

   private void openCreateStudentProfileDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      JTextField var4 = new JTextField();
      Object[] var5 = new Object[]{"User ID:", var1, "Roll No.:", var2, "Program:", var3, "Year:", var4};
      int var6 = JOptionPane.showConfirmDialog(this, var5, "Create Student Profile", 2);
      if (var6 == 0) {
         try {
            int var7 = Integer.parseInt(var1.getText());
            String var8 = var2.getText();
            String var9 = var3.getText();
            int var10 = Integer.parseInt(var4.getText());
            boolean var11 = this.adminService.createStudentProfile(var7, var8, var9, var10);
            if (var11) {
               JOptionPane.showMessageDialog(this, "Student profile created successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to create student profile.");
            }
         } catch (NumberFormatException var12) {
            JOptionPane.showMessageDialog(this, "Invalid user ID.");
         } catch (SQLException var13) {
            JOptionPane.showMessageDialog(this, "Error creating student profile: " + var13.getMessage());
         }
      }
   }

   private void openCreateInstructorProfileDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      Object[] var5 = new Object[]{"User ID:", var1, "Department:", var2};
      int var6 = JOptionPane.showConfirmDialog(this, var5, "Create Instructor Profile", 2);
      if (var6 == 0) {
         try {
            int var7 = Integer.parseInt(var1.getText());
            String var8 = var2.getText();
            boolean var11 = this.adminService.createInstructorProfile(var7, var8);
            if (var11) {
               JOptionPane.showMessageDialog(this, "Instructor profile created successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to create instructor profile.");
            }
         } catch (NumberFormatException var12) {
            JOptionPane.showMessageDialog(this, "Invalid user ID.");
         } catch (SQLException var13) {
            JOptionPane.showMessageDialog(this, "Error creating instructor profile: " + var13.getMessage());
         }
      }
   }

   private void listCourses() {
      try {
         List<Course> var1 = this.courseDAO.getAllCourses();
         StringBuilder var2 = new StringBuilder();
         for (Course var3 : var1) {
            var2.append("Code: ").append(var3.getCode()).append(", Title: ").append(var3.getTitle()).append(", Credits: ").append(var3.getCredits()).append("\n");
         }
         JOptionPane.showMessageDialog(this, "Courses:\n" + var2.toString());
      } catch (SQLException var4) {
         JOptionPane.showMessageDialog(this, "Error listing courses: " + var4.getMessage());
      }
   }

   private void openMaintenanceMenu() {
      Object[] var1 = new Object[]{"Check Maintenance Mode", "Enable Maintenance", "Disable Maintenance"};
      int var2 = JOptionPane.showOptionDialog(this, "Select maintenance action:", "Maintenance Menu", 0, 3, (javax.swing.Icon)null, var1, var1[0]);
      try {
         if (var2 == 0) {
            String var3 = this.settingsService.getMaintenanceStatus();
            JOptionPane.showMessageDialog(this, "Maintenance mode is " + var3);
         } else if (var2 == 1) {
            this.settingsService.enableMaintenance();
            JOptionPane.showMessageDialog(this, "Maintenance mode enabled.");
         } else if (var2 == 2) {
            this.settingsService.disableMaintenance();
            JOptionPane.showMessageDialog(this, "Maintenance mode disabled.");
         }
      } catch (SQLException var4) {
         JOptionPane.showMessageDialog(this, "Error: " + var4.getMessage());
      }
   }

   private void openUnlockUserDialog() {
      String var1 = JOptionPane.showInputDialog(this, "Enter User ID to unlock:");
      if (var1 != null) {
         try {
            int var2 = Integer.parseInt(var1);
            boolean var3 = this.adminService.unlockUser(var2);
            if (var3) {
               JOptionPane.showMessageDialog(this, "User unlocked successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to unlock user.");
            }
         } catch (NumberFormatException var4) {
            JOptionPane.showMessageDialog(this, "Invalid user ID.");
         } catch (SQLException var5) {
            JOptionPane.showMessageDialog(this, "Error unlocking user: " + var5.getMessage());
         }
      }
   }

   private void openChangePassword() {
      new ChangePasswordFrame(this.currentUser);
   }
}

