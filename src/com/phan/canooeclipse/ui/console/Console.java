package com.phan.canooeclipse.ui.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class Console extends MessageConsole {

	public Console() {
		super("CanooEclipse Console", null);
	}
	
	public void openConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = manager.getConsoles();
		boolean exists = false;
		for (IConsole existConsole : consoles) {
			if (existConsole.getName().equals(this.getName())) {
				exists = true;
			}
		}
		if (!exists) {
			manager.addConsoles(new IConsole[] { this });
		}
	}
	
	@Override
	protected void dispose() {
	}
}
