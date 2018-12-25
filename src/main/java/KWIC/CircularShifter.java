package KWIC;
import java.util.ArrayList;
import java.util.Arrays;

public class CircularShifter {
	private ArrayList<Line> csLines;
	
	public CircularShifter() {
		csLines = new ArrayList<Line>();
	}
	
	public ArrayList<Line> getShiftedLines() {
		return csLines;
	}
	
	public void setup(LineStorage lineStorage) {
		Line line = lineStorage.getLineWithoutNoise();
		
		csLines.add(line);
		
		ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(line.getDescriptor().split(" ")));
		
		for(int i = 0; i < splitLine.size() - 1; i++) {
			String firstWord = splitLine.get(0);
			splitLine.remove(0);
			splitLine.add(firstWord);
			
			String shiftedLine = "";
			for(int j = 0; j < splitLine.size(); j++) {
				shiftedLine += splitLine.get(j) + " ";
			}
			shiftedLine = shiftedLine.trim();
			
			csLines.add(new Line(shiftedLine, line.getUrl()));
		}
	}
}