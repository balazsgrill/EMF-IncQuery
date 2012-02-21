package org.eclipse.viatra2.emf.incquery.core.project;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.pde.core.project.IBundleClasspathEntry;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.pde.core.project.IBundleProjectService;
import org.eclipse.pde.core.project.IRequiredBundleDescription;
import org.eclipse.viatra2.emf.incquery.core.IncQueryPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;

/**
 * @author Zoltan Ujhelyi, Istvan Rath
 * 
 *         Support methods for creating a project
 */
public class IncQueryProjectSupport {

	/**
	 * Creates a new IncQuery project: a plug-in project with src and src-gen
	 * folders and specific dependencies.
	 * 
	 * @param description
	 * @param proj
	 * @param monitor
	 * @throws CoreException
	 * @throws OperationCanceledException
	 */
	public static void createProject(IProjectDescription description,
			IProject proj, IProgressMonitor monitor) throws CoreException,
			OperationCanceledException {
		BundleContext context = null;
		ServiceReference ref = null;

		try {

			monitor.beginTask("", 2000);
			/* Creating plug-in information */
			context = IncQueryPlugin.plugin.context;
			ref = context.getServiceReference(IBundleProjectService.class
					.getName());
			IBundleProjectService service = (IBundleProjectService) context
					.getService(ref);
			IBundleProjectDescription bundleDesc = service.getDescription(proj);
			bundleDesc.setBundleName(description.getName());
			bundleDesc.setBundleVersion(new Version(0, 0, 1, "qualifier"));
			bundleDesc.setSingleton(true);
			bundleDesc.setTargetVersion(IBundleProjectDescription.VERSION_3_6);
			bundleDesc.setSymbolicName(description.getName());
			bundleDesc.setExtensionRegistry(true);
			bundleDesc.setBundleClasspath(new IBundleClasspathEntry[] {
					service.newBundleClasspathEntry(new Path(
							IncQueryNature.SRC_DIR), null, null),
					service.newBundleClasspathEntry(new Path(
							IncQueryNature.SRCGEN_DIR), null, null) });
			bundleDesc
					.setExecutionEnvironments(new String[] { IncQueryNature.EXECUTION_ENVIRONMENT });
			// Adding dependencies
			IRequiredBundleDescription[] reqBundles = new IRequiredBundleDescription[] {
					service.newRequiredBundle("org.eclipse.pde.core", null,
							false, false),
					service.newRequiredBundle("org.eclipse.emf.ecore", null,
							false, false),
					service.newRequiredBundle("org.eclipse.emf.transaction",
							null, false, true),
					service.newRequiredBundle(
							"org.eclipse.viatra2.emf.incquery.runtime", null,
							false, true) };
			bundleDesc.setRequiredBundles(reqBundles);
			bundleDesc.apply(monitor);
			// Adding IncQuery-specific natures
			ProjectGenerationHelper.addNatures(proj, new String[] {
					IncQueryNature.NATURE_ID,
					"org.eclipse.xtext.ui.shared.xtextNature" }, monitor);
		} finally {
			monitor.done();
			if (context != null && ref != null)
				context.ungetService(ref);
		}
	}

	/**
	 * Fills the VPML file of the project with nEMF content generated according
	 * to the EPackage given.
	 * 
	 * @param ePackages
	 *            a collection of {@link EPackage}s
	 * @param project
	 *            the INCQuery project we are targeting
	 * @param modelPluginID
	 *            the plugin ID of the emf.model project that will be added to
	 *            the required bundles
	 * @throws Exception
	 * @throws IOException
	 * @throws CoreException
	 */
	public static void addPackageDependencies(Collection<EPackage> ePackages,
			IProject project, String modelPluginID) throws Exception,
			IOException, CoreException {

		// add modelPluginID to the list of required bundles
		BundleContext context = IncQueryPlugin.plugin.context;
		ServiceReference ref = context
				.getServiceReference(IBundleProjectService.class.getName());
		IBundleProjectService service = (IBundleProjectService) context
				.getService(ref);
		IBundleProjectDescription bundleDesc = service.getDescription(project);
		IRequiredBundleDescription[] requiredBundles = bundleDesc
				.getRequiredBundles();
		if (requiredBundles != null) {
			boolean found = false;
			for (IRequiredBundleDescription desc : requiredBundles) {
				if (desc.getName().contentEquals(modelPluginID))
					found = true;
			}
			if (!found) {
				IRequiredBundleDescription[] newBundles = Arrays.copyOf(
						requiredBundles, requiredBundles.length + 1);
				newBundles[requiredBundles.length] = service.newRequiredBundle(
						modelPluginID, null, false, true);
				bundleDesc.setRequiredBundles(newBundles);
				bundleDesc.apply(new NullProgressMonitor());
			}
		} else {
			bundleDesc
					.setRequiredBundles(new IRequiredBundleDescription[] { service
							.newRequiredBundle(modelPluginID, null, false, true) });
			bundleDesc.apply(new NullProgressMonitor());
		}
		context.ungetService(ref);
	}

}
