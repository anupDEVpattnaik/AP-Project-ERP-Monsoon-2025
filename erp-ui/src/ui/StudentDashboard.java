package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import model.AuthUser;
import model.Course;
import model.Grade;
import model.Section;
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
      JPanel var2 = new JPanel(new FlowLayout(2));
      JButton var3 = new JButton("Logout");
      var3.addActionListener((var1x) -> {
         this.logout();
      });
      var2.add(var3);
      var1.add(var2, "North");
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
      JButton var9 = new JButton("Register for Section");
      var9.addActionListener((var1x) -> {
         this.openRegisterDialog();
      });
      var8.add(var9, "North");
      var4.addTab("Register", var8);
      JPanel var10 = new JPanel(new BorderLayout());
      JButton var11 = new JButton("View My Timetable");
      JTextArea var12 = new JTextArea();
      var12.setEditable(false);
      var11.addActionListener((var2x) -> {
         this.viewTimetable(var12);
      });
      var10.add(var11, "North");
      var10.add(new JScrollPane(var12), "Center");
      var4.addTab("My Timetable", var10);
      JPanel var13 = new JPanel(new BorderLayout());
      JButton var14 = new JButton("View My Grades");
      JTextArea var15 = new JTextArea();
      var15.setEditable(false);
      var14.addActionListener((var2x) -> {
         this.viewGrades(var15);
      });
      var13.add(var14, "North");
      var13.add(new JScrollPane(var15), "Center");
      var4.addTab("My Grades", var13);
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

   private void viewCourseCatalog(JTextArea var1) {
      try {
         List var2 = this.studentService.getAllCourses();
         StringBuilder var3 = new StringBuilder();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Course var5 = (Course)var4.next();
            var3.append("Code: ").append(var5.getCode()).append(", Title: ").append(var5.getTitle()).append(", Credits: ").append(var5.getCredits()).append("\n");
         }

         var1.setText(var3.toString());
      } catch (SQLException var6) {
         JOptionPane.showMessageDialog(this, "Error viewing course catalog: " + var6.getMessage());
      }

   }

   private void openRegisterDialog() {
      String var1 = JOptionPane.showInputDialog(this, "Enter Section ID to register:");
      if (var1 != null) {
         try {
            int var2 = Integer.parseInt(var1);
            boolean var3 = this.studentService.registerForSection(this.currentUser.getUserId(), var2, this.currentUser);
            if (var3) {
               JOptionPane.showMessageDialog(this, "Registered successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Registration failed.");
            }
         } catch (NumberFormatException var4) {
            JOptionPane.showMessageDialog(this, "Invalid section ID.");
         } catch (SQLException var5) {
            JOptionPane.showMessageDialog(this, "Error registering: " + var5.getMessage());
         }
      }

   }

   private void viewTimetable(JTextArea var1) {
      try {
         List var2 = this.studentService.getStudentTimetable(this.currentUser.getUserId(), this.currentUser);
         StringBuilder var3 = new StringBuilder();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Section var5 = (Section)var4.next();
            var3.append("Course: ").append(var5.getcourse_id()).append(", Time: ").append(var5.getday_time()).append(", Room: ").append(var5.getroom()).append("\n");
         }

         var1.setText(var3.toString());
      } catch (SQLException var6) {
         JOptionPane.showMessageDialog(this, "Error viewing timetable: " + var6.getMessage());
      }

   }

   private void viewGrades(JTextArea var1) {
      try {
         List var2 = this.studentService.getGrades(this.currentUser.getUserId(), this.currentUser);
         StringBuilder var3 = new StringBuilder();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Grade var5 = (Grade)var4.next();
            var3.append("Component: ").append(var5.getComponent()).append(", Score: ").append(var5.getScore()).append(", Final: ").append(var5.getFinal_grade()).append("\n");
         }

         var1.setText(var3.toString());
      } catch (SQLException var6) {
         JOptionPane.showMessageDialog(this, "Error viewing grades: " + var6.getMessage());
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
}

