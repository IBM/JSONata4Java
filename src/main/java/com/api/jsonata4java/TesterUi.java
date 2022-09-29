package com.api.jsonata4java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
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

public class TesterUi {

	private Expressions expressions;
	private final ObjectMapper mapper = new ObjectMapper();

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


	private TesterUi() throws IOException {

		inputArea.setText(readFile("src/test/resources/exerciser/address.json"));
		jsonataArea.setText(readFile("src/test/resources/exerciser/addressExpression.jsonata"));
		outputArea.setEditable(false);

		splitPane.add(inputSp);
		splitPaneRight.add(jsonataSp);
		splitPaneRight.add(outputSp);
		splitPane.add(splitPaneRight);

		frame.setTitle("JSONata4Java Tester UI");
		final Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
		// frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.getContentPane().add(splitPane);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		frame.setSize(new Dimension(screendim.getWidth() > 800 ? 800 : (int) (screendim.getWidth() / 2),
				screendim.getHeight() > 500 ? 500 : (int) (screendim.getHeight() / 2)));
		frame.setLocation(100, 100);
		// frame.pack();

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

	private void close() {
		System.out.println("Bye bye");
		System.exit(0);
	}

	private void inputChanged() {
		if (this.expressions != null || parseMappingDescription()) {
			map();
		}
	}

	private void jsonataChanged() {
		if (parseMappingDescription()) {
			map();
		}
	}

	private boolean parseMappingDescription() {
		try {
			this.expressions = Expressions.parse(this.jsonataArea.getText());
			return true;
		} catch (ParseException | IOException | RuntimeException e) {
			System.err.println("JSONata syntax error: " + e.getMessage());
			this.outputArea.setText("JSONata syntax error: " + e.getMessage());
			this.jsonataArea.setBackground(new Color(0xFFEEEE));
			this.outputArea.setBackground(new Color(0xFFEEEE));
			this.expressions = null;
			return false;
		}
	}

	private void map() {
		JsonNode inNode = null;
		try {
			inNode = mapper.readTree(inputArea.getText());
		} catch (JsonProcessingException | RuntimeException e) {
			System.err.println("Input error: " + e.getMessage());
			this.outputArea.setText("Input error: " + e.getMessage());
			this.inputArea.setBackground(new Color(0xFFEEEE));
			this.outputArea.setBackground(new Color(0xFFEEEE));
			return;
		}
		try {
			final JsonNode outNode = this.expressions.evaluate(inNode);
			final String newOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(outNode);
			this.outputArea.setText(newOutput);
			System.out.println("Mapped successfully");
			this.inputArea.setBackground(new Color(0xEEFFFF));
			this.jsonataArea.setBackground(new Color(0xFFFFEE));
			this.outputArea.setBackground(new Color(0xEEFFEE));
		} catch (JsonProcessingException | EvaluateException | RuntimeException e) {
			System.err.println("JSONata mapping error: " + e.getMessage());
			this.outputArea.setText("JSONata mapping error: " + e.getMessage());
			this.jsonataArea.setBackground(new Color(0xFFEEEE));
			this.outputArea.setBackground(new Color(0xFFEEEE));
		}
	}

	private static String readFile(String filePath) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(filePath));
		String s = new String(bytes, StandardCharsets.UTF_8);
		return s;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new TesterUi().frame.setVisible(true);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}
}
