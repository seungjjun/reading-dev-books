package org.example.chapter5.part1;

public enum Builder {
	FENDER, MARTIN, GIBSON, COLLINGS, OLSON, RYAN, PRS, ANY;

	public String toString() {
		return switch (this) {
			case FENDER -> "Fender";
			case MARTIN -> "Martin";
			case GIBSON -> "Gibson";
			case COLLINGS -> "Collings";
			case OLSON -> "Olson";
			case RYAN -> "Ryan";
			case PRS -> "Prs";
			case ANY -> "Any";
		};
	}
}
