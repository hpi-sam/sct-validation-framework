package de.hpi.mod.sim.core.scenario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import de.hpi.mod.sim.core.Configuration;



public class TestResultDatabase {
	
	private String filename; 
	private String selectedWorldName;
	private Map<String, Map<String,Result>> results;
	
	TestResultDatabase(String worldName){
		this(Configuration.getTestFileName(), worldName);
	}
		
	TestResultDatabase(String filename, String worldName){
		this.filename = filename;
		this.selectedWorldName = worldName;
		this.results = new HashMap<>();
		try {
			this.createFileIfNotExist();
			this.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetTestResults() {
		this.resetTestResults(this.selectedWorldName);
	}
	
	public void resetTestResults(String worldName) {
		this.getResultMapForWorld(worldName).replaceAll((k ,v) -> Result.NOT_TESTED);
	}

	public void addTest(String testName) {
		this.addTest(this.selectedWorldName, testName);
	}

	public void addTest(String worldName, String testName) {
		this.getResultMapForWorld(worldName).putIfAbsent(testName, Result.NOT_TESTED);
	}

	public void resetTestResult(String testName) {
		this.resetTestResult(this.selectedWorldName, testName);
	}

	public void resetTestResult(String worldName, String testName) {
		this.getResultMapForWorld(worldName).put(testName, Result.NOT_TESTED);
	}

	public void setTestPassed(String testName) {
		this.setTestPassed(this.selectedWorldName, testName);
	}


	public void setTestPassed(String worldName, String testName) {
		this.getResultMapForWorld(worldName).put(testName, Result.LAST_TEST_PASSED);
	}

	public void setTestFailed(String testName) {
		this.setTestFailed(this.selectedWorldName, testName);
	}


	public void setTestFailed(String worldName, String testName) {
		this.getResultMapForWorld(worldName).put(testName, Result.LAST_TEST_FAILED);
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

	public int getSuccessfulTestNumber() {
		return this.getSuccessfulTestNumberForWorld(this.selectedWorldName);
	}

	public int getSuccessfulTestNumberForWorld(String worldName) {
		return Collections.frequency(this.getResultMapForWorld(worldName).values(), Result.LAST_TEST_PASSED);
	}

	private Map<String, Result> getResultMapForSelectedWorld() {
		return this.getResultMapForWorld(this.selectedWorldName);
	}
	
	private Map<String, Result> getResultMapForWorld(String worldName) {
		// Try to get existing result set for world
		if(this.results.containsKey(worldName))
			return this.results.get(worldName);
		
		// If none existsed, create new result set
		Map<String,Result> newWorldResults = new HashMap<>();
		this.results.put(worldName, newWorldResults);
		return newWorldResults;
	}
	
	private void createFileIfNotExist() throws IOException {
		File file = new File(this.filename);
		file.createNewFile();
	}

	private void loadFile() throws IOException {
		// Open the fule path
		Path path = Paths.get(this.filename);
		
		// Iterate all lines
		for(String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			// Split Line by separator
			String[] lineParts = line.split("#");
			
			// Ignore lines that contain less than 1 or more than 2 times "#"
			if(lineParts.length < 2 || lineParts.length > 3) continue;
			
			String worldName = lineParts[0];
			String testName = lineParts[1];
			if(lineParts.length == 3 && lineParts[2] == "y") {
				this.setTestPassed(worldName, testName);
			} else if(lineParts.length == 3 && lineParts[2] == "n") {
				this.setTestFailed(worldName, testName);	
			} else {
				this.addTest(worldName, testName);	
			}
		}

		
	}
	
	private void writeFile() {
		
	}
	
	public enum Result {
		TEST_UNKNOWN, NOT_TESTED, LAST_TEST_PASSED, LAST_TEST_FAILED
	}
	
	
	
//	private void resetTestFile(String testFileName) {
//		try {
//			changeContent(testFileName, "#y", "#n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private JButton newResetButton() {
//		JButton button = new JButton("Reset Results");
//		
//		button.addActionListener(e -> {
//			resetTestFile(Configuration.getTestFileName());
//			resetTests();
//			updateProgressDisplay();
//			frame.getTestListPanel().resetColors();
//		});
//		
//		return button;
//	}
//	
//	private int getCompletedTestCount() {
//		Path path = Paths.get(Configuration.getTestFileName());
//		Charset charset = StandardCharsets.UTF_8;
//		
//		int count = 0;
//		try {
//			String content;
//			content = new String(Files.readAllBytes(path), charset);
//			count = content.split("#y", -1).length - 1;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return count;
//	}
//	
//	private int getCompletedTestCount() {
//		Path path = Paths.get(Configuration.getTestFileName());
//		Charset charset = StandardCharsets.UTF_8;
//		
//		int count = 0;
//		try {
//			String content;
//			content = new String(Files.readAllBytes(path), charset);
//			count = content.split("#y", -1).length - 1;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return count;
//	}
//
//	private void writeTestPassed(TestScenario test) throws IOException {
//		changeContent(Configuration.getTestFileName(), test.getName() + "#n", test.getName() + "#y");
//	}
//	
//	private void changeContent(String fileName, String oldContent, String newContent) throws IOException {
//		Path path = Paths.get(fileName);
//		Charset charset = StandardCharsets.UTF_8;
//
//		String content = new String(Files.readAllBytes(path), charset);
//		content = content.replaceAll(oldContent, newContent);
//		Files.write(path, content.getBytes(charset));
//	}
//
//	
//
//	private boolean testPassed(TestScenario test, String fileName) {
//		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//		    String line;
//		    while ((line = br.readLine()) != null) {
//		       String[] parts = line.split("#");
//		       if(parts[0].equals(test.getName())) {
//		    	   if(parts[1].equals("n")) {
//		    		   return false;
//		    	   } else if (parts[1].equals("y")) {
//		    		   return true;
//		    	   }
//		       }
//		    }
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	private boolean writeLineIfNeeded(TestScenario test, String fileName) {
//		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//		    String line;
//		    while ((line = br.readLine()) != null) {
//		       String[] parts = line.split("#");
//		       if(parts[0].equals(test.getName())) {
//		    	   return false;
//		       }
//		    }
//		    br.close();
//		    BufferedWriter output = new BufferedWriter(new FileWriter(fileName, true));
//		    output.append(test.getName() + "#n" + System.lineSeparator());
//		    output.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return true;
//	}
//
//	private void createFileIfNotExist(String fileName) {
//    	File file = new File(fileName);
//    	try {
//			file.createNewFile();
//		} catch (IOException e) {
//			System.out.println("Could not write persistence file to file system.");
//			e.printStackTrace();
//		}
//	}
	
}
