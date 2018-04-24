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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFlowComponent implements IFlow {

    /**
     * Flow Component label.
     */
    protected final transient String nodeLabel;

    /**
     * The unique ID of the DFD label (note labels are not unique in a DFD and
     * hence, a component identifier is also needed.
     */
    protected final transient String flowComponentID;

    /**
     * The values describing the state type, start, trigger, etc.
     * Can be start and trigger, or start and normal.
     */
    protected ComponentType nodeType;

    /**
     * Each node has a set of one or more nodes that it points to. An end
     * node is the exception and the end node must have zero entries.
     */
    protected final transient List<Flow> outNodes;

    /**
     * Each node has a set of incoming transitions. A start node is an exception
     *
     */
    protected final transient List<Flow> inNodes;

    /**
     * The state machine that this state node belongs to.
     */
    protected transient FlowDiagram flowDiagram;

    /**
     * Creates a new instance of OpenCOMComponent.
     * @param nodeName
     * @param nodeID
     * @param cType
     * @param inputFD
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.InvalidDataFlowDiagramException
     */
    public AbstractFlowComponent(final String nodeName, final String nodeID, ComponentType cType,
            final FlowDiagram inputFD) throws InvalidDataFlowDiagramException{

        /**
         * Check that the id is unique.
         */

        if ((nodeName == null) || (cType == null)) {
                throw new InvalidDataFlowDiagramException("State " + nodeName + " contains"
                        + "null values for input");
        }
        if (nodeName.length() < 2) {
            throw new InvalidDataFlowDiagramException("Invalid label - must be"
                    + "at least 2 characters long");
        }

        this.nodeLabel = nodeName;
        this.flowDiagram = inputFD;
        this.nodeType = cType;
        this.flowComponentID = nodeID;

        outNodes = new ArrayList<Flow>();
        inNodes = new ArrayList<Flow>();

    }

    /**
     * Get the actor label. This can be an individual, a role, a service name etc.
     * @return The String
     */
    @Override
    public String getLabel() {
        return this.nodeLabel;
    }

    /**
     * Get the component type.
     * @return The Actor component type.
     */
    @Override
    public ComponentType getType() {
        return this.nodeType;
    }

    /**
     * Add a new flow to an actor. Flows are only added in the "from" element.
     * The procedure adds the flow to the "to" list.
     * @param newFlow
     * @param label
     */
    @Override
    public void addFlow(Flow newFlow) {
        this.outNodes.add(newFlow);

        /**
         * Find the "to" component
         */
        String readToLabel = newFlow.readToLabel();

        this.flowDiagram.getNodes();
    }

}
