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
import javax.swing.JTextField;
import model.AuthUser;
import model.Section;
import service.InstructorService;

public class InstructorDashboard extends JFrame {
   private AuthUser currentUser;
   private InstructorService instructorService;

   public InstructorDashboard(AuthUser var1) {
      this.currentUser = var1;

      try {
         this.instructorService = new InstructorService();
      } catch (SQLException var3) {
         JOptionPane.showMessageDialog(this, "Error initializing instructor service: " + var3.getMessage());
         return;
      }

      this.setTitle("Instructor Dashboard - " + var1.getUsername());
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
      JButton var6 = new JButton("View My Sections");
      JTextArea var7 = new JTextArea();
      var7.setEditable(false);
      var6.addActionListener((var2x) -> {
         this.viewSections(var7);
      });
      var5.add(var6, "North");
      var5.add(new JScrollPane(var7), "Center");
      var4.addTab("My Sections", var5);
      JPanel var8 = new JPanel(new BorderLayout());
      JButton var9 = new JButton("Enter Grades");
      var9.addActionListener((var1x) -> {
         this.openEnterGradesDialog();
      });
      var8.add(var9, "North");
      var4.addTab("Enter Grades", var8);
      JPanel var10 = new JPanel(new BorderLayout());
      JButton var11 = new JButton("Export Gradebook");
      var11.addActionListener((var1x) -> {
         this.exportGradebook();
      });
      var10.add(var11, "North");
      var4.addTab("Export Gradebook", var10);
      var1.add(var4, "Center");
   }

   private void logout() {
      new LoginFrame();
      this.dispose();
   }

   private void viewSections(JTextArea var1) {
      try {
         List var2 = this.instructorService.getMySections(this.currentUser.getUserId(), this.currentUser);
         StringBuilder var3 = new StringBuilder();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Section var5 = (Section)var4.next();
            var3.append("Section ID: ").append(var5.getsection_id()).append(", Course: ").append(var5.getcourse_id()).append(", Room: ").append(var5.getroom()).append(", Time: ").append(var5.getday_time()).append("\n");
         }

         var1.setText(var3.toString());
      } catch (SQLException var6) {
         JOptionPane.showMessageDialog(this, "Error viewing sections: " + var6.getMessage());
      }

   }

   private void openEnterGradesDialog() {
      JTextField var1 = new JTextField();
      JTextField var2 = new JTextField();
      JTextField var3 = new JTextField();
      JTextField var4 = new JTextField();
      JTextField var5 = new JTextField();
      Object[] var6 = new Object[]{"Section ID:", var1, "Student ID:", var2, "Component:", var3, "Score:", var4, "Final Grade (optional):", var5};
      int var7 = JOptionPane.showConfirmDialog(this, var6, "Enter Grade", 2);
      if (var7 == 0) {
         try {
            int var8 = Integer.parseInt(var1.getText());
            int var9 = Integer.parseInt(var2.getText());
            String var10 = var3.getText();
            float var11 = Float.parseFloat(var4.getText());
            String var12 = var5.getText().isEmpty() ? null : var5.getText();
            boolean var13 = this.instructorService.enterComponentGrade(var8, var9, var10, var11, var12, this.currentUser);
            if (var13) {
               JOptionPane.showMessageDialog(this, "Grade entered successfully!");
            } else {
               JOptionPane.showMessageDialog(this, "Failed to enter grade.");
            }
         } catch (NumberFormatException var14) {
            JOptionPane.showMessageDialog(this, "Invalid number format.");
         } catch (SQLException var15) {
            JOptionPane.showMessageDialog(this, "Error entering grade: " + var15.getMessage());
         }
      }

   }

   private void exportGradebook() {
      String var1 = JOptionPane.showInputDialog(this, "Enter Section ID to export:");
      if (var1 != null) {
         try {
            int var2 = Integer.parseInt(var1);
            String var3 = this.instructorService.exportSectionGradebookCSV(var2, this.currentUser);
            JOptionPane.showMessageDialog(this, "Gradebook CSV:\n" + var3);
         } catch (NumberFormatException var4) {
            JOptionPane.showMessageDialog(this, "Invalid section ID.");
         } catch (SQLException var5) {
            JOptionPane.showMessageDialog(this, "Error exporting gradebook: " + var5.getMessage());
         }
      }

   }
}

