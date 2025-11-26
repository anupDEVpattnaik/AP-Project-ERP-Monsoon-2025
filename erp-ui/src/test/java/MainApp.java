import javax.swing.SwingUtilities;
import ui.LoginFrame;

public class MainApp {
   public MainApp() {
   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(() -> {
         new LoginFrame();
      });
   }
}
