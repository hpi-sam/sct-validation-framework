package de.hpi.mod.sim.core.scenario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.mod.sim.core.Configuration;

public class TestResultDatabase implements ITestScenarioListener {

	private final String SEPERATOR = "#";

	private String filename;
	private String selectedWorldName;
	private Map<String, Map<String, Result>> results;

	TestResultDatabase(String worldName) {
		this(Configuration.getTestFileName(), worldName);
	}

	TestResultDatabase(String filename, String worldName) {
		this.filename = filename;
		this.selectedWorldName = worldName;
		this.results = new HashMap<>();
		this.createFileIfNotExist();
	}
	
	public void loadTestResultsFromFile() {
		this.loadFile(false);
	}
	
	public void loadTestResultsFromFile(boolean ignoreAdditionalTestsForSlectedWorld) {
		this.loadFile(ignoreAdditionalTestsForSlectedWorld);
	}

	public void resetTestResults() {
		this.resetTestResults(this.selectedWorldName);
	}

	public void resetTestResults(String worldName) {
		this.getResultMapForWorld(worldName).replaceAll((k, v) -> Result.NOT_TESTED);
		this.writeFile();
	}

	public void earmarkTest(String testName) {
		this.earmarkTest(this.selectedWorldName, testName, Result.NOT_TESTED);
	}

	public void earmarkTest(String worldName, String testName) {
		this.earmarkTest(this.selectedWorldName, testName, Result.NOT_TESTED);
	}

	public void earmarkTest(String testName, Result testResult) {
		this.earmarkTest(this.selectedWorldName, testName, testResult);
	}

	public void earmarkTest(String worldName, String testName, Result testResult) {
		this.getResultMapForWorld(worldName).put(testName, testResult);
	}

	public void insertNewTest(String testName) {
		this.insertNewTest(this.selectedWorldName, testName);
	}

	public void insertNewTest(String worldName, String testName) {
		if (this.getResultMapForWorld(worldName).putIfAbsent(testName, Result.NOT_TESTED) == null)
			// Write to file if test was actually added
			this.writeFile();
	}

	public void resetTestResult(String testName) {
		this.resetTestResult(this.selectedWorldName, testName);
	}

	public void resetTestResult(String worldName, String testName) {
		if (this.getResultMapForWorld(worldName).put(testName, Result.NOT_TESTED) != Result.NOT_TESTED)
			// Write to file if value was changed
			this.writeFile();
	}

	public void setTestPassed(String testName) {
		this.setTestPassed(this.selectedWorldName, testName);
	}

	public void setTestPassed(String worldName, String testName) {
		if (this.getResultMapForWorld(worldName).put(testName, Result.TEST_PASSED) != Result.TEST_PASSED)
			// Write to file if value was changed
			this.writeFile();
	}

	public void setTestFailed(String testName) {
		this.setTestFailed(this.selectedWorldName, testName);
	}

	public void setTestFailed(String worldName, String testName) {
		if (this.getResultMapForWorld(worldName).put(testName, Result.TEST_FAILED) != Result.TEST_FAILED)
			// Write to file if value was changed
			this.writeFile();
	}

	private boolean hasTest(String worldName, String testName) {
		return this.getResultMapForWorld(worldName).containsKey(testName);
	}

	public Result getTestResult(String testName) {
		return this.getTestResult(this.selectedWorldName, testName);
	}

	public Result getTestResult(String worldName, String testName) {
		return this.getResultMapForWorld(worldName).getOrDefault(testName, Result.TEST_UNKNOWN);
	}

	public int getNumberOfTests() {
		return this.getNumberOfTestsForWorld(this.selectedWorldName);
	}

	public int getNumberOfTestsForWorld(String worldName) {
		return this.getResultMapForWorld(worldName).size();
	}

	public int getNumberOfPassedTests() {
		return this.getNumberOfPassedTestsForWorld(this.selectedWorldName);
	}

	public int getNumberOfPassedTestsForWorld(String worldName) {
		return Collections.frequency(this.getResultMapForWorld(worldName).values(), Result.TEST_PASSED);
	}

	private Map<String, Result> getResultMapForSelectedWorld() {
		return this.getResultMapForWorld(this.selectedWorldName);
	}

	private Map<String, Result> getResultMapForWorld(String worldName) {
		// Try to get existing result set for world
		if (this.results.containsKey(worldName))
			return this.results.get(worldName);

		// If none existsed, create new result set
		Map<String, Result> newWorldResults = new HashMap<>();
		this.results.put(worldName, newWorldResults);
		return newWorldResults;
	}

	private void createFileIfNotExist() {
		File file = new File(this.filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFile() {
		this.loadFile(false);
	}

	private void loadFile(boolean ignoreAdditionalTestForSlectedWorld) {
		
		// Open the fule path
		Path path = Paths.get(this.filename);

		try {
			// Iterate all lines
			for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
				// Split Line by separator
				String[] lineParts = line.split("#");

				// Ignore lines that contain less or more than 2 timesthe separator "#"
				if (lineParts.length != 3)
					continue;
				
				String worldName = lineParts[0];
				String testName = lineParts[1];
				String testResult = lineParts[2];
				
				// Ignore lines that contain an illegal test result value
				if ( !testResult.equals(Result.TEST_PASSED.toString())
						&& !testResult.equals(Result.TEST_FAILED.toString())
						&& !testResult.equals(Result.NOT_TESTED.toString()))
					continue;

				// Add all tests that...
				// a) are in a different world than is currently selected (we have no real test data data on other worlds, so we must load the results and assume they are true in order to store them to the file again while writing)
				// b) if we are NOT in ignoreAdditionalTestForSlectedWorld mode (in that case, all tests for the current world may be importet, even in not imported yet)
				// or
				// c) if the test case is already known to use (relevant to load PASS or FAIL results for known tests in ignoreAdditionalTestForSlectedWorld) 
				if (worldName != this.selectedWorldName || 
						!ignoreAdditionalTestForSlectedWorld || 
						this.hasTest(worldName, testName)) {
					if (testResult.equals(Result.TEST_PASSED.toString())) {
						this.earmarkTest(worldName, testName, Result.TEST_PASSED);
					}else if (testResult.equals(Result.TEST_FAILED.toString())) {
						this.earmarkTest(worldName, testName, Result.TEST_FAILED);
					}else{
						this.earmarkTest(worldName, testName);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeFile() {
		try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(this.filename, false))) {
			// Iterate all worlds
			for (Entry<String, Map<String, Result>> worldEntry : this.results.entrySet()) {
				String worldName = worldEntry.getKey();
				// Iterate over all test in the world
				for (Entry<String, Result> testEntry : worldEntry.getValue().entrySet()) {
					String testName = testEntry.getKey();
					String testResult = testEntry.getValue().toString();
					// Write it to the file
					outputFile.append(worldName + this.SEPERATOR + testName + this.SEPERATOR + testResult);
					outputFile.newLine();
				}
			}
			outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public enum Result {
		TEST_UNKNOWN, NOT_TESTED, TEST_PASSED, TEST_FAILED
	}

	@Override
	public void markTestPassed(TestScenario test) {
		this.setTestPassed(test.getName());
	}

	@Override
	public void markTestFailed(TestScenario test) {
		this.setTestFailed(test.getName());
	}

	@Override
	public void resetAllTests() {
		this.resetTestResults();
	}

}
