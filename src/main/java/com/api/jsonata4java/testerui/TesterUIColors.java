package com.api.jsonata4java.testerui;

import java.awt.Color;

public enum TesterUIColors {
	// special semantic: custom settings
	CUSTOM("custom", 0x000000, 0x000000, 0x000000, 0x000000),
	EXERCISER("exerciser", 0xFFFFFB, 0xFFFFFB, 0xEEEEEE, 0xFFEEEE),
	PASTEL("pastel", 0xEEFFFF, 0xFFFFEE, 0xEEFFEE, 0xFFEEEE);

	private final String uiName;
	private final Color colorInput;
	private final Color colorJsonata;
	private final Color colorOutput;
	private final Color colorError;

	private TesterUIColors(final String uiName, int colorInput, int colorJsonata, int colorOutput, int colorError) {
		this.uiName = uiName;
		this.colorInput = new Color(colorInput);
		this.colorJsonata = new Color(colorJsonata);
		this.colorOutput = new Color(colorOutput);
		this.colorError = new Color(colorError);
	}

	public static TesterUIColors fromColors(Color colorInput, Color colorJsonata, Color colorOutput, Color colorError) {
		for (final TesterUIColors color : TesterUIColors.values()) {
			if (color == TesterUIColors.CUSTOM) {
				continue;
			}
			if (colorInput.equals(color.getColorInput())
					&& colorJsonata.equals(color.getColorJsonata())
					&& colorOutput.equals(color.getColorOutput())
					&& colorError.equals(color.getColorError())) {
				return color;
			}
		}
		return TesterUIColors.CUSTOM;
	}

	public String toString() {
		return uiName;
	}

	public Color getColorInput() {
		return colorInput;
	}

	public Color getColorJsonata() {
		return colorJsonata;
	}

	public Color getColorOutput() {
		return colorOutput;
	}

	public Color getColorError() {
		return colorError;
	}
}
