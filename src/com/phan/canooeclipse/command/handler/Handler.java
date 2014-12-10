package com.phan.canooeclipse.command.handler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.PropertyResourceBundle;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.phan.canooeclipse.CanooEclipse;
import com.phan.canooeclipse.ui.console.Console;

public class Handler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final Console console = CanooEclipse.getDefault().getConsole();
		final MessageConsoleStream out = console.newMessageStream();
		
		console.clearConsole();
		console.openConsole();
		console.activate();

		IFile testXml = ((FileEditorInput) HandlerUtil.getActiveEditorInput(event)).getFile();
		IFile canooProperties = testXml.getProject().getFile("resources/canooeclipse.properties");
		if(canooProperties == null) {
			MessageDialog.openError(shell, "Error", "This project is missing resources/canooeclipse.properties");
			return null;
		}
		
		String projectHome = testXml.getProject().getLocation().toOSString();
		
		PropertyResourceBundle config = null;
		try {
			config = new PropertyResourceBundle(canooProperties.getContents());
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error", "resources/canooeclipse.properties is corrupted");
			out.println(CanooEclipse.getStackTrace(e));
			return null;
		}
		
		ProcessBuilder ps = new ProcessBuilder();
		ps.redirectErrorStream(true);
		try {
			ps.command(
				config.getString("webtest.home") + "/bin/webtest.bat",
				"-buildfile", projectHome + "/resources/canooeclipse-test-runner.xml",
				"-Dwebtest.home", config.getString("webtest.home"),
				"-Dproject.home", projectHome,
				"-Dhost", config.getString("host"),
				"-Dport", config.getString("port"),
				"-Dcontext", config.getString("context"),
//				"-Djdbc.classpath", config.getString("jdbc.classpath"),
//				"-Djdbc.driver", config.getString("jdbc.driver"),
//				"-Djdbc.url", config.getString("jdbc.url"),
//				"-Djdbc.userid", config.getString("jdbc.userid"),
//				"-Djdbc.passwd", config.getString("jdbc.passwd"),
//				"-Djdbc.schema", config.getString("jdbc.schema"),
				"-Dwebtest.testdir", testXml.getParent().getLocation().toOSString(),
				"-Dwebtest.testfile", testXml.getName()
			);
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error", "resources/canooeclipse.properties is missing some configurations");
			out.println(CanooEclipse.getStackTrace(e));
			return null;
		}
		
		try {
			final Process process = ps.start();
			
			new Thread() {
			    public void run() {
				    try {
				    	BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
					    String line;
				    	while ((line = input.readLine()) != null) {
				    		out.println("> " + line);
				    	}
			        } catch (Exception e) {
			        	MessageDialog.openError(shell, "Error", "Something wrong with Eclipse ProcessBuilder output");
			        	out.println(CanooEclipse.getStackTrace(e));
			        }
			    }
			}.start();
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error", "Something wrong with Eclipse ProcessBuilder execution");
			out.println(CanooEclipse.getStackTrace(e));
			return null;
		}
		
		return null;
	}
}
