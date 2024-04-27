package org.example.appendix2;

public class FlyTest {
	public static void main(String[] args) {
		Airplane biplane = new Airplane();

		biplane.setSpeed(212);
		System.out.println(biplane.getSpeed()); // 212

		Jet boeing = new Jet();
		boeing.setSpeed(422);
		System.out.println(boeing.getSpeed()); // 844
		int x = 0;
		while (x < 4) {
			boeing.accelerate();
			System.out.println(boeing.getSpeed()); // 1688, 6752
			if (boeing.getSpeed() > 5000) {
				biplane.setSpeed(biplane.getSpeed() * 2);
			} else {
				boeing.accelerate();
			}
			x++;
		}
		System.out.println(biplane.getSpeed());
	}
}
