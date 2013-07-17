package org.rulemaker.engine.expressions;

import java.util.Map;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.core.Predicate;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;

public class OgnlExpressionSolver implements ExpressionSolver {

	public Object eval(Map<String, Object> contextMap, String expression)
			throws InvalidExpressionException {
		try {
			return Ognl.getValue(expression, contextMap,
					buildRootObject(contextMap));
		} catch (OgnlException e) {
			throw new InvalidExpressionException(e);
		}
	}

	/**
	 * Builds OGNL root object based on context map, for each
	 * key-value pair in context map will be a matching property in root
	 * object with the same value.
	 * 
	 * @param contextMap The source context map to build OGNL root object
	 * @return The resulting root object.
	 */
	private Object buildRootObject(Map<String, Object> contextMap) {
		BeanGenerator bg = new BeanGenerator();
		bg.setSuperclass(Object.class);
		bg.setNamingPolicy(new net.sf.cglib.core.NamingPolicy() {
			public String getClassName(String prefix, String source,
					Object key, Predicate names) {
				return prefix + "Impl";
			}
		});
		for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
			String propertyName = entry.getKey();
			Class<?> propertyType = entry.getValue().getClass();
			bg.addProperty(propertyName, propertyType);
		}
		Object beanInst = bg.create();
		try {
			for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
				String propertyName = entry.getKey();
				Object propertyValue = entry.getValue();
				PropertyUtils
						.setProperty(beanInst, propertyName, propertyValue);
			}
		} catch (Exception e) {
			// this should never happen
			throw new RuntimeException(e);
		}
		return beanInst;
	}

}
