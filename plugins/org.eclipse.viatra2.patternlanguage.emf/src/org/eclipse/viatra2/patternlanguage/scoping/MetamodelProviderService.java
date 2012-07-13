package org.eclipse.viatra2.patternlanguage.scoping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.viatra2.patternlanguage.EcoreGenmodelRegistry;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.SimpleScope;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class MetamodelProviderService implements IMetamodelProvider {

	private static final Logger LOG = Logger.getLogger(MetamodelProviderService.class);
	
	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;
	
	protected EcoreGenmodelRegistry genmodelRegistry = new EcoreGenmodelRegistry();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.viatra2.patternlanguage.scoping.IMetamodelProvider#
	 * getAllMetamodelObjects()
	 */
	@Override
	public IScope getAllMetamodelObjects(EObject context) {
		Set<String> packageURIs = new HashSet<String>(
				EPackage.Registry.INSTANCE.keySet());
		Iterable<IEObjectDescription> metamodels = Iterables.transform(packageURIs,
				new Function<String, IEObjectDescription>() {
					public IEObjectDescription apply(String from) {
						EPackage ePackage = EPackage.Registry.INSTANCE
								.getEPackage(from);
						// InternalEObject proxyPackage = (InternalEObject)
						// EcoreFactory.eINSTANCE.createEPackage();
						// proxyPackage.eSetProxyURI(URI.createURI(from));
						QualifiedName qualifiedName = qualifiedNameConverter
								.toQualifiedName(from);
						// return EObjectDescription.create(qualifiedName,
						// proxyPackage,
						// Collections.singletonMap("nsURI", "true"));
						return EObjectDescription.create(qualifiedName,
								ePackage,
								Collections.singletonMap("nsURI", "true"));
					}
				});
		return new SimpleScope(IScope.NULLSCOPE, metamodels);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.viatra2.patternlanguage.scoping.IMetamodelProvider#getPackage
	 * (java.lang.String)
	 */
	@Override
	public EPackage loadEPackage(String packageUri, ResourceSet resourceSet) {
		if (EPackage.Registry.INSTANCE.containsKey(packageUri)) {
			return EPackage.Registry.INSTANCE.getEPackage(packageUri);
		}
		URI uri = null;
		try {
			 uri = URI.createURI(packageUri);
			if (uri.fragment() == null) {
				Resource resource = resourceSet.getResource(uri, true);
				return (EPackage) resource.getContents().get(0);
			}
			return (EPackage) resourceSet.getEObject(uri, true);
		} catch(RuntimeException ex) {
			if (uri != null && uri.isPlatformResource()) {
				String platformString = uri.toPlatformString(true);
				URI platformPluginURI = URI.createPlatformPluginURI(platformString, true);
				return loadEPackage(platformPluginURI.toString(), resourceSet);
			}
			LOG.trace("Cannot load package with URI '" + packageUri + "'", ex);
			return null;
		}
	}

	@Override
	public boolean isGeneratedCodeAvailable(EPackage ePackage, ResourceSet set) {
		return genmodelRegistry.findGenPackage(ePackage.getNsURI(), set) != null;
	}
}
