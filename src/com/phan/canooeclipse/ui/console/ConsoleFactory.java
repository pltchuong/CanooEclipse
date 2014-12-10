package com.phan.canooeclipse.ui.console;

import org.eclipse.ui.console.IConsoleFactory;

import com.phan.canooeclipse.CanooEclipse;

public class ConsoleFactory implements IConsoleFactory {
	
	@Override
	public void openConsole() {
		CanooEclipse.getDefault().getConsole().openConsole();
	}
}
