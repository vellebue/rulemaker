package org.rulemaker.engine.expressions;

import org.junit.Before;

public class OgnlExpressionSolverTest extends ExpressionSolverTest {
	
	@Before
	public void setupExpressionsolverTest() {
		setExpressionSolver(new OgnlExpressionSolver());
	}

}
