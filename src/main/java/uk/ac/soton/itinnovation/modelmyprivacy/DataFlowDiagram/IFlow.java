/////////////////////////////////////////////////////////////////////////
//
// © University of Southampton IT Innovation Centre, 2018
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

/**
 * The IFlow component is the common interface provided by all nodes
 * in the data flow diagram.
 *
 */
public interface IFlow {

    /**
     * Enumeration defined for the three different states.
     */
    public enum ComponentType {
        /**
         * A start node of a state machine that waits on rest inputs.
         */
        ACTOR,
        /**
         * And end node of the state machine where termination occurs.
         */
        DATASTORE,

    }

    /**
     * Get the unique label identifier of the node.
     * @return
     **/
    public String getLabel();

    /**
     * Get the type the node.
     * @return
     **/
    public ComponentType getType();

    /**
     * Add a new flow to an actor. Flows are only added in the "from" element.
     * The procedure adds the flow to the "to" list.
     * @param label
     */
    public void addFlow(Flow label);
}

