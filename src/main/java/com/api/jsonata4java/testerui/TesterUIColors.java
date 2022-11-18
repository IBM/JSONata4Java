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

import java.awt.Color;

/**
 * Defines a set color schemas for the different text areas in the UI.
 * One color schema consists 4 colors.
 * <ul>
 *   <li><b>input:</b> background color of the "Input" JSON or XML text area</li>
 *   <li><b>jsonata:</b> background color of the "JSONata" (mapping expressions) text area</li>
 *   <li><b>output:</b> background color of the "Output" JSON text area</li>
 *   <li><b>error:</b> background color for output areas containing (input / jsonata) or showing (output) an error</li>
 * </ul>
 *
 * @author Martin Bluemel
 */
public enum TesterUIColors {

        // special semantics: custom settings
        CUSTOM("- (custom settings)", 0x000000, 0x000000, 0x000000, 0x000000), EXERCISER("exerciser", 0xFFFFFB, 0xFFFFFB, 0xEEEEEE, 0xFFEEEE), PASTEL("pastel", 0xEEFFFF, 0xFFFFEE,
            0xEEFFEE, 0xFFEEEE);

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
