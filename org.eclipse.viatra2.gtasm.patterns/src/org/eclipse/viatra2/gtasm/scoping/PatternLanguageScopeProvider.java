/*
 * generated by Xtext
 */
package org.eclipse.viatra2.gtasm.scoping;

import static org.eclipse.emf.ecore.util.EcoreUtil.getRootContainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.viatra2.gtasm.patternLanguage.ClassType;
import org.eclipse.viatra2.gtasm.patternLanguage.EMFType;
import org.eclipse.viatra2.gtasm.patternLanguage.ExpressionHead;
import org.eclipse.viatra2.gtasm.patternLanguage.ExpressionTail;
import org.eclipse.viatra2.gtasm.patternLanguage.Import;
import org.eclipse.viatra2.gtasm.patternLanguage.PatternModel;
import org.eclipse.viatra2.gtasm.patternLanguage.ReferenceType;
import org.eclipse.viatra2.gtasm.patternLanguage.util.PatternLanguageSwitch;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.SimpleScope;
import org.eclipse.xtext.util.PolymorphicDispatcher;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * This class contains custom scoping description. The scoping related to metamodel elements is based on the code from {@link org.eclipse.xtext.xtext.XtextScopeProvider}.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping
 * on how and when to use it 
 * @author Zoltan Ujhelyi
 */
public class PatternLanguageScopeProvider extends AbstractDeclarativeScopeProvider {
	
	@Inject
	private IQualifiedNameConverter qualifiedNameConverter;
	
	/**
	 * {@inheritDoc}
	 * Overridden for debugging purposes.
	 */
	@Override
	protected Predicate<Method> getPredicate(EObject context, EClass type) {
		String methodName = "scope_" + type.getName();
		System.out.println(methodName);
		return PolymorphicDispatcher.Predicates.forName(methodName, 2);
	}

	/**
	 * {@inheritDoc}
	 * Overridden for debugging purposes.
	 */
	@Override
	protected Predicate<Method> getPredicate(EObject context, EReference reference) {
		String methodName = "scope_" + reference.getEContainingClass().getName() + "_" + reference.getName();
		System.out.println(methodName);
		return PolymorphicDispatcher.Predicates.forName(methodName, 2);
	}
	
	public IScope scope_EPackage(Import ctx, EReference ref){
		IScope current = new SimpleScope(IScope.NULLSCOPE, Iterables.transform(EPackage.Registry.INSTANCE.keySet(), new Function<String, IEObjectDescription>() {
			public IEObjectDescription apply(String from) {
				InternalEObject proxyPackage = (InternalEObject) EcoreFactory.eINSTANCE.createEPackage();
				proxyPackage.eSetProxyURI(URI.createURI(from));
				QualifiedName qualifiedName = qualifiedNameConverter.toQualifiedName(from);
				return EObjectDescription.create(qualifiedName, proxyPackage, Collections.singletonMap("nsURI", "true"));
			}
		}));
		return current;
	}
	
	public IScope scope_EClass(EMFType ctx, EReference ref) {
		EObject root = getRootContainer(ctx);
		if (root instanceof PatternModel){
			return createReferencedPackagesScope((PatternModel) root);
		} else 
			return IScope.NULLSCOPE;
	}
	
	protected IScope createClassifierScope(Iterable<EClassifier> classifiers) {
		return new SimpleScope(
				IScope.NULLSCOPE,Iterables.transform(classifiers, new Function<EClassifier, IEObjectDescription>() {
					public IEObjectDescription apply(EClassifier param) {
						return EObjectDescription.create(QualifiedName.create(param.getName()), param);
					}
				}));
	}
	
	protected IScope createReferencedPackagesScope(PatternModel model) {
		final Collection<EClassifier> allClassifiers = new ArrayList<EClassifier>();
		for(Import decl: model.getImports()) {
			if (decl.getEPackage() != null)
				allClassifiers.addAll(decl.getEPackage().getEClassifiers());
		}
		return createClassifierScope(allClassifiers);
	}
	
	public IScope scope_EReference(ExpressionTail ctx, EReference ref) {
		return new ParentScopeProvider().doSwitch(ctx.eContainer());
	}
	
	class ParentScopeProvider extends PatternLanguageSwitch<IScope> {

		@Override
		public IScope caseExpressionHead(ExpressionHead object) {
			EMFType type = object.getType();
			return calculateReferences(type);
		}

		@Override
		public IScope caseExpressionTail(ExpressionTail object) {
			return calculateReferences(object.getType());
		}
		
		private IScope calculateReferences(EMFType type) {
			List<EReference> targetReferences = Collections.emptyList();
			if (type instanceof ReferenceType) {
				EClassifier referredType = ((ReferenceType) type).getTypename().getEType();
				if (referredType instanceof EClass) {
					targetReferences = ((EClass) referredType).getEAllReferences();
				}
			} else if (type instanceof ClassType) {
				targetReferences = ((ClassType) type).getTypename().getEAllReferences();
			}
			return new SimpleScope(IScope.NULLSCOPE, Iterables.transform(targetReferences, new Function<EReference, IEObjectDescription>() {
				public IEObjectDescription apply(EReference param) {
					return EObjectDescription.create(QualifiedName.create(param.getName()), param);
				}
			}));
		}
	}
}
