package readInesData;

import javax.swing.UIManager;

public class mainClass {

	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
		
		mainWindow mainFrame = new mainWindow();
		mainFrame.setVisible(true);
	}

}
