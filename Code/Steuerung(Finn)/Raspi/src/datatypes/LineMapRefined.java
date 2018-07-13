package datatypes;

import java.util.Arrays;

import lejos.geom.*;

import lejos.robotics.mapping.LineMap;

public class LineMapRefined extends LineMap {

	public LineMapRefined(Line[] linesNew, Rectangle boundingRect) {
		super(linesNew, boundingRect);
	}

	public LineMapRefined() {
		super();
	}

	public LineMapRefined addLines(Line[] l) {
		Line[] lines = getLines();
		Line[] linesNew = concat(lines, l);
		return new LineMapRefined(linesNew, getBoundingRect());
	}

	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

}