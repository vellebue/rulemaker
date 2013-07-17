package org.rulemaker.engine.expressions;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public abstract class ExpressionSolverTest {
	
	private ExpressionSolver expressionSolver;

	protected void setExpressionSolver(ExpressionSolver expressionSolver) {
		this.expressionSolver = expressionSolver;
	}
	
	@Test
	public void shouldSolveASimpleFieldAcessor() throws Exception {
		Person joe = new Person();
		joe.setName("Joe");
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("person", joe);
		Object result = expressionSolver.eval(contextMap, "person.name");
		assertTrue("Result must be String", result instanceof String);
		assertEquals(result, "Joe");
	}

}
