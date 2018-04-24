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
 * A key element in the DFD describing an actor. The same actor can
 * occur multiple times, hence the label (id) need not be unique.
 */
public class ActorNode extends AbstractFlowComponent {

    /**
     *
     * @param nodeName This is the string identifier labelling the actor. It must
     * be at least 2 characters long.
     * @param inputDFD The DFD context. An actor object only exists in a specific
     * flow diagram object.
     * @throws InvalidDataFlowDiagramException error initialising node
     */
    public ActorNode(final String nodeName, String id, final FlowDiagram inputDFD)
        throws InvalidDataFlowDiagramException {
        super(nodeName, id, ComponentType.ACTOR, inputDFD);

    }


}