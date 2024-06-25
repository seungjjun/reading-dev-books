package org.example.chapter4;

public class Bark {
	private String sound;

	public Bark(String sound) {
		this.sound = sound;
	}

	public String getSound() {
		return sound;
	}

	public boolean equals(Object o) {
		if (o instanceof Bark) {
			Bark otherBark = (Bark) o;
			if (this.sound.equalsIgnoreCase(otherBark.sound)) {
				return true;
			}
		}
		return false;
	}

}

