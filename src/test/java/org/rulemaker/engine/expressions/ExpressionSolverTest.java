package org.rulemaker.engine.expressions;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;
import org.rulemaker.engine.matcher.ChainedMap;

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
		assertEquals("Joe", result);
	}
	
	@Test
	public void shouldSolveAVectorAccesorExpression() throws Exception {
		Person joe = new Person();
		joe.setRegisterCodes(new String[]{"012312","761231"});
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("person", joe);
		Object result = expressionSolver.eval(contextMap, "person.registerCodes[1]");
		assertTrue("Result must be String", result instanceof String);
		assertEquals("761231", result);
	}
	
	@Test
	public void shouldSolveACombinedExpressionAccesor() throws Exception {
		Person joe = new Person();
		joe.setName("Joe");
		joe.setSalary(1500.0);
		Person paul = new Person();
		paul.setName("Paul");
		paul.setSalary(2000.0);
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("joe", joe);
		contextMap.put("paul", paul);
		Object result = expressionSolver.eval(contextMap, "joe.salary + paul.salary");
		assertTrue("Result must be double", result instanceof Double);
		assertEquals("3500.0", result.toString());
	}
	
	@Test
	public void shouldSolveAnExpressionWithVariableNamesPrecededByUnderscoreCharacter() throws Exception {
		Person joe = new Person();
		joe.setName("Joe");
		Map<String, Object> innerMap = new HashMap<String, Object>();
		innerMap.put("_1", joe);
		ChainedMap<String, Object> contextMap = 
				new ChainedMap<String, Object>(new HashMap<String, Object>(), 
						new ChainedMap<String, Object>(innerMap)); 
		Object result = expressionSolver.eval(contextMap, "_1.name");
		assertTrue("Result must be String", result instanceof String);
		assertEquals("Joe", result);
		assertNotNull("Inner map must not be altered", innerMap.get("_1"));
	}
	
	@Test
	public void shouldReturnNullValueAccessingNullValueFields() throws Exception {
		Person joe = new Person();
		joe.setName(null);
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("joe", joe);
		Object result = expressionSolver.eval(contextMap, "joe.name");
		assertNull(result);
	}
	
	@Test(expected = InvalidExpressionException.class)
	public void shouldNotifyInvalidExpressionAsInvalidExpressionException() throws Exception {
		Person joe = new Person();
		joe.setName("Joe");
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("person", joe);
		expressionSolver.eval(contextMap, "person.nonExixtingField");
	}

}
