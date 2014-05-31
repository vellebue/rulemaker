package org.rulemaker.engine.matcher;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChainedMapTest {
	
	private Map<String, String> mapZero;
	private Map<String, String> mapOne;
	
	@Before
	public void initTestMaps() {
		mapZero = new HashMap<String, String>();
		mapZero.put("X", "valueX");
		mapZero.put("Y", "valueY");
		mapOne = new HashMap<String, String>();
		mapOne.put("X", "valueXNew");
		mapOne.put("Z", "valueZ");
	}
	
	@Test
	public void shouldGetSizeZeroForAnEmptyChainedMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>();
		assertEquals(0, chainedMap.size());
	}
	
	@Test
	public void shouldGetSizeTwoForAChainedMapBasedOnMapZero() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapZero);
		assertEquals(2, chainedMap.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowIllegalArgumentExceptionWithANullMapArgument() throws Exception {
		new ChainedMap<String, String>(null);
	}
	
	@Test
	public void shouldGetSizeThreeForAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertEquals(3, chainedMap.size());
	}
	
	@Test
	public void shouldIsEmptyReturnTrueForAnEmptyChainedMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>();
		assertTrue(chainedMap.isEmpty());
	}
	
	@Test
	public void shouldIsEmptyReturnFalseForAChainedMapBasedOnMapZero() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapZero);
		assertFalse(chainedMap.isEmpty());
	}
	
	@Test
	public void shouldIsEmptyReturnFalseForAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertFalse(chainedMap.isEmpty());
	}
	
	@Test
	public void shouldRecognizeYAsAContainedKeyForAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertTrue(chainedMap.containsKey("Y"));
	}
	
	@Test
	public void shouldRecognizeXAsAContainedKeyForAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertTrue(chainedMap.containsKey("Z"));
	}
	
	@Test
	public void shouldRecognizeWIsNotAContainedKeyForAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertFalse(chainedMap.containsKey("W"));
	}
	
	@Test
	public void shouldContainValueZInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertTrue(chainedMap.containsValue("valueZ"));
	}
	
	@Test
	public void shouldContainValueXNewInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertTrue(chainedMap.containsValue("valueXNew"));
	}
	
	@Test
	public void shouldNotContainValueWInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertFalse(chainedMap.containsValue("valueW"));
	}
	
	@Test
	public void shouldNotContainValueXInAChainedMapBasedOnMapZeroAndMapOneDueValueXHasBeenOverwrittenByMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertFalse(chainedMap.containsValue("valueW"));
	}
	
	@Test
	public void shouldContainValueCommonAssociatedWithTwoKeysInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		mapZero.put("U", "common");
		mapOne.put("V", "common");
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertTrue(chainedMap.containsValue("common"));
	}
	
	@Test
	public void shouldGetXReturnValueXNewInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertEquals("valueXNew", chainedMap.get("X"));
	}
	
	@Test
	public void shouldGetYReturnValueYInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertEquals("valueY", chainedMap.get("Y"));
	}
	
	@Test
	public void shouldGetZReturnValueZInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		assertEquals("valueZ", chainedMap.get("Z"));
	}
	
	@Test
	public void shouldPutWWithValueValueWInAChainedMapBasedOnMapZeroAndMapOneCorrectly() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		String previousValue = chainedMap.put("W", "valueW");
		assertEquals("valueW", chainedMap.get("W"));
		assertNull(previousValue);
	}
	
	@Test
	public void shouldOverrideZKeyWithValueValueZNewInAChainedMapBasedOnMapZeroAndMapOneCorrectly() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		String previousValue = chainedMap.put("Z", "valueZNew");
		assertEquals("valueZNew", chainedMap.get("Z"));
		assertEquals("valueZ", previousValue);
	}
	
	@Test
	public void shouldRemoveANonExistingKeyReturningNullInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		String previousValue = chainedMap.remove("W");
		assertNull(chainedMap.get("W"));
		assertNull(previousValue);
	}
	
	@Test
	public void shouldRemoveAnOverwrittenKeyEntirelyInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		String previousValue = chainedMap.remove("X");
		assertNull(chainedMap.get("X"));
		assertEquals("valueXNew", previousValue);
	}
	
	@Test
	public void shouldRemoveAnInnerKeyCorrectlyInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		String previousValue = chainedMap.remove("Z");
		assertNull(chainedMap.get("Z"));
		assertEquals("valueZ", previousValue);
	}
	
	@Test
	public void shouldPerformPutAllCorrectlyInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		Map<String, String> mapTwo = new HashMap<String, String>();
		mapTwo.put("W", "valueW");
		mapTwo.put("Z", "valueZNew");
		chainedMap.putAll(mapTwo);
		assertEquals("valueW", chainedMap.get("W"));
		assertEquals("valueZNew", chainedMap.get("Z"));
	}
	
	@Test
	public void shouldPerformClearCorrectlyInAChainedMapBasedOnMapZeroAndMapOne() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		chainedMap.clear();
		assertNull(chainedMap.get("X"));
		assertNull(chainedMap.get("Y"));
		assertNull(chainedMap.get("Z"));
	}
	
	@Test
	public void shouldObtainAnEmptyKeySetForEmptyChainedMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>();
		assertEquals(new HashSet<String>(), chainedMap.keySet());
	}
	
	@Test
	public void shouldObtainTheSameKeySetAsMapZeroForAChainedMapBasedOnMapZero() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapZero);
		assertEquals(mapZero.keySet(), chainedMap.keySet());
	}
	
	@Test
	public void shouldObtainTheGivenKeySetForChainedMapBasedOnMapZeroAndMapOne() {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		Set<String> expectedResult = new HashSet<String>(Arrays.asList(new String []{"X", "Y", "Z"}));
		assertEquals(expectedResult, chainedMap.keySet());
	}
	
	@Test
	public void shouldGetAnEmptyValueSetForAnEmptyChainedMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>();
		Set<String> actualValues = new HashSet<String>();
		actualValues.addAll(chainedMap.values());
		assertEquals(new HashSet<String>(), actualValues);
	}
	
	@Test
	public void shouldGetTheSameValueCollectionForMapZeroValueSetAndChainedMapBasedOnZeroMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapZero);
		Set<String> expectedValues = new HashSet<String>();
		expectedValues.addAll(mapZero.values());
		Set<String> actualValues = new HashSet<String>();
		actualValues.addAll(chainedMap.values());
		assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void shouldGetAValueCollectionContainingTheRightValuesForChainedMapBasedOnMapZeroAndMapOne() {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		Set<String> expectedValues = new HashSet<String>();
		expectedValues.addAll(Arrays.asList(new String[]{"valueXNew", "valueY", "valueZ"}));
		Set<String> actualValues = new HashSet<String>();
		actualValues.addAll(chainedMap.values());
		assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void shouldGetAnEmptyEntrySetForAnEmptyChainedMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>();
		Set<java.util.Map.Entry<String, String>> actualValues = new HashSet<java.util.Map.Entry<String, String>>();
		actualValues.addAll(chainedMap.entrySet());
		assertEquals(new HashSet<java.util.Map.Entry<String, String>>(), actualValues);
	}
	
	@Test
	public void shouldGetTheEntrySetForMapZeroValueSetAndChainedMapBasedOnZeroMap() throws Exception {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapZero);
		assertEquals(mapZero.entrySet(), chainedMap.entrySet());
	}
	
	@Test
	public void shouldGetAnEntrySetContainingTheRightValuesForChainedMapBasedOnMapZeroAndMapOne() {
		ChainedMap<String, String> chainedMap = new ChainedMap<String, String>(mapOne, 
				new ChainedMap<String, String>(mapZero));
		Set<java.util.Map.Entry<String, String>> expectedValues = new HashSet<java.util.Map.Entry<String, String>>();
		expectedValues.add(new AbstractMap.SimpleEntry<String, String>("X", "valueXNew"));
		expectedValues.add(new AbstractMap.SimpleEntry<String, String>("Y", "valueY"));
		expectedValues.add(new AbstractMap.SimpleEntry<String, String>("Z", "valueZ"));
		Set<java.util.Map.Entry<String, String>> actualValues = new HashSet<java.util.Map.Entry<String, String>>();
		actualValues.addAll(chainedMap.entrySet());
		assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void shouldToStringReturnAProperValue() {
		String expectedToStringResult = "one -> 1.0\n" +
				"two -> 2.0\n"
			  + "three -> null";
		Map<String, Double> innerMap = new HashMap<String, Double>(); 
		innerMap.put("two", 2.0);
		Map<String, Double> map = new ChainedMap<String, Double>(innerMap);
		map.put("one", 1.0);
		map.put("three", null);
		Map<String, String> expectedMap = buildMapFromString(expectedToStringResult);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String entryKey = entry.getKey().toString();
			String entryValue = ((entry.getValue() != null) ? entry.getValue().toString() : "null");
			assertNotNull(expectedMap.get(entryKey));
			assertEquals(expectedMap.get(entryKey), entryValue);
		}
	}
	
	private Map<String, String> buildMapFromString(String stringMap) {
		String []lines = stringMap.split("\\n");
		Map<String, String> map = new HashMap<String, String>();
		for (String aLine : lines) {
			String []keyValue = aLine.split("\\-\\>");
			map.put(keyValue[0].trim(), keyValue[1].trim());
		}
		return map;
	}

}
