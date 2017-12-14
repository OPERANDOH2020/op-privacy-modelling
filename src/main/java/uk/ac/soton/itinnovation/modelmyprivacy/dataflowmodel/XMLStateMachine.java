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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Field;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidRoleException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateTypeException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidTransitionException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Role;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.State;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateNode;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Transition;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.Guard;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.InvalidGuard;


/**
 * XML operations specific to the creation of a state machine in memory
 * from an xml document describing the states see the schema: statemachine.xsd.
 *
 * @author pjg
 */
public final class XMLStateMachine {

    /**
     * XML state tag label constant.
     */
    public static final String STATE_LABEL = "state";

    /**
     * XML transition tag label constant.
     */
    public static final String TRANSITION_LABEL = "transition";

    /**
     * XML label tag label constant.
     */
    public static final String LABEL_LABEL = "label";

    /**
     * XML type tag label constant.
     */
    public static final String STATE_TYPE = "type";

    /**
     * XML start type content label constant.
     */
    public static final String START_LABEL = "start";

    /**
     * XML END_LABEL type content label constant.
     */
    public static final String END_LABEL = "end";

    /**
     * XML NORMAL_LABEL type content label constant.
     */
    public static final String NORMAL_LABEL = "normal";

    /**
     * XML TO_LABEL type content label constant.
     */
    public static final String TO_LABEL = "to";

    /**
     * Label in XML specification of the state machine architecture
     * - model tag <model>
     */
    public static final String LTS_LABEL = "lts";

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
    private XMLStateMachine() {
        // no implementation.
    }

    /**
     * Each state in the state machine is one of three types (start, end,
     * normal) - this matches to three strings in the xml schema. Here
     * we generate the java state type from this string
     *
     * @param textInput The string input from the xml content
     * @return A matching state type to the string. Note an invalid string
     * generates and exception.
     *
     * @see State.stateType
     * @throws InvalidStateTypeException Error in given state type.
     */
     private static State.StateType getStateType(final String textInput)
            throws InvalidStateTypeException {
         // Make sure function operates independent of case
         switch (textInput.toLowerCase(Locale.ENGLISH)) {
             case START_LABEL: return State.StateType.START;
             case END_LABEL: return State.StateType.END;
             case NORMAL_LABEL: return State.StateType.NORMAL;
             default: // Input hasn't matched so we must through an invalid input exception
                throw new InvalidStateTypeException(textInput
                    + "must be either: {start, end, normal, trigger, triggerstart}");
         }

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
     private static Guard getGuard(final Element transition)
        throws InvalidTransitionException {

         final String role = transition.getChildText("role");
         final String action = transition.getChildText("action");
         final String data = transition.getChildText("data");
         final String purpose = transition.getChildText("purpose");
        try {
            Guard gg = new Guard(role, action, data, purpose);
            return  gg;
        } catch (InvalidGuard ex) {
            Logger.getLogger(XMLStateMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
         return null;
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
     private static void addTransitions(final Element elState, final Map<String,
             State> states) throws InvalidTransitionException {

         final State fromState = states.get(elState.getChildText(LABEL_LABEL));
         if (fromState.isEndNode()) {
             return;
         }

         final List<Element> tOut = elState.getChildren(TRANSITION_LABEL);
         for (Element eltIndex : tOut) {
             final String toLabel = eltIndex.getChildText(TO_LABEL);
             if (states.get(elState.getChildText(LABEL_LABEL)) == null) {
                     throw new InvalidTransitionException("To state does not exist in state machine");
             }
             try {

                fromState.addToTransition(new Transition(toLabel, fromState.getLabel(), getGuard(eltIndex)), states);
             } catch (InvalidTransitionException ex) {
                 throw new InvalidTransitionException("Invalid transition specification", ex);
             }
         }
     }

     /**
      * Based on the XML states list - generate the set of states.
      * This method also calls functions to build the transition map.
      *
      * @param doc The XML document with a fully formed state machine as
      * input. An invalid state machine input will generate an exception.
      * @param arch The overall architecture context of the operation.
      * @return The set of created states from the xml spec.
      * @throws InvalidStateMachineException Error caused by invalid behaviour specification.
      */
     private static Map<String, State> createStates(final Element doc, final StateMachine sm)
             throws InvalidStateMachineException {

        final Map<String, State> states = new HashMap();
        try {

            final XPath xpa = XPath.newInstance("//" + STATE_LABEL);

            final List<Element> xmlStates = xpa.selectNodes(doc);
            for (Element eltIndex : xmlStates) {
                // Get the state label
                final String label = eltIndex.getChildText(LABEL_LABEL);
                final State.StateType type = getStateType(eltIndex.getChildText(STATE_TYPE));
                states.put(label, new StateNode(label, type, sm));
            }
            for (Element eltIndex2 : xmlStates) {
                addTransitions(eltIndex2, states);
            }
        } catch (JDOMException ex) {
            throw new InvalidStateMachineException("Invalid XML input", ex);
        } catch (InvalidStateTypeException ex) {
            throw new InvalidStateMachineException("Invalid state type input", ex);
        } catch (InvalidTransitionException ex) {
            throw new InvalidStateMachineException("Invalid transition specification", ex);
        }
        return states;
     }

     /**
      * Extract the Roles from the XML specification.
      *
      * @param doc The XML document with a fully formed state machine as
      * input. An invalid state machine input will generate an exception.
      * @return The set of created roles from the xml spec.
      * @throws InvalidStateMachineException Error caused by invalid behaviour specification.
      */
     public static List<Role> addRoles(final Element doc)
             throws InvalidStateMachineException {

        List<Role> roles = new ArrayList<>();
        try {
            final XPath xpaRolesRoot = XPath.newInstance("roles/role");
            final List<Element> xmlRoles = xpaRolesRoot.selectNodes(doc);
            for (Element roleIndex : xmlRoles) {
                final String roleID = roleIndex.getChildText("name");
                final String categoryLabel = roleIndex.getChildText("category");
                final String reference = roleIndex.getChildText("reference");
                if  (reference != null) {
                    Role role = new Role(roleID, categoryLabel, reference);
                    roles.add(role);
                }
                else {
                    Role role = new Role(roleID, categoryLabel);
                    roles.add(role);
                }
            }
        } catch (JDOMException ex) {
            throw new InvalidStateMachineException("Invalid XML input - check XML syntax", ex);
        } catch (InvalidRoleException ex) {
            throw new InvalidStateMachineException("Invalid XML input - check role specification", ex);
        }
        return roles;
     }


     private static List<Field> readFields(Element fieldXML) {
         List<Field> fields = new ArrayList<>();
          List<Element> catFields = fieldXML.getChildren();
          for (Element e : catFields) {
                fields.add(readField(e));
          }
         return fields;
     }

     /**
      * Read a field from a field list and put it into a Field object
      * @param fieldXML The XML element reference.
      * @param recField If this is a embedded record in the field
      * @return The created field.
      */
    private static Field readField(Element fieldXML) {
        Field newField = new Field(fieldXML.getChildText("name"));

        List<Element> catFields = fieldXML.getChild("categories").getChildren();
        for (Element catIndex : catFields) {
            newField.addCategory(catIndex.getText());
        }

        String type = fieldXML.getChildText("type");
        if (type!= null) {
                switch (type) {
                    case "sensitive":
                        newField.setSensitive(true);
                        break;
                    case "qi":
                        newField.setQI(true);
                        break;
                    case "ei":
                        newField.setEI(true);
                        break;
                }
        }

        if(fieldXML.getChild("fields") != null) {
            List<Field> fields = readFields(fieldXML.getChild("fields"));
            newField.setRecordField(fields);
        }

        return newField;
     }

     /**
      * Extract the Records from the XML specification.
      *
      * @param doc The XML document with a fully formed state machine as
      * input. An invalid state machine input will generate an exception.
      * @return The set of created records from the xml spec.
      * @throws InvalidStateMachineException Error caused by invalid behaviour specification.
      */
     public static List<Field> addRecords(final Element doc)
             throws InvalidStateMachineException {

        List<Field> records = new ArrayList<>();
        try {
            final XPath xpaRolesRoot = XPath.newInstance("records");
            final List<Element> xmlRecords = xpaRolesRoot.selectNodes(doc);
            /**
             * For each record:
             */
            for (Element recordIndex : xmlRecords) {
                final String recordID = recordIndex.getChildText("name");
                List<Element> fields = recordIndex.getChild("fields").getChildren();
                Field record = new Field(recordID);
                List<Field> fieldElements = new ArrayList<>();
                /**
                 * For each field in the record:
                 */
                for (Element fieldIndex : fields) {
                    fieldElements.add(readField(fieldIndex));
                }
                record.setRecordField(fieldElements);
                records.add(record);
            }
        } catch (JDOMException ex) {
            throw new InvalidStateMachineException("Invalid XML input - check XML syntax", ex);
        }
        return records;
     }

     /**
      * Search through the state diagram for the first state. There must be
      * one and only one first state.
      *
      * @param states The set of state machine states to search
      * @return The state label of the first state. Null if there is no first
      * state
      */
     private static String getFirstState(final Map<String, State> states) {
         final Iterator<State> statesMap = states.values().iterator();
         while (statesMap.hasNext()) {
             final State next = statesMap.next();
             if (next.isStartNode()) {
                 return next.getLabel();
             }
         }
         return null;
     }

     /**
      * Create a state machine index.e. the in memory state machine graph from
      * a given XML document instance. This state machine can be executed
      * to test the interoperability of an operating REST composition
      * configuration.
      *
      * @param doc The XML instance of the state machine domain language to
      * be turned into an in-memory state machine
     * @param sMach The State Machine to fill with content.
     * @throws InvalidStateMachineException error during the parsing of the state
     * machine from the XML.
     */
    public static void createStateMachine(final Element doc, final StateMachine sMach ) throws InvalidStateMachineException {

         /**
          * First parse the xml documents to create the state set with
          * transitions between them.
          */
         final Map<String, State> states = createStates(doc, sMach);
         final String firstLabel = getFirstState(states);
         if (firstLabel == null) {
             throw new InvalidStateMachineException("State machine: <behaviour> "
                     + "description does not contain a start node");
         }
         /**
          * Build the StateMachine type with the identified first state and
          * state set.
          */
         sMach.inputContent(firstLabel, states);
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
