package org.eclipse.viatra2.emf.incquery.triggerengine.specific;

import org.eclipse.viatra2.emf.incquery.runtime.api.IMatcherFactory;
import org.eclipse.viatra2.emf.incquery.runtime.api.IPatternMatch;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryEngine;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.viatra2.emf.incquery.runtime.exception.IncQueryException;
import org.eclipse.viatra2.emf.incquery.triggerengine.api.Rule;
import org.eclipse.viatra2.emf.incquery.triggerengine.api.RuleEngine;
import org.eclipse.viatra2.emf.incquery.triggerengine.api.RuleFactory;

public class DefaultRuleFactory implements RuleFactory {

	@Override
	public <Match extends IPatternMatch, Matcher extends IncQueryMatcher<Match>> Rule<Match> createRule(
			IncQueryEngine engine,
			IMatcherFactory<Matcher> factory, 
			boolean upgradedStateUsed,
			boolean disappearedStateUsed, 
			boolean allowMultipleFiring) {
		
		Rule<Match> rule = null;
		try {
			rule = new RecordingRule<Match>(RuleEngine.getInstance().getOrCreateAgenda(engine), 
											factory.getMatcher(engine), 
											upgradedStateUsed, 
											disappearedStateUsed, 
											allowMultipleFiring);
		} catch (IncQueryException e) {
			engine.getLogger().error("Error while creating RecordingRule!", e);
		}
		return rule;
	}

}