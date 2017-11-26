package PDT.Backend;

import java.awt.Color;

public class Helper {

	static String getcolor(double length) {
		double blend = length / 10000;
		System.out.println(blend);

		int b = 0;
		int r = (int) (blend * 255);
		int g = (int) ((1 - blend) * 255);
		Color c = new Color(r, g, b);
		System.out.println(r + " " + g + " " + b + " ");
		System.out.println("#" + Integer.toHexString(c.getRGB()).substring(2));
		return "#" + Integer.toHexString(c.getRGB()).substring(2);

	}

}
