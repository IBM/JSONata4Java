/**
 * (c) Copyright 2018 - 2022 IBM Corporation
 * 1 New Orchard Road, 
 * Armonk, New York, 10504-1722
 * United States
 * +1 914 499 1900
 * support: Nathaniel Mills wnm3@us.ibm.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.api.jsonata4java.testerui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Aggregates all Tester UI app settings that can be interactively changed.
 *
 * @author Martin Bluemel
 */
public class TesterUISettings {

	public static final File SETTINGS_FOLDER = new File(System.getProperty("user.home") + File.separator + ".jsonata4java");
	public static final File SETTINGS_FILE = new File(SETTINGS_FOLDER, "preferences.properties");
	public static final Path DEFAULT_PATH_INPUT = Paths.get("src/test/resources/exerciser/address.json");
	public static final Path DEFAULT_PATH_JSONATA = Paths.get("src/test/resources/exerciser/addressExpression.jsonata");

	private TesterUIJsonataExample example = TesterUIJsonataExample.ADDRESS;
	private Path pathInput = example.getPathInput();
	private Path pathJsonata = example.getPathJsonata();
	private Color backgroundInput = TesterUIColors.EXERCISER.getColorInput();
	private Color backgroundJsonata = TesterUIColors.EXERCISER.getColorJsonata();
	private Color backgroundOutput = TesterUIColors.EXERCISER.getColorOutput();
	private Color backgroundError = TesterUIColors.EXERCISER.getColorError();
	private Font font = new Font("Consolas", Font.PLAIN, 18);

	public void load() {
		final Properties storedSettings = new Properties();
		final File settingsFile = SETTINGS_FILE;
		if (!settingsFile.exists()) {
			return;
		}
		try (final FileInputStream fis = new FileInputStream(settingsFile)) {
			storedSettings.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (storedSettings.getProperty("example") == null) {
			example = TesterUIJsonataExample.NONE;
		} else {
			example = TesterUIJsonataExample.valueOf(storedSettings.getProperty("example"));
		}
		if (storedSettings.getProperty("path.input") != null) {
			pathInput = Paths.get(storedSettings.getProperty("path.input"));
		}
		if (storedSettings.getProperty("path.jsonata") != null) {
			pathJsonata = Paths.get(storedSettings.getProperty("path.jsonata"));
		}
		if (storedSettings.getProperty("color.background.input") != null) {
			backgroundInput = new Color(Integer.parseInt(storedSettings.getProperty("color.background.input"), 16));
		}
		if (storedSettings.getProperty("color.background.jsonata") != null) {
			backgroundJsonata = new Color(Integer.parseInt(storedSettings.getProperty("color.background.jsonata"), 16));
		}
		if (storedSettings.getProperty("color.background.output") != null) {
			backgroundOutput = new Color(Integer.parseInt(storedSettings.getProperty("color.background.output"), 16));
		}
		if (storedSettings.getProperty("color.background.error") != null) {
			backgroundError = new Color(Integer.parseInt(storedSettings.getProperty("color.background.error"), 16));
		}
		if (storedSettings.getProperty("font") != null) {
			font = textToFont(storedSettings.getProperty("font"));
		}
	}

	public void store() {
		final TesterUIProperties storedSettings = new TesterUIProperties(new Properties());
		ensureSettingsFolder();
		final File settingsFile = SETTINGS_FILE;
		storedSettings.setProperty("example", example.name());
		storedSettings.setProperty("path.input", pathInput.toString());
		storedSettings.setProperty("path.jsonata", pathJsonata.toString());
		storedSettings.setProperty("color.background.input", Integer.toHexString(backgroundInput.getRGB()).substring(2).toUpperCase());
		storedSettings.setProperty("color.background.jsonata", Integer.toHexString(backgroundJsonata.getRGB()).substring(2).toUpperCase());
		storedSettings.setProperty("color.background.output", Integer.toHexString(backgroundOutput.getRGB()).substring(2).toUpperCase());
		storedSettings.setProperty("color.background.error", Integer.toHexString(backgroundError.getRGB()).substring(2).toUpperCase());
		storedSettings.setProperty("font", fontToText(font));
		try (final FileOutputStream fos = new FileOutputStream(settingsFile)) {
			storedSettings.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static String fontToText(final Font in) {
		return in.getFamily() + ',' + in.getFontName() + ',' + in.getStyle() + ',' + in.getSize();
	}

	public static Font textToFont(final String in) {
		String[] args = in.split(",");
		return new Font(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}

	public void ensureSettingsFolder() {
		final File settingsFolder = SETTINGS_FOLDER;
		if (!settingsFolder.exists()) {
			if (!settingsFolder.mkdirs()) {
				new RuntimeException("Could not create folder " + settingsFolder.getAbsolutePath()).printStackTrace();
				return;
			}
		}
	}


	public TesterUIJsonataExample getExample() {
		return example;
	}

	public void setExample(TesterUIJsonataExample example) {
		this.example = example;
	}

	public Path getPathInput() {
		return pathInput;
	}

	public void setPathInput(Path path) {
		pathInput = path;
	}

	public Path getPathJsonata() {
		return pathJsonata;
	}

	public void setPathJsonata(Path path) {
		pathJsonata = path;
	}

	public Color getBackgroundInput() {
		return backgroundInput;
	}

	public void setBackgroundInput(Color backgoundInput) {
		this.backgroundInput = backgoundInput;
	}

	public Color getBackgroundJsonata() {
		return backgroundJsonata;
	}

	public void setBackgroundJsonata(Color backgroundJsonata) {
		this.backgroundJsonata = backgroundJsonata;
	}

	public Color getBackgroundOutput() {
		return backgroundOutput;
	}

	public void setBackgroundOutput(Color backgroundOutput) {
		this.backgroundOutput = backgroundOutput;
	}

	public Color getBackgroundError() {
		return backgroundError;
	}

	public void setBackgroundError(Color backgroundError) {
		this.backgroundError = backgroundError;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}
}
