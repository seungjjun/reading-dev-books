package org.example.chapter1;

public class GuitarSpec {
	private Builder builder;
	private String model;
	private Type type;
	private int numStrings;
	private Wood backWood ,topWood;

	public GuitarSpec(Builder builder, String model, Type type, int numStrings, Wood backWood, Wood topWood) {
		this.builder = builder;
		this.model = model;
		this.numStrings = numStrings;
		this.type = type;
		this.backWood = backWood;
		this.topWood = topWood;
	}

	public Builder getBuilder() {
		return builder;
	}

	public String getModel() {
		return model;
	}

	public int getNumStrings() {
		return numStrings;
	}

	public Type getType() {
		return type;
	}

	public Wood getBackWood() {
		return backWood;
	}

	public Wood getTopWood() {
		return topWood;
	}

	public boolean matches(GuitarSpec otherSpec) {
		if (this.builder != otherSpec.getBuilder()) {
			return false;
		}

		String model = otherSpec.getModel().toLowerCase();
		if ((model != null) && (!model.equals("")) && !model.equals(this.model.toLowerCase())) {
			return false;
		}
		if (this.getType() != otherSpec.getType()) {
			return false;
		}
		if (this.backWood != otherSpec.getBackWood()) {
			return false;
		}
		if (this.topWood != otherSpec.getTopWood()) {
			return false;
		}

		return true;
	}
}
