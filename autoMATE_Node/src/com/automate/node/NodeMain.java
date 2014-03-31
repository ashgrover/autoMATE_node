package com.automate.node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class NodeMain {

	private static AutoMateNode node;
	
	public static void main(String [] args) {
		if(args.length < 1) {
			showUsage();
		}
		Properties configurationProperties = new Properties();
		try {
			configurationProperties.load(new FileReader(args[0]));
			node = new AutoMateNode(configurationProperties);
			node.start();
		} catch (FileNotFoundException e) {
			System.err.print("Failed to initialize node - properties file not found");
		} catch (IOException e) {
			System.err.print("Failed to initialize node - properties file could not be read");
		} catch (InitializationException e) {
			System.err.print("Failed to initialize node.");
			e.printStackTrace();
		}
		BufferedReader commandLineReader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			String line;
			try {
				line = commandLineReader.readLine();
				if(line.equals("exit")) {
					node.stop();
					return;
				}
			} catch (IOException e) {}
		}
	}
	
	static void showUsage() {
		System.out.println("Usage:java -jar AutoMateNode.jar <properties file>");
		System.exit(1);
	}
	
}
