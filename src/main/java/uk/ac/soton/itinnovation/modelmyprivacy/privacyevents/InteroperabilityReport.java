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

import java.io.PrintStream;

/**
 * The Interoperability report is a generated report of the trace through
 * the interoperability state machine. It is a final result that is used
 * for a simple request response (e.g. POST) operation to monitor a system.
 * Alternatively the dynamic output stream can be used to monitor in progress
 * execution
 * @author pjg
 */
public class InteroperabilityReport {

    /**
     * This is the editable content of the report (i.e. the body). We initialise
     * with a title.
     */
    private transient String textContent = "Output from Interoperability Cradle\n";


    /**
     * The stream output of the text on the local host.
     */
    private transient PrintStream output;

    /**
     * Boolean indicator if the report is carried out during the execution i.e.
     * step by step (true); or as a single batch report at the end (false).
     */
    private final transient boolean realtime;

    /**
     * Method to add a new line to the report. Simple formatting method.
     */
    private void newline() {
        this.textContent += "\n";
    }

    /**
     * Empty constructor. Simple interoperability report written in batch
     * mode. Output to be displayed as text.
     */
    public InteroperabilityReport() {
        this.realtime = false;
    }

    /**
     * Add a string to a new line.
     * @param newval String to report.
     */
    public final void println(final String newval) {
        newline();
        this.textContent += newval;
        newline();
    }

    /**
    * Add a tabbed string new line.
    * @param newval The text to add as a tabbed line.
    */
    public final void printtabline(final String newval) {
        this.textContent += "\t" + newval;
        newline();
    }

    /**
     * Produce a text version of the output report. Typically displayed to
     * a text field or the console.
     * @return The interoperability report as a single string.
     */
    public final String outputReport() {
        return this.textContent;
    }

}
