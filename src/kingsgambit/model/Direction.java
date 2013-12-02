package kingsgambit.model;

import java.awt.Point;

public enum Direction {
	NORTH(0.0) {
		public Square transform(Square s) {
			return s;
		}
		public Square reverseTransform(Square s) {
			return s;
		}
		public Square projectFrom(Point p, int distance) {
			return new Square(p.y-distance, p.x);
		}
	},
	EAST(90.0) {
		public Square transform(Square s) {
			return new Square(s.column, -s.row);
		}
		public Square reverseTransform(Square s) {
			return new Square(-s.column, s.row);
		}
		public Square projectFrom(Point p, int distance) {
			return new Square(p.y, p.x+distance);
		}
	},
	SOUTH(180.0) {
		public Square transform(Square s) {
			return new Square(-s.row, s.column);
		}
		public Square reverseTransform(Square s) {
			return new Square(-s.row, s.column);
		}
		public Square projectFrom(Point p, int distance) {
			return new Square(p.y+distance, p.x);
		}
	},
	WEST(270.0) {
		public Square transform(Square s) {
			return new Square(s.column, s.row);
		}
		public Square reverseTransform(Square s) {
			return new Square(s.column, -s.row);
		}
		public Square projectFrom(Point p, int distance) {
			return new Square(p.y, p.x-distance);
		}
	};
	
	static {
		NORTH.rightFace = EAST;
		NORTH.leftFace = WEST;
		NORTH.aboutFace = SOUTH;
		
		EAST.rightFace = SOUTH;
		EAST.leftFace = NORTH;
		EAST.aboutFace = WEST;
		
		SOUTH.rightFace = WEST;
		SOUTH.leftFace = EAST;
		SOUTH.aboutFace = NORTH;
		
		WEST.rightFace = NORTH;
		WEST.leftFace = SOUTH;
		WEST.aboutFace = EAST;
	}
	
	public static Direction between(Point a, Point b) {
		if (b.x > a.x)
			return EAST;
		if (b.x < a.x)
			return WEST;
		if (b.y > a.y)
			return SOUTH;
		return NORTH;
	}
	
	public static Direction closest(double radians) {
		int degrees = (int)Math.toDegrees(radians) - 90;
		if (degrees < 0)
			degrees += 360;
		if (degrees > 315 || degrees <= 45)
			return NORTH;
		if (degrees <= 135)
			return EAST;
		if (degrees <= 225)
			return SOUTH;
		return WEST;
	}
	
	public Direction rightFace() {
		return rightFace;
	}
	
	public Direction leftFace() {
		return leftFace;
	}
	
	public Direction aboutFace() {
		return aboutFace;
	}	
	
	public double rotationInRadians() {
		return rotationInRadians;
	}
	
	/**
	 * Re-orients NORTH-facing coordinates to this direction.
	 * @param s the square in NORTH-oriented coordinates
	 * @return the position of the square when its displacement vector is pointed at this Direction.
	 */
	public abstract Square transform(Square s);
	
	/**
	 * Re-orients coordinates as if this direction were NORTH.
	 * @param s the square in NORTH-oriented coordinates
	 * @return the position of the square when its displacement vector is pointed at this Direction.
	 */
	public abstract Square reverseTransform(Square s);

	public abstract Square projectFrom(Point p, int distance);

	/**
	 * Between 0.0 and 180.0 inclusive
	 * @return
	 */
	public double positiveDifference(Direction other) {
		double diff = Math.toDegrees(Math.abs(rotationInRadians - other.rotationInRadians));
		if (diff > 180.0)
			diff = -(diff-360.0);
		return diff;
	}
	
	Direction(double rotationInDegrees) {
		rotationInRadians = Math.toRadians(rotationInDegrees);
	}
	
	private Direction rightFace;
	private Direction leftFace;
	private Direction aboutFace;
	private double rotationInRadians;
}
