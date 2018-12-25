package KWIC;

import java.util.ArrayList;
import java.util.Arrays;

public class NoiseEliminator {
	private String[] noiseWords;
	
	public NoiseEliminator() {
		noiseWords = new String[] {"-", "&", "a", "an", "and", "as", "for", "in", "into", "is", "of", "on", "or", "the", "to", "up"};
	}
	
	public void setup(LineStorage lineStorage) {
		Line line = lineStorage.getInputLine();
		
		ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(line.getDescriptor().split(" ")));
		
		for(int i = 0; i < splitLine.size(); i++) {
			for(int j = 0; j < noiseWords.length; j++) {
				if(splitLine.get(i).equalsIgnoreCase(noiseWords[j])) {
					splitLine.remove(i);
					i--;
					break;
				}
			}
		}
		
		String lineStringWithoutNoise = "";
		for(int i = 0; i < splitLine.size(); i++) {
			lineStringWithoutNoise += " " + splitLine.get(i);
		}
		lineStringWithoutNoise = lineStringWithoutNoise.trim();
		
		Line lineWithoutNoise = new Line(lineStringWithoutNoise, lineStorage.getInputLine().getUrl());
		
		lineStorage.setLineWithoutNoise(lineWithoutNoise);
	}
}