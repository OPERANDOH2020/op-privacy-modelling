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


package uk.ac.soton.itinnovation.modelmyprivacy.lts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.PrivacyEvent;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.UnexpectedEventException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.SingleGraph;
import org.jdom.Document;
import uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data.AccessPolicies;
import uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data.XMLDocument;
import uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data.XMLStateMachine;


/**
 * The java data representation of a set of states that form a state machine.
 * This executable in response to discrete events.
 *
 */
public class StateMachine implements EventCapture {
    /**
     * References to two states that we need to operate the state
     * machine: the first state, and the current state.
     */

    /**
     * Each state machine can have only one current state.
     */
    private transient State currentState;

    /**
     * Each state machine has only one start state.
     */
    private transient State firstState;

    /**
     * Synchronised blocking queue for execution input. That is,
     * the only input to this machine comes via this queue. The RESTLET
     * framework captures REST operations and pushes them as events to
     * this queue.
     */
    private final transient BlockingQueue<PrivacyEvent> eventQueue;

    /**
     * Each state machine is a set of states. We use a hash map to
     * perform transitions. Label - toState. Query toState. Set
     * currentState to this state. etc...
     */
    private transient Map<String, State> states;

    private List<Role> stateRoles;

    private List<Field> stateRecords;

    private AccessPolicies policies;

    /**
     * Construct a new state machine.
     */
    public StateMachine() {
        this.eventQueue = new ArrayBlockingQueue(50);
    }

    public void addAccessPolicies(String xmlModel){
        try {
            policies = new AccessPolicies();
            policies.loadAccessPolicy(xmlModel);
        } catch (IOException ex) {
            Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add the service roles
      * @param xmlModel The XML statemachine description.
     */
    public void addRoles(String xmlModel){
        try {
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            stateRoles = XMLStateMachine.addRoles(pattern.getRootElement());
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
        }
    }

    /**
     * Add the service roles
      * @param xmlModel The XML statemachine description.
     */
    public void addRecords(String xmlModel){
        try {
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            stateRecords = XMLStateMachine.addRecords(pattern.getRootElement());
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
        }
    }


    /**
     * Create the set of states and transitions.
     * @param xmlModel The XML statemachine description.
     */
    public void buildStates(String xmlModel){
        try {
            // Read the pattern from the xml string input parameter
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            XMLStateMachine.createStateMachine(pattern.getRootElement().getChild(XMLStateMachine.LTS_LABEL), this);
            addRecords(xmlModel);
            addRoles(xmlModel);
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
        }
    }

    /**
     * Currently only works on depth 1 records.
     * @param t
     * @param states
     */
    private State calculateCreate(Transition t, Map<String, State> states){
        try {
            /**
             * Get the data record to work with
             */
            String data = t.getGuards().getData();
            List<Field> fieldRules = new ArrayList<>();
            for(Field f: this.stateRecords) {
                if(f.getName().equalsIgnoreCase(data)){
                    if(f.getRecord()){
                        for(Field f1: f.getRecordField()){
                            fieldRules.add(f1);
                        }
                    }
                    else {
                        fieldRules.add(f);
                    }
                }
            }

            StateNode s = new StateNode("", StateNode.StateType.NORMAL, this);
            s.initialiseStateVariables(stateRoles, fieldRules);
            /**
             * calculate the can fields for the state variables
             */
            for(Field f: fieldRules){
                for(Role r: this.stateRoles){
                    boolean can = policies.canAccess(f.getName(), r.getRoleIdentity());
                    s.changeStateVariable(f.getName(), r.getRoleIdentity(), can);
                }
            }
            return s;
        } catch (InvalidStateMachineException ex) {
            Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void generatePolicyTransition(State node, Map<String, State> states) {
        /**
         * For each can state variable - create a node and transition
         */
        StateNode s = new StateNode("", StateNode.StateType.NORMAL, this);
            s.initialiseStateVariables(stateRoles, fieldRules);
            /**
             * calculate the can fields for the state variables
             */
            for(Field f: fieldRules){
                for(Role r: this.stateRoles){
                    boolean can = policies.canAccess(f.getName(), r.getRoleIdentity());
                    s.changeStateVariable(f.getName(), r.getRoleIdentity(), can);
                }
            }

    }

    /**
     *
     * @param node
     * @param states
     */
    private void preorderTraversal(State node, Map<String, State> states, State currentPos) {

        Iterator iter = node.getTransitions().iterator();
        while (iter.hasNext()) {
            try {
                Transition toProduce = (Transition) iter.next();
                /**
                 * Based on the transition generate the automated states.
                 */
                String action = toProduce.getGuards().getAction();
                switch (action) {
                    case "create":
                        State s = calculateCreate(toProduce, states);
                        Transition t = new Transition(s.getLabel(), currentPos.getLabel());
                        currentPos.addToTransition(t, states);
                        currentPos = s;
                }
                preorderTraversal(getState(toProduce.readToLabel()), states, currentPos);
            } catch (InvalidTransitionException ex) {
                Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Create the set of states and transitions.
     * @param dataflowStartState
     * @param xmlModel The XML statemachine description.
     */
    public Map<String, State> generateStates(State dataflowStartState){

        try {
            // The first state the user has no data - it is an empty state variables
            State startNode = new StateNode("private", State.StateType.START, this);
            Map<String, State> states = new HashMap<>();
            states.put("private", startNode);
            State currentNode = dataflowStartState;
            preorderTraversal(currentNode, states, startNode);
            return states;
        } catch (InvalidStateMachineException ex) {
            Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    /**
     * Construct a new state machine with a first state and the
     * remaining set of states.
     *
     * @param firstSt The label of the first state.
     * @param stateSet The set of states to create a machine from.
     */
    public final void inputContent(final String firstSt, final Map<String, State> stateSet) {
        this.states = new HashMap();
        for (State s : stateSet.values()) {
            addState(s);
        }
        this.firstState = getState(firstSt);
    }


    /**
     * Assign a state as the current executable position in the state machine.
     * @param state The current position state.
     */
    public final void setCurrentState(final State state) {
        this.currentState = state;
    }

    /**
     * Return the current executable position in the state machine.
     * @return state The current position state.
     */
    public final State getCurrentState() {
        return this.currentState;
    }

    /**
     * Get the start state. Used for beginning the execution phase.
     * @return The fixed start state reference.
     */
    public final State getStartState() {
        return firstState;
    }

    /**
     * Add a new state to the state machine.
     * @param newState The state object to add.
     */
    public final void addState(final State newState) {
	this.states.put(newState.getLabel().toLowerCase(), newState);
    }

    /**
     * Event interface implemenation. When a discrete event is detected it is
     * sent here to move the state machine execution.
     * @param restEvent The new event detected in the distributed system e.g. a
     * rest message.
     */
    @Override
    public final void pushEvent(final PrivacyEvent restEvent) {
        try {
            this.eventQueue.put(restEvent);
        } catch (InterruptedException ex) {
        }
    }
//
    /**
     * Report an exception. Potentially this method could be added into
     * the execution phase, so the state machine can deal with exceptions.
     * @param excep The exception observed.
     */
    @Override
    public final void logException(final Exception excep) {
//        outputReport.println("Interoperability Error: " + excep.getMessage());
    }

    /**
     * Start the trace and begin outputting the event tests that correspond
     * to the state machine checks.
     * @return The string version of the output report.
     */
    public final String start()	{
	currentState = this.firstState;
        if (currentState == null) {
//            outputReport.println("Invalid pattern -> no valid start state");
//            return outputReport.outputReport();
        }

//        outputReport.println("Test started - run the application");
//        outputReport.println("----------------------------------");
//        outputReport.println("Starting trace at Node:" + currentState.getLabel());

        while (!currentState.isEndNode()) {
            try {
                currentState = getState(currentState.evaluateTransition(this.eventQueue.take()));
                if (currentState == null) {
                        //return outputReport.outputReport();
                }
                //outputReport.println("Transition Success - move to state:" + currentState.getLabel());

            } catch (UnexpectedEventException ex) {
               logException(ex);
               //return outputReport.outputReport();
            } catch (InterruptedException ex) {

            }
        }
        //outputReport.println("End node reached --> Application interoperates correctly");
        return "";//outputReport.outputReport();
    }

    /**
     * Given the label id, retrieve a state from the state machine.
     * @param label The id of the state - the label.
     * @return The state object.
     */
    public final State getState(final String label) {
        return this.states.get(label.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Create a string representation of the state machine object.
     * @return The string description.
     */
    @Override
    public final String toString() {
	String sep = "";
	final StringBuilder sBuilder = new StringBuilder();
	for (String key : states.keySet()) {
	    sBuilder.append(sep)
		.append(states.get(key).toString());
	    sep = "\n";
	}
	return sBuilder.toString();
    }

    public final void visualiseGraph() {
        Graph graph = new SingleGraph("Privacy Modeller");
        String styleSheet ="edge {arrow-shape: arrow; arrow-size: 10px, 5px;}";
        graph.addAttribute("ui.stylesheet",styleSheet);

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            Node n = graph.addNode(entry.getValue().getLabel());
            n.addAttribute("ui.label", entry.getValue().getLabel());
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            List<Transition> transitions = entry.getValue().getTransitions();
            for(Transition t: transitions) {
                AbstractEdge n = graph.addEdge(entry.getValue().getLabel() + "->" + t.readToLabel(), entry.getValue().getLabel(), t.readToLabel(), true);
                n.addAttribute("ui.label", t.listGuards().get(0).getAction());
            }

        }

        graph.display();
    }
}