package org.rulemaker.engine;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rulemaker.model.Rule;

public class BaseEngineRunEngineTest {
	
	private class TestEngine extends BaseEngine {
		
		public TestEngine(String rulesText) throws EngineException {
			super(rulesText);
			// TODO Auto-generated constructor stub
		}

		public TestEngine(List<Rule> rules) {
			super(rules);
			// TODO Auto-generated constructor stub
		}	
	}
	
	@Test
	public void shouldTurnEveryDeliveryNoteIntoBill() throws Exception {
		BaseEngineRunEngineTest.TestEngine engine = new BaseEngineRunEngineTest.TestEngine(" (documentType = 'deliveryNote') -> update(#target = 1, documentType = 'bill') ");
		Document [] deliveryNotes = {new Document(13.34, "deliveryNote", 1), 
				new Document(26.12, "deliveryNote", 1), new Document(72.10, "deliveryNote", 2)};
		for (Document aDeliveryNote : deliveryNotes) {
			engine.addFact(aDeliveryNote);
		}
		engine.runEngine();
		for (Object anEngineObject : engine.getEngineContext().getFactList()) {
			Document aDocument = (Document) anEngineObject;
			assertEquals("bill", aDocument.getDocumentType());
		}
	}

}
