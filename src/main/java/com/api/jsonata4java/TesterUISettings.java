package com.api.jsonata4java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TesterUISettings {

	public static final File SETTINGS_FOLDER = new File(System.getProperty("user.home") + File.separator + ".jsonata4java");
	public static final File SETTINGS_FILE = new File(SETTINGS_FOLDER, "preferences.properties");
	public static final Path DEFAULT_PATH_INPUT = Paths.get("src/test/resources/exerciser/address.json");
	public static final Path DEFAULT_PATH_JSONATA = Paths.get("src/test/resources/exerciser/addressExpression.jsonata");

	private Path pathInput = DEFAULT_PATH_INPUT;
	private Path pathJsonata = DEFAULT_PATH_JSONATA;

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
		if (storedSettings.getProperty("path.input") != null) {
			pathInput = Paths.get(storedSettings.getProperty("path.input"));
		}
		if (storedSettings.getProperty("path.jsonata") != null) {
			pathJsonata = Paths.get(storedSettings.getProperty("path.jsonata"));
		}
	}

	public void store() {
		final Properties storedSettings = new Properties();
		ensureSettingsFolder();
		final File settingsFile = SETTINGS_FILE;
		storedSettings.setProperty("path.input", pathInput.toString());
		storedSettings.setProperty("path.jsonata", pathJsonata.toString());
		try (final FileOutputStream fos = new FileOutputStream(settingsFile)) {
			storedSettings.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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
}
