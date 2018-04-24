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
 * A Data Store in the Data Flow Diagram.
 *
 */
public class DataStore extends AbstractFlowComponent{

    /**
     *
     * @param nodeName This is the string identifier labelling the state. It must
     * be at least 2 characters long.
     * @param type must be one of the stateType enumeration. Null values are
     * not allowed.
     * @param inputSM The StateMachine context
     * @throws InvalidDataFlowDiagramException error initialising node
     */
    public DataStore(final String nodeName, String id, final FlowDiagram inputSM)
        throws InvalidDataFlowDiagramException {
        super(nodeName, id, ComponentType.DATASTORE, inputSM);


    }



}