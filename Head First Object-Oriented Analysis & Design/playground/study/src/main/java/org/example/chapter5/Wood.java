package org.example.chapter5.part1;

public enum Wood {
	INDIAN_ROSEWOOD, BRAZILIAN_ROSEWOOD, MAHOGANY, MAPLE, COCOBOLO, CEDAR, ADIRONDACK, ALDER, SITKA;

	public String toString() {
		return switch (this) {
			case INDIAN_ROSEWOOD -> "Indian Rosewood";
			case BRAZILIAN_ROSEWOOD -> "Brazilian Rosewood";
			case MAHOGANY -> "Mahogany";
			case MAPLE -> "maple";
			case COCOBOLO -> "Cocobolo";
			case CEDAR -> "Cedar";
			case ADIRONDACK -> "Adirondack";
			case ALDER -> "Alder";
			case SITKA -> "Sitka";
		};
	}
}
