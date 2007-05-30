package dmxeffects;

import com.trolltech.qt.gui.QMenu;

public interface ModuleGUI {
	
	QMenu getMenu();
	
	void runModeEnabled();
	
	void programModeEnabled();
	
	void dmxListenerEnabled();
	
}
