package org.rulemaker.engine.expressions;

import java.util.Map;

import org.rulemaker.engine.expressions.exception.InvalidExpressionException;

/**
 * Common interface for expression solvers used by rule engines.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public interface ExpressionSolver {
	
	/**
	 * Evaluates an expression where the variable names referenced
	 * into that expression are mapped by  a provided map.
	 * 
	 * @param contextMap The map that binds each variable 
	 * 				     referenced into the expression with its
	 * 					 corresponding java object.
	 * 
	 * @param expression The expression that must be solved
	 *                   and its build in terms of variables in context map.
	 *                   
	 * @return The resulting object of expression evaluation.
	 * 
	 * @throws InvalidExpressionException If expression evaluation process fails.
	 */
	public Object eval(Map<String, Object> contextMap, String expression) 
			throws InvalidExpressionException;

}
