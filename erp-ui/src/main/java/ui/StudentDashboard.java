package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.AuthUser;
import model.Course;
import model.Grade;
import model.Section;
import service.SettingsService;
import service.StudentService;

public class StudentDashboard extends JFrame {
   private AuthUser currentUser;
   private StudentService studentService;

   public StudentDashboard(AuthUser var1) {
      this.currentUser = var1;

      try {
         this.studentService = new StudentService();
      } catch (SQLException var3) {
         JOptionPane.showMessageDialog(this, "Error initializing student service: " + var3.getMessage());
         return;
      }

      this.setTitle("Student Dashboard - " + var1.getUsername());
      this.setSize(800, 600);
      this.setDefaultCloseOperation(3);
      this.setLocationRelativeTo((Component)null);
      this.initUI();
      this.setVisible(true);
   }

   private void initUI() {
      Container var1 = this.getContentPane();
      var1.setLayout(new BorderLayout());

      // Create top panel for banner and buttons
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.Y_AXIS));

      // Check for maintenance mode and add banner if on
      try {
         SettingsService settingsService = new SettingsService();
         if (settingsService.isMaintenanceOn()) {
            JPanel bannerPanel = new JPanel();
            bannerPanel.setBackground(java.awt.Color.RED);
            JLabel bannerLabel = new JLabel("System is under maintenance. Some features may be unavailable.");
            bannerLabel.setForeground(java.awt.Color.WHITE);
            bannerPanel.add(bannerLabel);
            topPanel.add(bannerPanel);
         }
      } catch (SQLException e) {
         // If error checking maintenance, just continue without banner
      }

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
      topPanel.add(var2);

      var1.add(topPanel, "North");
      JTabbedPane var4 = new JTabbedPane();
      JPanel var5 = new JPanel(new BorderLayout());
      JButton var6 = new JButton("View Course Catalog");
      JTextArea var7 = new JTextArea();
      var7.setEditable(false);
      var6.addActionListener((var2x) -> {
         this.viewCourseCatalog(var7);
      });
      var5.add(var6, "North");
      var5.add(new JScrollPane(var7), "Center");
      var4.addTab("Course Catalog", var5);
      JPanel var8 = new JPanel(new BorderLayout());
      JPanel registerPanel = new JPanel(new FlowLayout());
      JButton var9 = new JButton("Register for Section");
      var9.addActionListener((var1x) -> {
         this.openRegisterDialog();
      });
      registerPanel.add(var9);

      // Add drop section combo box and button
      JComboBox<Section> dropComboBox = new JComboBox<>();
      this.populateDropComboBox(dropComboBox);
      JButton dropButton = new JButton("Drop Section");
      dropButton.addActionListener((var1x) -> {
         this.dropSelectedSection(dropComboBox);
      });
      registerPanel.add(dropComboBox);
      registerPanel.add(dropButton);

      var8.add(registerPanel, "North");
      var4.addTab("Register", var8);
      JPanel var10 = new JPanel(new BorderLayout());
      JButton var11 = new JButton("View Timetable");
      JTextArea var12 = new JTextArea();
      var12.setEditable(false);
      var11.addActionListener((var2x) -> {
         this.viewTimetable(var12);
      });
      var10.add(var11, "North");
      var10.add(new JScrollPane(var12), "Center");
      var4.addTab("Timetable", var10);
      JPanel var13 = new JPanel(new BorderLayout());
      JButton var14 = new JButton("View Grades");
      JTextArea var15 = new JTextArea();
      var15.setEditable(false);
      var14.addActionListener((var2x) -> {
         this.viewGrades(var15);
      });
      var13.add(var14, "North");
      var13.add(new JScrollPane(var15), "Center");
      var4.addTab("Grades", var13);
      JPanel var16 = new JPanel(new BorderLayout());
      JButton var17 = new JButton("Export Transcript");
      var17.addActionListener((var1x) -> {
         this.exportTranscript();
      });
      var16.add(var17, "North");
      var4.addTab("Export Transcript", var16);
      var1.add(var4, "Center");
   }

   private void logout() {
      new LoginFrame();
      this.dispose();
   }

   private void openChangePassword() {
      new ChangePasswordFrame(this.currentUser);
   }

   private void viewCourseCatalog(JTextArea var1) {
      try {
         List<Course> var2 = this.studentService.getAllCourses();
         StringBuilder var3 = new StringBuilder();
         for (Course var4 : var2) {
            var3.append("Code: ").append(var4.getCode()).append(", Title: ").append(var4.getTitle()).append(", Credits: ").append(var4.getCredits()).append("\n");
         }
         var1.setText(var3.toString());
      } catch (SQLException var5) {
         JOptionPane.showMessageDialog(this, "Error viewing course catalog: " + var5.getMessage());
      }
   }

   private void openRegisterDialog() {
      JTextField var1 = new JTextField();
      Object[] var2 = new Object[]{"Section ID:", var1};
      int var3 = JOptionPane.showConfirmDialog(this, var2, "Register for Section", 2);
      if (var3 == 0) {
         try {
            int var4 = Integer.parseInt(var1.getText());
            boolean var5 = this.studentService.registerForSection(this.currentUser.getUserId(), var4, this.currentUser);
            if (var5) {
               JOptionPane.showMessageDialog(this, "Registered successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Registration failed.");
            }
         } catch (NumberFormatException var6) {
            JOptionPane.showMessageDialog(this, "Invalid section ID.");
         } catch (SQLException var7) {
            JOptionPane.showMessageDialog(this, "Error registering: " + var7.getMessage());
         }
      }
   }

   private void viewTimetable(JTextArea var1) {
      try {
         List<Section> var2 = this.studentService.getStudentTimetable(this.currentUser.getUserId(), this.currentUser);
         StringBuilder var3 = new StringBuilder();
         for (Section var4 : var2) {
            var3.append("Section ID: ").append(var4.getsection_id()).append(", Course: ").append(var4.getcourse_id()).append(", Room: ").append(var4.getroom()).append(", Time: ").append(var4.getday_time()).append("\n");
         }
         var1.setText(var3.toString());
      } catch (SQLException var5) {
         JOptionPane.showMessageDialog(this, "Error viewing timetable: " + var5.getMessage());
      }
   }

   private void viewGrades(JTextArea var1) {
      try {
         List<Grade> var2 = this.studentService.getGrades(this.currentUser.getUserId(), this.currentUser);
         StringBuilder var3 = new StringBuilder();
         for (Grade var4 : var2) {
            var3.append("Component: ").append(var4.getComponent()).append(", Score: ").append(var4.getScore()).append(", Final: ").append(var4.getFinal_grade()).append("\n");
         }
         var1.setText(var3.toString());
      } catch (SQLException var5) {
         JOptionPane.showMessageDialog(this, "Error viewing grades: " + var5.getMessage());
      }
   }

   private void exportTranscript() {
      try {
         String var1 = this.studentService.exportTranscriptCSV(this.currentUser.getUserId(), this.currentUser);
         JOptionPane.showMessageDialog(this, "Transcript CSV:\n" + var1);
      } catch (SQLException var2) {
         JOptionPane.showMessageDialog(this, "Error exporting transcript: " + var2.getMessage());
      }
   }

   private void populateDropComboBox(JComboBox<Section> comboBox) {
      try {
         List<Section> enrolledSections = this.studentService.getStudentTimetable(this.currentUser.getUserId(), this.currentUser);
         comboBox.removeAllItems();
         for (Section section : enrolledSections) {
            comboBox.addItem(section);
         }
      } catch (SQLException e) {
         JOptionPane.showMessageDialog(this, "Error loading enrolled sections: " + e.getMessage());
      }
   }

   private void dropSelectedSection(JComboBox<Section> comboBox) {
      Section selectedSection = (Section) comboBox.getSelectedItem();
      if (selectedSection == null) {
         JOptionPane.showMessageDialog(this, "No section selected.");
         return;
      }

      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to drop Section " + selectedSection.getsection_id() + " - " + selectedSection.getcourse_id() + "?", "Confirm Drop", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
         try {
            boolean success = this.studentService.dropSection(this.currentUser.getUserId(), selectedSection.getsection_id(), this.currentUser);
            if (success) {
               JOptionPane.showMessageDialog(this, "Section dropped successfully!");
               this.populateDropComboBox(comboBox); // Refresh the combo box
            } else {
               JOptionPane.showMessageDialog(this, "Failed to drop section.");
            }
         } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error dropping section: " + e.getMessage());
         }
      }
   }
}
