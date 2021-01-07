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
import java.util.List;

import javax.swing.JButton;

import de.hpi.mod.sim.core.Configuration;



public class TestResultDatabase {
	
	private String filename; 
	private List<TestResultsForWorld> resultsForWorlds;
		
	TestResultDatabase(String filename){
		this.filename = filename;
		this.resultsForWorlds = new ArrayList<>();
		try {
			this.createFileIfNotExist();
			this.loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetTestResultsForWorld(String worldName) {
		
	}

	public void setTestResult(String worldName, String testName, Result result) {
		
	}

	public void setTestPassed(String worldName, String testName) {
		
	}

	public void setTestFailed(String worldName, String testName) {
		
	}

	public Result getTestResult(String worldName, String testName) {
		TestResultsForWorld worldResults = this.getTestResultsForWorld(worldName);
		return worldResults.getResultFor(testName);
	}

	public TestResultsForWorld getTestResultsForWorld(String worldName) {
		// Try to get existing result set for world
		for(TestResultsForWorld worldResults: this.resultsForWorlds) 
			if(worldResults.name.equals(worldName)) 
				return worldResults;
		
		// If none existsed, create new result set
		TestResultsForWorld newWorldResults = new TestResultsForWorld(worldName);
		this.resultsForWorlds.add(newWorldResults);
		return newWorldResults;
	}

	public int getSuccessfulTestNumberForWorld(String worldName) {
		return 0;
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
			
			// Ignore lines that contain less than 1 or more than 2 times "#"
			int hashCounter = line.length() - line.replaceAll("#","").length();
			if(hashCounter < 1 || hashCounter > 2) continue;
			
			System.out.println(line);
		}

		
	}
	
	private void writeFile() {
		
	}
	
	public enum Result {
		TEST_UNKNOWN, NOT_TESTED, LAST_TEST_PASSED, LAST_TEST_FAILED
	}
	
	private class TestReultEntry{
		String name;
		Result value;
	}
	
	
	private class TestResultsForWorld{
		String name; 
		List<TestReultEntry> results;
		
		public TestResultsForWorld(String name) {
			this.name = name;
			this.results = new ArrayList<>();
		}
		
		public Result getResultFor(String testName) {
			for(TestReultEntry entry: this.results) 
				if(entry.name.equals(testName)) 
					return entry.value;
			throw new IllegalArgumentException(); 
		}
		
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
