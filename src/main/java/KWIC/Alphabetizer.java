package KWIC;
import java.util.ArrayList;

public class Alphabetizer {
	private ArrayList<Line> sortedLines;
	
	public Alphabetizer() {
		sortedLines = new ArrayList<Line>();
	}
	
	public ArrayList<Line> getSortedLines() {
		return sortedLines;
	}
	
	public void setup(CircularShifter shifter) {
		ArrayList<Line> csLines = shifter.getShiftedLines();
		
		for(int i = 0; i < csLines.size(); i++) {
			if(sortedLines.size() == 0) {
				sortedLines.add(csLines.get(i));
			} else {
				for(int j = 0; j <= sortedLines.size(); j++) {
					if(j == sortedLines.size()) {
						sortedLines.add(csLines.get(i));
						break;
					} else {
						if(csLines.get(i).getDescriptor().compareToIgnoreCase(sortedLines.get(j).getDescriptor()) < 0) {
							sortedLines.add(j, csLines.get(i));
							break;
						}
					}
				}
			}
		}
	}
}