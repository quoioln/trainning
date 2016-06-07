package info;

public class Display {
	// size of dispaly
	private float size;
	// number of color display
	private int numColor;
	/**
	 * Contructor
	 */
	public Display() {
		size = 0;
		numColor = 0;
	}
	/**
	 * contructor include 2 param:
	 * @param size
	 * @param numColor
	 */
	public Display(final float size, final int numColor) {
	
		this.size = size;
		this.numColor = numColor;
	}
	/**
	 * @return the size
	 */
	public final float getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public final void setSize(float size) {
		this.size = size;
	}
	/**
	 * @return the numColor
	 */
	public final int getNumColor() {
		return numColor;
	}
	/**
	 * @param numColor the numColor to set
	 */
	public final void setNumColor(int numColor) {
		this.numColor = numColor;
	}
	
	public final String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Infomation of display:\n");
		result.append("\tSize: " + size + "\n");
		result.append("\tNumber of color: " + numColor);
		return result.toString();
	}
	
	
}