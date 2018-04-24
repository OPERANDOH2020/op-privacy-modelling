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

package uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.ActorNode;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.Flow;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.FlowDiagram;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.IFlow;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.InvalidDataFlowDiagramException;


/**
 * XML operations specific to the creation of a data flow diagram in memory.
 */
public final class XMLDataFlowDiagram {

    /**
     * XML actor tag label constant.
     */
    public static final String ACTOR_LABEL = "actor";

    /**
     * XML flow tag label constant.
     */
    public static final String FLOW_LABEL = "flow";

    /**
     * XML label tag label constant.
     */
    public static final String LABEL_LABEL = "label";

    /**
     * XML ID tag label constant.
     */
    public static final String ID_LABEL = "id";

    /**
     * XML type tag label constant.
     */
    public static final String COMPONENT_TYPE = "type";

    /**
     * XML TO_LABEL type content label constant.
     */
    public static final String TO_LABEL = "to";

    /**
     * XML FROM_LABEL type content label constant.
     */
    public static final String FROM_LABEL = "from";

    /**
     * Label in XML specification of the state machine architecture
     * - model tag <model>
     */
    public static final String DFD_LABEL = "dfd";

    /**
     * XML name label constant.
     */
    public static final String DATA_NAME = "name";

    /**
     * XML value label constant.
     */
    public static final String DATA_VALUE = "value";

    /**
     * Utility class with a private constructor.
     */
    private XMLDataFlowDiagram() {
        // no implementation.
    }

     /**
      * Retrieve the list of guards attached to a particular transition. That
      * is, this operation can be used to extract the interoperability tests
      * that must conform to true if the transition is to be followed.
      *
      * @param transition The specified transition in the JDOM version of the
      * state machine
      * @param archDesc Reference to the current architecture pattern.
      * @return The list of guards in the xml desciption.
      * @see Guard
      * @throws InvalidTransitionException error in the transition specification.
      */
     private static Flow getFlowFromXML(final Element flow)
        throws InvalidDataFlowDiagramException {

        final String to = flow.getChildText(TO_LABEL);
        final String sequence = flow.getChildText("sequence");
        final String purpose = flow.getChildText("purpose");

        Flow newFlow = new Flow(to, sequence, purpose);
        Element child = flow.getChild("data");
        List<Element> children = child.getChildren();
        for(Element el: children) {
            newFlow.addData(el.getChildText("field"));
        }

        return newFlow;
     }




     /**
      * Add the transitions to a given state from the information given in
      * the xml specification.
      *
      * @param elState The xml specification to find transitions in.
      * @param states The current set of states being produced.
      * @param archDesc The overall architecture reference context.
      * @throws InvalidTransitionException Error parsing the information into
      * a data structure about the transitions.
      */
     private static void addFlows(final Element elState, final Map<String,
             IFlow> nodes) throws InvalidDataFlowDiagramException {

         final IFlow fromNode = nodes.get(elState.getChildText(ID_LABEL));

         final List<Element> tOut = elState.getChildren(FLOW_LABEL);
         for (Element eltIndex : tOut) {
             final String toLabel = eltIndex.getChildText(TO_LABEL);
             final String seqLabel = eltIndex.getChildText("sequence");
             final String purpLabel = eltIndex.getChildText("purpose");
             Flow flow = new Flow(toLabel, seqLabel, purpLabel);
             List<Element> children = eltIndex.getChild("data").getChildren();
             for(Element field: children) {
                 flow.addData(field.getValue());
             }
             fromNode.addFlow(flow);
         }
     }

     /**
      * Based on the XML nodes list - generate the set of states.
      * This method also calls functions to build the transition map.
      *
      * @param doc The XML document with a fully formed state machine as
      * input. An invalid state machine input will generate an exception.
      * @param arch The overall architecture context of the operation.
      * @return The set of created states from the xml spec.
      * @throws InvalidStateMachineException Error caused by invalid behaviour specification.
      */
     private static Map<String, IFlow> createNodes(final Element doc, final FlowDiagram sm)
             throws InvalidDataFlowDiagramException {

        final Map<String, IFlow> states = new HashMap();
        try {

            final XPath xpa = XPath.newInstance("//" + ACTOR_LABEL);

            final List<Element> xmlStates = xpa.selectNodes(doc);
            for (Element eltIndex : xmlStates) {
                // Get the state label
                final String label = eltIndex.getChildText(LABEL_LABEL);
                final String id = eltIndex.getChildText(ID_LABEL);
                states.put(id, new ActorNode(label, id, sm));
                addFlows(eltIndex, states);
            }

        } catch (JDOMException ex) {
            throw new InvalidDataFlowDiagramException("Invalid XML input", ex);
        } catch (InvalidDataFlowDiagramException ex) {
            Logger.getLogger(XMLDataFlowDiagram.class.getName()).log(Level.SEVERE, null, ex);
        }
        return states;
     }

     /**
      * Create a data flow diagram.
      *
      * @param doc The XML instance of the state machine domain language to
      * be turned into an in-memory state machine
     * @param fDiag The State Machine to fill with content.
     * @throws InvalidStateMachineException error during the parsing of the state
     * machine from the XML.
     */
    public static void createFlow(final Element doc, final FlowDiagram fDiag ) throws InvalidDataFlowDiagramException {

        try {
            /**
             * First parse the xml documents to create the state set with
             * transitions between them.
             */
            final Map<String, IFlow> states = createNodes(doc, fDiag);
        } catch (InvalidDataFlowDiagramException ex) {
            Logger.getLogger(XMLDataFlowDiagram.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Find the number of occurences of a character in a given string.
     * @param tocheck The String to evaluate.
     * @param cInst The character (substring) to check for.
     * @return The number of occurences.
     */
    public static int charOccurences(final String tocheck, final String cInst) {
        return tocheck.length() - tocheck.replace(cInst, "").length();
    }
}
