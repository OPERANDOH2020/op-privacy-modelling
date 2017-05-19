/////////////////////////////////////////////////////////////////////////
//
// © University of Southampton IT Innovation Centre, 2017
//
// Copyright in this library belongs to the University of Southampton
// University Road, Highfield, Southampton, UK, SO17 1BJ
//
// This software may not be used, sold, licensed, transferred, copied
// or reproduced in whole or in part in any manner or form or in or
// on any media by any person other than in accordance with the terms
// of the Licence Agreement supplied with the software, or otherwise
// without the prior written consent of the copyright owners.
//
// This software is distributed WITHOUT ANY WARRANTY, without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE, except where stated in the Licence Agreement supplied with
// the software.
//
// Created By : Paul Grace
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////

package uk.ac.soton.itinnovation.modelmyprivacy.privacyevents;

import java.io.Serializable;

/**
 * A representation of a privacy action e.g. read, anonymise,
 * pseudo-anonymise data.
 */
public class PrivacyAction implements Serializable {

    /**
     * Portable serializable class.
     */
    public static final long serialVersionUID = 1L;

    /** XML <guards> tag constant. */
    public static final String DATAIN_LABEL = "datainput";

    /** XML <guards><action> tag constant. */
    public static final String DATAOUT_LABEL = "dataoutput";

    private final String actionValue;
    private final Object inputValue;
    private final Object outputValue;

    /**
     * Constructor for simple action.
     * @param action The action label (CRUD, anon etc.)
     */
    public PrivacyAction(final String action){
        this.actionValue = action;
        inputValue = null;
        outputValue = null;
    }

    /**
     * Constructor for complex action.
     * @param action The action label (CRUD, anon etc.)
     * @param input The any data input for the action (privacy of data before)
     * @param output The any data output for the action (privacy of data after)
     */
    public PrivacyAction(final String action, Object input, Object output){
        this.actionValue = action;
        this.inputValue = input;
        this.outputValue = output;
    }

    /**
     * Getter for the data action of the event.
     * @return The data action.
     */
    public final String getAction() {
        return actionValue;
    }

    /**
     * Getter for the data action of the event.
     * @return The data action.
     */
    public final Object getInput() {
        return inputValue;
    }

    /**
     * Getter for the data action of the event.
     * @return The data action.
     */
    public final Object getOutput() {
        return outputValue;
    }

}
