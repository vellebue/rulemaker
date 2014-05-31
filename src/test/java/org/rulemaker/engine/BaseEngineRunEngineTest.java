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
		TestEngine engine = new TestEngine(" (documentType = 'deliveryNote') -> update(#target = 1, documentType = 'bill') ");
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
	
	@Test
	public void shouldCumulateEveryDeliveryNoteIntoASingleBill() throws Exception {
		TestEngine engine = new TestEngine(
				"(documentType = 'deliveryNote', clientId = X)(documentType = 'bill', clientId = X) -> "
				       + "update(#target = 2, amount = ${_2.amount + _1.amount}) delete(#target = 1);"
			+	"(documentType = 'deliveryNote', clientId = X) -> "
					   + "create(#className = 'Document', amount = ${_1.amount}, documentType = 'bill', clientId = ${X}) delete(#target = 1)");
		Document [] deliveryNotes = {new Document(13.34, "deliveryNote", 1), 
				new Document(26.12, "deliveryNote", 2), new Document(72.10, "deliveryNote", 1),
				new Document(15.34, "deliveryNote", 1), new Document(17.23, "deliveryNote", 2) };
		engine.registerClassSinonym("Document", Document.class);
		for (Document aDeliveryNote : deliveryNotes) {
			engine.addFact(aDeliveryNote);
		}
		engine.runEngine();
		assertEquals("There might be two facts 'bills' remaining in fact base", 2, engine.getEngineContext().getFactList().size());
		Document firstBill = (Document) engine.getEngineContext().getFactList().get(0);
		Document secondBill = (Document) engine.getEngineContext().getFactList().get(1);
		assertEquals(100.78, firstBill.getAmount().doubleValue(), 0.01);
		assertEquals(43.35, secondBill.getAmount().doubleValue(), 0.01);
	}

}
