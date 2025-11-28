import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import ui.LoginFrame;

public class MainApp {
   public MainApp() {
   }

   public static void main(String[] var0) {
      try {
         UIManager.setLookAndFeel(new FlatLightLaf());
      } catch (Exception e) {
         e.printStackTrace();
      }

      SwingUtilities.invokeLater(() -> {
         new LoginFrame();
      });
   }
}
