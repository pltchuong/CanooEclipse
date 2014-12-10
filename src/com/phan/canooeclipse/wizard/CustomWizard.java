package com.phan.canooeclipse.wizard;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;

import com.phan.canooeclipse.CanooEclipse;

@SuppressWarnings("restriction")
public class CustomWizard extends JavaProjectWizard {

	@Override
	public boolean performFinish() {
		NewJavaProjectWizardPageOne fMainPage = (NewJavaProjectWizardPageOne)getPage("NewJavaProjectWizardPageOne");
		boolean result = super.performFinish();
		
		try {
			makeTemplate(new File(FileLocator.toFileURL(new URL("platform:/plugin/com.phan.canooeclipse/resources/template")).toURI()), ResourcesPlugin.getWorkspace().getRoot().getProject(fMainPage.getProjectName()).getFolder("resources"));
		} catch (Exception e) {
			CanooEclipse.getDefault().getConsole().newMessageStream().println(CanooEclipse.getStackTrace(e));
		}
		
		return result;
	}

	private void makeTemplate(File sourceFolder, IFolder targetFolder) throws Exception {
		targetFolder.create(true, true, null);
		for (File subSourceFolder : sourceFolder.listFiles()) {
			if(subSourceFolder.isDirectory()) {
				IFolder subTargetFolder = targetFolder.getFolder(subSourceFolder.getName());
				makeTemplate(subSourceFolder, subTargetFolder);
			} else {
				targetFolder.getFile(subSourceFolder.getName()).create(subSourceFolder.toURI().toURL().openConnection().getInputStream(), IResource.NONE, null);
			}
		}
	}
	
}
