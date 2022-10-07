package com.api.jsonata4java.testerui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.api.jsonata4java.expressions.EvaluateException;
import com.api.jsonata4java.expressions.Expressions;
import com.api.jsonata4java.expressions.ParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class TesterUI {

	private Expressions expressions;
	private final ObjectMapper jsonMapper = new ObjectMapper();
	private final XmlMapper xmlMapper = new XmlMapper();
	private TesterUISettings settings = new TesterUISettings();

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu menuFile = new JMenu("File");
	private final JMenuItem menuItemExit = new JMenuItem("Exit");
	private final JMenu menuEdit = new JMenu("Edit");
	private final JMenuItem menuItemFormatInput = new JMenuItem("Format input");
	private final JMenu menuSettings = new JMenu("Settings");
	private final JMenuItem menuItemPreferences = new JMenuItem("Preferences...");
	private final Font font = new Font("Consolas", Font.PLAIN, 18);
	private final JTextArea inputArea = new JTextArea();
	private final JTextArea jsonataArea = new JTextArea();
	private final JTextArea outputArea = new JTextArea();
	private final JScrollPane inputSp = new JScrollPane(inputArea);
	private final JScrollPane jsonataSp = new JScrollPane(jsonataArea);
	private final JScrollPane outputSp = new JScrollPane(outputArea);
	private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	private final JSplitPane splitPaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private final JFrame frame = new JFrame();

	private final UndoManager undoManInput = new UndoManager();
	private final UndoManager undoManJsonata = new UndoManager();

	private static final String REDO_KEY_NAME = "redo";
	private static final String UNDO_KEY_NAME = "undo";

	private final KeyStroke undoKey = KeyStroke.getKeyStroke("control Z");
	private final KeyStroke redoKey = KeyStroke.getKeyStroke("control Y");

	protected TesterUI() throws IOException {

		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

		settings.load();

		inputArea.setFont(font);
		try {
			inputArea.setText(readFile(settings.getPathInput()));
		} catch (NoSuchFileException e) {
			inputArea.setText(readFile(TesterUISettings.DEFAULT_PATH_INPUT));
		}
		jsonataArea.setFont(font);
		try {
			jsonataArea.setText(readFile(settings.getPathJsonata()));
		} catch (NoSuchFileException e) {
			jsonataArea.setText(readFile(TesterUISettings.DEFAULT_PATH_JSONATA));
		}
		outputArea.setFont(font);
		outputArea.setEditable(false);

		inputSp.setMinimumSize(new Dimension(400, 0));
		jsonataSp.setMinimumSize(new Dimension(0, 50));

		splitPane.add(inputSp);
		splitPaneRight.add(jsonataSp);
		splitPaneRight.add(outputSp);
		splitPane.add(splitPaneRight);

		frame.setTitle("JSONata4Java Tester UI");
		frame.getContentPane().add(splitPane);
		frame.setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		menuBar.add(menuEdit);
		menuEdit.add(menuItemFormatInput);
		menuItemFormatInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formatInput();
			}
		});
		menuBar.add(menuSettings);
		menuSettings.add(menuItemPreferences);
		menuItemPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPreferences();
			}
		});
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		final Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension windim = new Dimension(screendim.getWidth() > 1500 ? 1500 : (int) (screendim.getWidth() / 2),
				screendim.getHeight() > 800 ? 800 : (int) (screendim.getHeight() / 2));
		frame.setSize(windim);
		frame.setLocation(100, 100);

		parseMappingDescription();
		map();

		makeUndoable(inputArea, undoManInput);
		makeUndoable(jsonataArea, undoManJsonata);
		listenToInputChanges();
		listenToJsonataChanges();
	}

	private void listenToInputChanges() {
		inputArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				inputChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				inputChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				inputChanged();
			}
		});
	}

	private void listenToJsonataChanges() {
		jsonataArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				jsonataChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				jsonataChanged();
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				jsonataChanged();
			}
		});
	}

	private void makeUndoable(JTextArea area, UndoManager undoManager) {
		area.getDocument().addUndoableEditListener(event -> undoManager.addEdit(event.getEdit()));
		area.getActionMap().put(UNDO_KEY_NAME, new AbstractAction(UNDO_KEY_NAME) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undoManager.canUndo()) {
						undoManager.undo();
					}
				} catch (CannotUndoException ignore) {
				}
			}
		});
		area.getInputMap().put(undoKey, UNDO_KEY_NAME);
		area.getActionMap().put(REDO_KEY_NAME, new AbstractAction(REDO_KEY_NAME) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					if (undoManager.canRedo()) {
						undoManager.redo();
					}
				} catch (CannotRedoException ignore) {
				}
			}
		});
		area.getInputMap().put(redoKey, REDO_KEY_NAME);
	}

	private static final File INPUT_FILE_JSON = new File(TesterUISettings.SETTINGS_FOLDER, "input.json");
	private static final File INPUT_FILE_XML = new File(TesterUISettings.SETTINGS_FOLDER, "input.xml");
	private static final File JSONATA_FILE = new File(TesterUISettings.SETTINGS_FOLDER, "expression.jsonata");

	private void exit() {
		try {
			settings.ensureSettingsFolder();
			switch (getFormat(inputArea.getText())) {
			case XML:
				Files.write(INPUT_FILE_XML.toPath(), inputArea.getText().getBytes());
				settings.setPathInput(INPUT_FILE_XML.toPath());
				break;
			default:
				Files.write(INPUT_FILE_JSON.toPath(), inputArea.getText().getBytes());
				settings.setPathInput(INPUT_FILE_JSON.toPath());
				break;
			}
			Files.write(JSONATA_FILE.toPath(), jsonataArea.getText().getBytes());
			settings.setPathJsonata(JSONATA_FILE.toPath());
			settings.setExample(TesterUIJsonataExample.fromContent(inputArea.getText(), jsonataArea.getText()));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		settings.store();
		System.out.println("Bye bye");
		System.exit(0);
	}

	private void formatInput() {
		try {
			switch (getFormat(inputArea.getText())) {
			case XML:
				inputArea.setText(xmlMapper.writeValueAsString(xmlMapper.readTree(inputArea.getText())));
				break;
			default:
				inputArea.setText(jsonMapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(jsonMapper.readTree(inputArea.getText())));
				break;
			}
		} catch (JsonProcessingException e) {
			System.err.println("Input error: " + e.getMessage());
			this.outputArea.setText("Input error: " + e.getMessage());
			this.inputArea.setBackground(settings.getBackgroundError());
			this.outputArea.setBackground(settings.getBackgroundError());
		}
	}

	private void showPreferences() {
		new TesterUIPerferences(this, this.settings).open();
	}

	private void inputChanged() {
		if (this.expressions != null || parseMappingDescription()) {
			map();
			this.settings.setExample(TesterUIJsonataExample.NONE);
		}
	}

	private void jsonataChanged() {
		if (parseMappingDescription()) {
			map();
			this.settings.setExample(TesterUIJsonataExample.NONE);
		}
	}

	private boolean parseMappingDescription() {
		final String mappingDescription = this.jsonataArea.getText();
		if (mappingDescription.length() == 0) {
			return false;
		}
		try {
			this.expressions = Expressions.parse(mappingDescription);
			return true;
		} catch (ParseException | IOException | RuntimeException e) {
			System.err.println("JSONata syntax error: " + e.getMessage());
			this.outputArea.setText("JSONata syntax error: " + e.getMessage());
			this.jsonataArea.setBackground(settings.getBackgroundError());
			this.outputArea.setBackground(settings.getBackgroundError());
			this.expressions = null;
			return false;
		}
	}

	private void map() {
		JsonNode inNode = null;
		try {
			switch (getFormat(inputArea.getText())) {
			case XML:
				inNode = xmlMapper.readTree(inputArea.getText());
				break;
			default:
				inNode = jsonMapper.readTree(inputArea.getText());
				break;
			}
		} catch (JsonProcessingException | RuntimeException e) {
			System.err.println("Input error: " + e.getMessage());
			this.outputArea.setText("Input error: " + e.getMessage());
			this.inputArea.setBackground(settings.getBackgroundError());
			this.outputArea.setBackground(settings.getBackgroundError());
			return;
		}
		try {
			final JsonNode outNode = this.expressions.evaluate(inNode);
			String newOutput = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(outNode);
			if (newOutput == null || newOutput.trim().equals("null")) {
				newOutput = "** no match **";
			}
			this.outputArea.setText(newOutput);
			System.out.println("Mapped successfully");
			this.inputArea.setBackground(settings.getBackgroundInput());
			this.jsonataArea.setBackground(settings.getBackgroundJsonata());
			this.outputArea.setBackground(settings.getBackgroundOutput());
		} catch (JsonProcessingException | EvaluateException | RuntimeException e) {
			System.err.println("JSONata mapping error: " + e.getMessage());
			this.outputArea.setText("JSONata mapping error: " + e.getMessage());
			this.jsonataArea.setBackground(settings.getBackgroundError());
			this.outputArea.setBackground(settings.getBackgroundError());
		}
	}

	enum Format {
		XML, JSON
	};

	private Format getFormat(String text) {
		if (text.trim().startsWith("<")) {
			return Format.XML;
		}
		return Format.JSON;
	}

	protected static String readFile(Path filePath) throws IOException {
		byte[] bytes = Files.readAllBytes(filePath);
		String s = new String(bytes, StandardCharsets.UTF_8);
		return s;
	}

	protected String jsonToXml(final String json) throws IOException {
		JsonNode node = jsonMapper.readValue(json, JsonNode.class);
		xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
		xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
		ObjectWriter ow = xmlMapper.writer().withRootName("root");
		StringWriter w = new StringWriter();
		ow.writeValue(w, node);
		return w.toString();
	}

	protected String xmlToJson(final String xml) throws IOException {
		JsonNode node = xmlMapper.readTree(xml.getBytes());
		return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new TesterUI().frame.setVisible(true);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void loadInput(Path path) {
		try {
			inputArea.setText(readFile(path));
		} catch (NoSuchFileException e) {
		} catch (IOException e) {
		}
	}

	public void loadJsoanata(Path path) {
		try {
			jsonataArea.setText(readFile(path));
		} catch (NoSuchFileException e) {
		} catch (IOException e) {
		}
	}

	public void setBackgroundInput(Color color) {
		this.inputArea.setBackground(color);
	}

	public void setBackgroundJsonata(Color color) {
		this.jsonataArea.setBackground(color);
	}

	public void setBackgroundOutput(Color color) {
		this.outputArea.setBackground(color);
	}
}
