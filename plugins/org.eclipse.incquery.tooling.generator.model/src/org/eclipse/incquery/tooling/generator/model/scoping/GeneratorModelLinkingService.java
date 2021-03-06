/*******************************************************************************
 * Copyright (c) 2010-2012, Zoltan Ujhelyi, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi - initial API and implementation
 *******************************************************************************/
package org.eclipse.incquery.tooling.generator.model.scoping;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.incquery.tooling.generator.model.generatorModel.GeneratorModelPackage;
import org.eclipse.incquery.tooling.generator.model.generatorModel.GeneratorModelReference;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

public class GeneratorModelLinkingService extends DefaultLinkingService {

	private static final String PLUGIN_URI_SCHEME = "plugin";

    private static final Logger LOG = Logger.getLogger(GeneratorModelLinkingService.class);

    @Inject
    private IValueConverterService valueConverterService;

    @Override
    public List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) {
        if (ref == GeneratorModelPackage.eINSTANCE.getGeneratorModelReference_Genmodel()
                && context instanceof GeneratorModelReference && node instanceof ILeafNode) {
            return getGenModel((GeneratorModelReference) context, (ILeafNode) node);
        }
        return super.getLinkedObjects(context, ref, node);
    }

    private List<EObject> getGenModel(GeneratorModelReference context, ILeafNode text) {
        String nsUri = getMetamodelNsURI(text);
        if (nsUri == null) {
            return Collections.emptyList();
        }
        GenModel pack = loadGenmodel(nsUri, context.eResource().getResourceSet());
        if (pack != null) {
            return Collections.<EObject> singletonList(pack);
        }
        return Collections.emptyList();
    }

    private String getMetamodelNsURI(ILeafNode text) {
        try {
            return (String) valueConverterService.toValue(text.getText(),
                    getLinkingHelper().getRuleNameFrom(text.getGrammarElement()), text);
        } catch (ValueConverterException e) {
            LOG.debug("Exception on leaf '" + text.getText() + "'", e);
            return null;
        }
    }

	/**
	 * Resolve a "plugin:/plugin.id/folder/resource" - like URI to a location
	 * URL<br>
	 * This method can find resources from any workspace or target platform
	 * plugin. This method uses {@link PluginRegistry}.findModel(String
	 * pluginId) to search for plugins. This method will return the workspace
	 * version if available, or the target-platform plugin with the highest
	 * version.
	 * 
	 * @param resourceOrNsURI
	 *            An URI with "plugin:/" scheme
	 * @return The "file:" or "jar:" URL string of the resource, or null if the
	 *         given plug-in could not be found.
	 */
	private String resolvePluginURI(String resourceOrNsURI) {
		String path = resourceOrNsURI.substring(PLUGIN_URI_SCHEME.length() + 2);
		int i = path.indexOf('/');
		Preconditions.checkArgument(i != -1, "Invalid URI: '" + resourceOrNsURI
				+ "', a resource must be selected in the plugin!");

		String pluginId = path.substring(0, i);
		path = path.substring(i);

		IPluginModelBase pluginbase = PluginRegistry.findModel(pluginId);
		if (pluginbase != null) {
			String location = pluginbase.getInstallLocation();
			if (location.endsWith(".jar")) {
				return "jar:file:" + location + "!" + path;
			} else {
				return "file:" + pluginbase.getInstallLocation() + path;
			}
		} else {
			LOG.trace("Invalid plugin ID: '" + pluginId + "'");
			return null;
		}

	}

    private GenModel loadGenmodel(String resourceOrNsURI, ResourceSet resourceSet) {
		/* If the given URI is a plugin URI, try to resolve it to a resource */
		if (resourceOrNsURI.startsWith(PLUGIN_URI_SCHEME + ":/")) {
			resourceOrNsURI = resolvePluginURI(resourceOrNsURI);
			if (resourceOrNsURI == null) {
				return null;
			}
		}
        URI uri = null;
        try {
            uri = URI.createURI(resourceOrNsURI);
            if (uri.fragment() == null) {
                Resource resource = resourceSet.getResource(uri, true);
                return (GenModel) resource.getContents().get(0);
            }
            return (GenModel) resourceSet.getEObject(uri, true);
        } catch (IllegalArgumentException ex) {
            LOG.trace("Invalid package URI: '" + resourceOrNsURI + "'", ex);
            return null;
        } catch (RuntimeException ex) {
            if (uri != null && uri.isPlatformResource()) {
                String platformString = uri.toPlatformString(true);
                URI platformPluginURI = URI.createPlatformPluginURI(platformString, true);
                return loadGenmodel(platformPluginURI.toString(), resourceSet);
            }
            LOG.trace("Cannot load package with URI '" + resourceOrNsURI + "'", ex);
            return null;
        }
    }
}
