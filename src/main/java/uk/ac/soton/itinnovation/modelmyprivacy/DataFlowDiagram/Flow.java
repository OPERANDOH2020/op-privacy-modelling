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

package uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram;

import java.util.ArrayList;
import java.util.List;


/**
 * A transition describes the transition from state of the machine
 * to another state in the machine.
 *
 * Each transition has a set of conditions (guards) that must be true i.e.
 * the transition is taken iff all conditions of the transition are matched
 * by an event that has a occurred.
 *
 * In the case of the Interoperability Test Engine: an event is a REST based
 * operation.
 *
 * @author pjg@it-innovation.soton.ac.uk
 */
public class Flow {

    /**
     * The label/id of the transition where directed to ie. the state label.
     */
    private transient String toLabel;


    /**
     * The list of data elements attached to a flow (fields or records) in record
     * field dot notation e.g. record.field or record.field.field .
     */
    private transient List<String> data;

    /**
     * The purpose.
     */
    private transient String purpose;

    /**
     * The sequence number.
     */
    private transient String sequence;

    /**
     * Create a basic flow.
     * @param label The identifier of the transition's to i.e. the label of the
     * to state.
     */
    public Flow(final String toID, String purpose, String sequence) {
        this.toLabel = toID;
        this.purpose = purpose;
        this.sequence = sequence;
        this.data = new ArrayList<>();
    }

    /**
     * Get the to label of the flow.
     * @return The string id of the to component.
     */
    public final String readToLabel() {
        return this.toLabel;
    }

    /**
     * Get the flow seq number.
     * @return The string seq number.
     */
    public final String readSeqLabel() {
        return this.sequence;
    }

    /**
     * Get the purpose of this flow.
     * @return The string of the purpose.
     */
    public final String readPurposeLabel() {
        return this.purpose;
    }

    /**
     * Add a new guard to the transition.
     * @param guard The guard rule.
     * @throws InvalidGuardException Error in the guard specification.
     */
    public final void addData(final String fieldName) throws InvalidDataFlowDiagramException {
        this.data.add(fieldName);
    }

    /**
     * Get all the data fields on the flow.
     * @return The list of fields.
     */
    public final List<String> getData() {
        return this.data;
    }

}
