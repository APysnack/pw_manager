package pw_manager;

import views.AppWindow;

public class App {
    public App() {
        createApp();
    }

    public void createApp() {
    	// creates a new instance of the password manager application
        AppWindow appWindow = new AppWindow();
        appWindow.setSize(900, 550);
        appWindow.setVisible(true);
    }
}
