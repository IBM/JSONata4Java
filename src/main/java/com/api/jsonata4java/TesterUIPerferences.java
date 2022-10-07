package com.api.jsonata4java;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class TesterUIPerferences extends JDialog {

	private final TesterUI ui;

	private final TesterUISettings settings;

	public TesterUIPerferences(final TesterUI ui, final TesterUISettings settings) {
		this.ui = ui;
		this.settings = settings;
	}

	private static final int FRAME_SIZE_Y = 600;

	private static final int FRAME_SIZE_X = 800;

	private final JFrame frame = new JFrame("JSONata4Java Tester UI Settings");

	private static final String DOTS_3 = "...";

	private final JTextField pathInput = new JTextField();
	private final JButton changePathInput = new JButton(DOTS_3);
	private final JTextField pathJsonata = new JTextField();
	private final JButton changePathJsonata = new JButton(DOTS_3);
	private final JComboBox<TesterUIJsonataExample> examples = new JComboBox<TesterUIJsonataExample>(TesterUIJsonataExample.values());

	private final JPanel inputPanel = new JPanel();
	private final JPanel southPanel = new JPanel();
	private final JPanel buttonsPanel = new JPanel();
	private final JButton okButton = new JButton("Ok");

	private static final int PAD = 5;

	private static final Insets INSETS = new Insets(PAD, PAD, PAD, PAD);

	private static final long serialVersionUID = 1L;

	public void open() {
		readProps();
		initUi();
		frame.setVisible(true);
	}

	private void changeFilePath(final JTextField textField, final String description, final String... suffix) {
		final JFileChooser fc = new JFileChooser();
		final String path = textField.getText();
		final File fileChosenBefore = path != null && path.trim().length() > 0 ? new File(path) : null;
		if (fileChosenBefore != null && fileChosenBefore.getParentFile().exists()) {
			fc.setCurrentDirectory(fileChosenBefore.getParentFile());
		}
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public boolean accept(final File f) {
				return acceptFile(f, suffix);
			}
		});
		int ret = fc.showOpenDialog(frame.getContentPane());
		switch (ret) {
		case 0:
			textField.setText(fc.getSelectedFile().getAbsolutePath());
			break;
		default:
			break;
		}
	}

	private boolean acceptFile(final File f, final String... suffix) {
		if (f.isDirectory()) {
			return true;
		}
		try {
			for (int i = 0; i < suffix.length; i++) {
				if (f.getName().endsWith(suffix[i])) {
					return true;
				}
			}
			return false;
		} catch (StringIndexOutOfBoundsException e) {
			return false;
		}
	}

	private void readProps() {
		examples.setSelectedItem(settings.getExample());
		pathInput.setText(settings.getPathInput().toString());
		pathJsonata.setText(settings.getPathJsonata().toString());
	}

	private void close() {
		frame.setVisible(false);
		frame.dispose();
	}

	private void initUi() {
		uiConfigure();
		uiSetListeners();
		uiCompose();
	}

	private void uiConfigure() {
		pathInput.setEditable(false);
		pathJsonata.setEditable(false);
		frame.setLayout(new BorderLayout(0, 0));
		frame.setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.setIconImage(parentApp.getIconApp().getImage());
		inputPanel.setLayout(new GridBagLayout());
		southPanel.setLayout(new BorderLayout());
		buttonsPanel.setLayout(new GridBagLayout());
	}

	private void uiCompose() {
		int x = 1;
		int y = 1;
		inputPanel.add(new JLabel("JSONata Examples"), new GridBagConstraints(1, y, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
		inputPanel.add(examples, new GridBagConstraints(2, y++, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

		x = 1;
		inputPanel.add(new JLabel("Input JSON / XML Path"), new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
		inputPanel.add(pathInput, new GridBagConstraints(x++, y, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
		inputPanel.add(changePathInput, new GridBagConstraints(x++, y++, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

		x = 1;
		inputPanel.add(new JLabel("JSONata Expression Path"), new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
		inputPanel.add(pathJsonata, new GridBagConstraints(x++, y, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));
		inputPanel.add(changePathJsonata, new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

		buttonsPanel.add(okButton, new GridBagConstraints(x++, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));

		southPanel.add(buttonsPanel, BorderLayout.SOUTH);

		frame.add(inputPanel, BorderLayout.NORTH);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void uiSetListeners() {
		uiListenerFrame();
		uiListenerExamples();
		uiListenerChangePathInput();
		uiListenerChangePathJsonata();
		uiListenerOkButton();
	}

	private void uiListenerFrame() {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				close();
			}
		});
	}

	private void uiListenerExamples() {
		examples.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TesterUIJsonataExample example = (TesterUIJsonataExample) examples.getSelectedItem();
				pathInput.setText(example.getPathInput().toAbsolutePath().toString());
				ui.loadInput(example.getPathInput());
				pathJsonata.setText(example.getPathJsonata().toAbsolutePath().toString());
				ui.loadJsoanata(example.getPathJsonata());
				settings.setExample((TesterUIJsonataExample) examples.getSelectedItem());
			}
		});
	}

	private void uiListenerChangePathInput() {
		changePathInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String pathBefore = pathInput.getText();
				changeFilePath(pathInput, "JSON or XML", ".json", ".xml");
				if (!pathInput.getText().equals(pathBefore)) {
					ui.loadInput(new File(pathInput.getText()).toPath());
					settings.setPathInput(new File(pathInput.getText()).toPath());
				}
			}
		});
	}

	private void uiListenerChangePathJsonata() {
		changePathJsonata.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String pathBefore = pathInput.getText();
				changeFilePath(pathJsonata, "JSONata mapping description", ".jsonata");
				if (!pathJsonata.getText().equals(pathBefore)) {
					ui.loadJsoanata(new File(pathJsonata.getText()).toPath());
					settings.setPathJsonata(new File(pathJsonata.getText()).toPath());
				}
			}
		});
	}

	private void uiListenerOkButton() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}
}
