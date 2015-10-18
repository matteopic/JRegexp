package it.matteopic.jrb;

import java.awt.Component;
import java.awt.Toolkit;

public class ResolutionIndipendence {
	
//	public static void setContainerSize(Container cmp, double inchWidth, double inchHeight){
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		int dpi = tk.getScreenResolution();
//
//		int newWidth = (int) (inchWidth * dpi);
//		int newHeight = (int) (inchHeight * dpi);
//		cmp.setSize(newWidth, newHeight);
//	}

	public static void setInchComponentSize(Component cmp, double inchWidth, double inchHeight){
		Toolkit tk = Toolkit.getDefaultToolkit();
		int dpi = tk.getScreenResolution();

		int newWidth = (int) (inchWidth * dpi);
		int newHeight = (int) (inchHeight * dpi);
		cmp.setSize(newWidth, newHeight);
	}
	
	public static void setCentimeterComponentSize(Component cmp, double cmWidth, double cmHeight){
		Toolkit tk = Toolkit.getDefaultToolkit();
		int dpi = tk.getScreenResolution();

		int newWidth = (int) ((cmWidth / 2.54) * dpi);
		int newHeight = (int) ((cmHeight / 2.54) * dpi);
		cmp.setSize(newWidth, newHeight);
	}

}
