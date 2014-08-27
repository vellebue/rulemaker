package org.rulemaker.engine;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseEngineRunEngineTest {
	
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
	
	@Test
	public void shouldTurnEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBillwithNullKFactor() throws Exception {
		Document [] deliveryNotes = {new Document(13.34, "deliveryNote", 1), 
				new Document(26.12, "deliveryNote", 2)};
		testTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBill(deliveryNotes, null);
		// After running the engine there must be two payed bills
		assertEquals("payedBill", deliveryNotes[0].getDocumentType());
		assertEquals("payedBill", deliveryNotes[1].getDocumentType());
	}

	@Test(expected = EngineException.class)
	public void shouldFailWithExceptionWhenTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBillWithKFactorEqualsToOne() 
			throws Exception {
		Document [] deliveryNotes = {new Document(13.34, "deliveryNote", 1), 
				new Document(26.12, "deliveryNote", 2)};
		testTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBill(deliveryNotes, 1);
	}
	
	@Test
	public void shouldPerformSucessfulWhenTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBillWithKFactorEqualsToTwo() 
			throws Exception {
		Document [] deliveryNotes = {new Document(13.34, "deliveryNote", 1), 
				new Document(26.12, "deliveryNote", 2), new Document(17.23, "deliveryNote", 2)};
		testTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBill(deliveryNotes, 2);
		// After running the engine there must be three payed bills		
		assertEquals("payedBill", deliveryNotes[0].getDocumentType());
		assertEquals("payedBill", deliveryNotes[1].getDocumentType());
		assertEquals("payedBill", deliveryNotes[2].getDocumentType());
	}
	
	private void testTurningEveryDeliveryNoteIntoBillAndEveryBillIntoPayedBill(Document [] documents, Integer kFactor) throws Exception {
		TestEngine engine = new TestEngine(" (documentType = 'deliveryNote') -> update(#target = 1, documentType = 'bill'); "
				+ " (documentType = 'bill') -> update(#target = 1, documentType = 'payedBill') ");
		engine.registerClassSinonym("Document", Document.class);
		for (Document aDeliveryNote : documents) {
			engine.addFact(aDeliveryNote);
		}
		engine.setkFactor(kFactor);
		engine.runEngine();
	}
}
