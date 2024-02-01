/**
 * (c) Copyright 2018, 2019 IBM Corporation
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Since Swing does not yet provide a font chooser we have to write it.
 *
 * @author Bluemel1.Martin
 */
public class TesterUIFontChooser {

    private static final int FRAME_SIZE_X = 300;
    private static final int FRAME_SIZE_Y = 410;
    private static final int PAD = 5;
    private static final Insets INSETS = new Insets(PAD, PAD, PAD, PAD);

    private final TesterUI ui;
    private final TesterUISettings settings;
    private final TesterUIPreferences prefui;
    private Font newFont;

    private final JFrame frame = new JFrame("JSONata4Java Font");
    private final DefaultListModel<String> nameListModel = new DefaultListModel<>();
    private final JList<String> nameList = new JList<>(nameListModel);
    private final JScrollPane nameSp = new JScrollPane(nameList);
    private final JComboBox<FontStyle> styleBox = new JComboBox<>(FontStyle.values());
    private final DefaultComboBoxModel<Integer> sizeBoxModel = new DefaultComboBoxModel<>();
    private final JComboBox<Integer> sizeBox = new JComboBox<>(sizeBoxModel);

    private final JPanel inputPanel = new JPanel();
    private final JPanel southPanel = new JPanel();
    private final JPanel buttonsPanel = new JPanel();
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Cancel");

    private static enum FontStyle {
            PLAIN, BOLD, ITALIC
    };

    private final static int[] fontSizes = {
        8, 9, 10, 12, 14, 16, 18, 24, 32, 48, 64
    };

    public TesterUIFontChooser(TesterUI ui, TesterUISettings settings, TesterUIPreferences prefui) {
        this.ui = ui;
        this.settings = settings;
        this.prefui = prefui;
    }

    public void open() {
        configureComponents();
        buildLayout();
        frame.setVisible(true);
    }

    protected void configureComponents() {

        frame.setLayout(new BorderLayout(0, 0));
        frame.setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {
                cancel();
            }
        });

        inputPanel.setLayout(new GridBagLayout());
        southPanel.setLayout(new BorderLayout());
        buttonsPanel.setLayout(new GridBagLayout());
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (final String name : ge.getAvailableFontFamilyNames()) {
            nameListModel.addElement(name);
        }
        nameList.setSelectedIndex(0);
        nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nameList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                fontChanged();
            }
        });
        nameList.setSelectedValue(settings.getFont().getFontName(), true);
        nameSp.setMinimumSize(new Dimension(100, 100));
        for (int i = 0; i < fontSizes.length; i++) {
            sizeBoxModel.addElement(Integer.valueOf(fontSizes[i]));
        }
        styleBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                fontChanged();
            }
        });
        styleBox.setSelectedIndex(settings.getFont().getStyle());
        sizeBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                fontChanged();
            }
        });
        sizeBox.setSelectedItem(settings.getFont().getSize());
    }

    protected void buildLayout() {

        int x = 1;
        int y = 1;
        inputPanel.add(new JLabel("Name"), new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
        inputPanel.add(nameSp, new GridBagConstraints(x++, y++, 1, 1, 1.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

        x = 1;
        inputPanel.add(new JLabel("Style"), new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
        inputPanel.add(styleBox, new GridBagConstraints(x++, y++, 1, 1, 1.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

        x = 1;
        inputPanel.add(new JLabel("Size"), new GridBagConstraints(x++, y, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
        inputPanel.add(sizeBox, new GridBagConstraints(x++, y++, 1, 1, 1.0, 1.0,
            GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, INSETS, 0, 0));

        buttonsPanel.add(okButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));
        buttonsPanel.add(cancelButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, INSETS, 0, 0));

        southPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(southPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
    }

    private void ok() {
        settings.setFont(newFont);
        prefui.setFont(newFont);
        frame.dispose();
    }

    private void cancel() {
        ui.setFont(settings.getFont());
        frame.dispose();
    }

    private void fontChanged() {
        final String name = nameList.getSelectedValue();
        final FontStyle style = (FontStyle) styleBox.getSelectedItem();
        final Integer size = (Integer) sizeBox.getSelectedItem();
        if (name != null && style != null && size != null) {
            newFont = new Font(name, style.ordinal(), size);
            ui.setFont(newFont);
        }
    }
}
