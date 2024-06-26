package org.example.chapter5.part1;

public enum Type {
	ACOUSTIC, ELECTRIC;

	public String toString() {
		return switch (this) {
			case ACOUSTIC -> "acoustic";
			case ELECTRIC -> "electric";
		};
	}
}
