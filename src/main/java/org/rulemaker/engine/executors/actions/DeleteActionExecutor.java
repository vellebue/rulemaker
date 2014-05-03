package org.rulemaker.engine.executors.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.executors.exception.ExecutionException;

/**
 * Executor used to delete facts from facts database.
 * <br>
 * Arguments: 
 * <ul>
 *   <li>
 *   #target: Number of condition in condition list 
 *                     (from 1 to N) that matches the selected fact 
 *                     to be deleted.
 *   </li>
 * </ul>
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class DeleteActionExecutor extends AbstractTargetActionExecutor {
	

	public void onExecute(Map<String, Object> conditionMatchingMap)
			throws ExecutionException {
		Integer targetIndex = (Integer) getSharpArgumentsMap().get("target");
		String targetFactName = "_" + targetIndex;
		Object targetObjectToDelete = conditionMatchingMap.get(targetFactName);
		Map<String, List<Object>> factsBase = getEngineContext().getFactBase();
		removeFactFromFactBase(factsBase, targetObjectToDelete);
	}

	private void removeFactFromFactBase(Map<String, List<Object>> factsBase, Object objectToDelete) {
		boolean deleted = false;
		Iterator<String> domainsIterator = factsBase.keySet().iterator();
		while(!deleted && domainsIterator.hasNext()) {
			String aDomainName = domainsIterator.next();
			if (factsBase.get(aDomainName) != null) {
				List<Object> factsList = factsBase.get(aDomainName);
				Object factToDelete = null;
				for (int i = 0 ; (i < factsList.size()) && (factToDelete == null); i++) {
					if (factsList.get(i) == objectToDelete) {
						factToDelete = objectToDelete;
					}
				}
				if (factToDelete != null) {
					factsList.remove(factToDelete);
					deleted = true;
				}
			}
		}
	}
}
