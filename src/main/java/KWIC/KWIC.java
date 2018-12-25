package KWIC;

public class KWIC {
	private LineStorage lineStorage;
	private NoiseEliminator noiseEliminator;
	private CircularShifter shifter;
	private Alphabetizer alphabetizer;
	
	public KWIC(Line inputLine) {
		lineStorage = new LineStorage(inputLine);
		noiseEliminator = new NoiseEliminator();
		shifter = new CircularShifter();
		alphabetizer = new Alphabetizer();
	}
	
	public void setup( ) {
		noiseEliminator.setup(lineStorage);
		shifter.setup(lineStorage); 
		alphabetizer.setup(shifter);
	}
	
	public LineStorage getLineStorage() {
		return lineStorage;
	}

	public CircularShifter getShifter() {
		return shifter;
	}

	public Alphabetizer getAlphabetizer() {
		return alphabetizer;
	}
}