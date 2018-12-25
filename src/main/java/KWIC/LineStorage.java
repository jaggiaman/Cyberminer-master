package KWIC;

public class LineStorage {
	private Line inputLine;
	private Line lineWithoutNoise;
	
	public LineStorage(Line inputLine) {
		this.inputLine = inputLine;
		this.lineWithoutNoise = null;
	}
	
	public Line getInputLine() {
		return inputLine;
	}
	
	public Line getLineWithoutNoise() {
		return lineWithoutNoise;
	}
	
	public void setLineWithoutNoise(Line lineWithoutNoise) {
		this.lineWithoutNoise = lineWithoutNoise;
	}
}