package org.rulemaker.engine.executors.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.executors.ActionError;

public class CreateActionExecutorTest {
	
	public static class TestFact {
		
		private String member1;
		private Integer member2;
		
		public String getMember1() {
			return member1;
		}
		
		public void setMember1(String member1) {
			this.member1 = member1;
		}
		
		public Integer getMember2() {
			return member2;
		}
		
		public void setMember2(Integer member2) {
			this.member2 = member2;
		}
	}

	@Test
	public void shouldCreateAnInstanceGivenItsClassNameOnDefaultDomain() throws Exception {
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("className", 
				"org.rulemaker.engine.executors.actions.CreateActionExecutorTest$TestFact");
		EngineContext context = new EngineContext(null);
		regularArgumentsMap.put("member1", "Joe");
		regularArgumentsMap.put("member2", 32);
		CreateActionExecutor executor = new CreateActionExecutor();
		executor.setEngineContext(context);
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		// There will not be validation errors
		assertTrue((errors == null) || (errors.size() == 0));
		executor.execute(null);
		// There must be a registered fact with the given data
		List<Object> factList = context.getFactList();
		assertEquals(1, factList.size());
		assertTrue(factList.get(0) instanceof  TestFact);
		TestFact fact = (TestFact) factList.get(0);
		assertEquals("Joe", fact.getMember1());
		assertEquals(new Integer(32), fact.getMember2());
	}
	
	@Test
	public void shouldCreateAnInstanceGivenItsClassNameOnGivenDomain() throws Exception {
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("domain", "myDomain");
		sharpArgumentsMap.put("className", 
				"org.rulemaker.engine.executors.actions.CreateActionExecutorTest$TestFact");
		EngineContext context = new EngineContext(null);
		regularArgumentsMap.put("member1", "Joe");
		regularArgumentsMap.put("member2", 32);
		CreateActionExecutor executor = new CreateActionExecutor();
		executor.setEngineContext(context);
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		// There will not be validation errors
		assertTrue((errors == null) || (errors.size() == 0));
		executor.execute(null);
		// There must be a registered fact with the given data
		List<Object> factList = context.getFactBase().get("myDomain");
		assertEquals(1, factList.size());
		assertTrue(factList.get(0) instanceof  TestFact);
		TestFact fact = (TestFact) factList.get(0);
		assertEquals("Joe", fact.getMember1());
		assertEquals(new Integer(32), fact.getMember2());
	}
}
