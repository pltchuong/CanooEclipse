package com.phan.canooeclipse;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.phan.canooeclipse.ui.console.Console;

public class CanooEclipse extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "CanooEclipsePlugin";
	
	private static CanooEclipse plugin;
	private Console console;
	
	public CanooEclipse() {
		super();
		plugin = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		console = new Console();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
	
	public Console getConsole() {
		return console;
	}
	
	public static CanooEclipse getDefault() {
		return plugin;
	}
	
	public static String getStackTrace(Exception exception) {
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	} 
	
}
