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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLDataFlowDiagram;
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLDocument;


/**
 * In memory representation of a data flow diagram (as Java objects)
 */
public class FlowDiagram {
    /**
     * Each flow diagram is a set of nodes between which data flows.
     */
    private final transient Map<String, IFlow> nodes;

    /**
     * Construct a new state machine.
     */
    public FlowDiagram() {
        this.nodes = new HashMap<String, IFlow>();
    }

    /**
     * Get an unordered list of nodes
     * @return
     */
    public List<IFlow> getNodes() {
        List<IFlow> unorderedList = new ArrayList<>();
        unorderedList.addAll(this.nodes.values());
        return unorderedList;
    }

    /**
     * Create the in memory data flow diagram.
     * @param xmlModel The XML data flow diagram  description.
     */
    public void buildDataFlowDiagram(String xmlModel){

        try {
            // Read the pattern from the xml string input parameter
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            XMLDataFlowDiagram.createFlow(pattern.getRootElement(), this);
        } catch (InvalidDataFlowDiagramException ex) {
            Logger.getLogger(FlowDiagram.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Find a component based upon it's unique ID.
     * @param componentId The unique ID to lookup
     * @return the Flow diagram component interface.
     */
    public IFlow getComponentFromID(String componentId) {
        return this.nodes.get(componentId);
    }


}