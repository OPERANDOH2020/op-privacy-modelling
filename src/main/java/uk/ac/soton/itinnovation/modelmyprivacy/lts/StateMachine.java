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
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLDocument;
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLStateMachine;


/**
 * The java data representation of a set of states that form a state machine.
 *
 * The state machine can be used for two purposes:
 * 1) It can be analysed for privacy conflicts.
 * 2) It can executed against received events.
 */
public class StateMachine implements EventCapture {

    //////////////////////////////////////////////////
    // CORE LTS elements
    //////////////////////////////////////////////////
    /**
     * Each state machine is a set of states. We use a hash map to
     * stores states and transitions. Label - toState. Query toState. Set
     * currentState to this state. etc...
     */
    private transient Map<String, State> states;

    /**
     * The set of roles (who can perform privacy actions).
     */
    private List<Role> ltsRoles;

    /**
     * The pieces of data privacy actions are performed on.
     */
    private List<Field> ltsData;

    /**
     * The access control policies in place. This is
     * an object storing the access policies with operations
     * available to query the policies.
     */
    private AccessPolicies ltsPolicies;

    /**
     * Each state machine has only one start state.
     */
    private transient State firstState;

    //////////////////////////////////////////////////
    // FIELDS for Executable LTS purposes
    //////////////////////////////////////////////////
    /**
     * Each state machine can have only one current state during execution.
     */
    private transient State currentState;

    /**
     * Synchronised blocking queue for execution input. That is,
     * the only input to this machine comes via this queue.
     */
    private final transient BlockingQueue<PrivacyEvent> eventQueue;

    /**
     * Construct a new state machine.
     */
    public StateMachine() {
        this.eventQueue = new ArrayBlockingQueue(50);
    }

    /**
     * Get an unordered list of states
     * @return
     */
    public List<State> getStates() {
        List<State> unorderedList = new ArrayList<>();
        unorderedList.addAll(this.states.values());
        return unorderedList;
    }

    public Map<String, State> getSMStates() {
        return this.states;
    }

    /**
     * Add the access policies to the LTS model
     * @param xmlModel The xml model as a string
     */
    public void addAccessPolicies(String xmlModel){
        try {
            this.ltsPolicies = new AccessPolicies();
            this.ltsPolicies.loadAccessPolicy(xmlModel);
        } catch (IOException ex) {
            Logger.getLogger(StateMachine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add the service roles from the XML data flow model
     * @param xmlModel The XML data flow model description as a string.
     */
    public void addRoles(String xmlModel){
        try {
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            this.ltsRoles = XMLStateMachine.addRoles(pattern.getRootElement());
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
        }
    }

    public List<Role> getRoles(){
        return this.ltsRoles;
    }

    /**
     * Add the data fields from the XML data flow model
     * @param xmlModel The XML data flow model description.
     */
    public void addData(String xmlModel){
        try {
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            this.ltsData = XMLStateMachine.addRecords(pattern.getRootElement());
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
        }
    }

    public List<Field> getData(){
        return this.ltsData;
    }

    public AccessPolicies getAccessPolicies(){
        return this.ltsPolicies;
    }

    /**
     * Create the set of states and transitions that model the LTS.
     * @param xmlModel The XML data flow model  description.
     */
    public void buildDataFlowLTS(String xmlModel){
        try {
            // Read the pattern from the xml string input parameter
            final Document pattern = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(xmlModel.getBytes(StandardCharsets.UTF_8)));
            XMLStateMachine.createStateMachine(pattern.getRootElement().getChild(XMLStateMachine.LTS_LABEL), this);
            addData(xmlModel);
            addRoles(xmlModel);
        } catch (InvalidStateMachineException ex) {
            LTSLogger.LOG.error(ex.getLocalizedMessage());
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


    /**
     * Start the trace and begin outputting the event tests that correspond
     * to the state machine checks.
     * @return The string version of the output report.
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException
     */
    public final void start() throws InvalidStateMachineException {
	currentState = this.firstState;
        if (currentState == null) {
            throw new InvalidStateMachineException("No start state");
        }
        System.out.println("Start state = " + currentState.getLabel());
        while (!currentState.isEndNode()) {
            try {
                PrivacyEvent obsEvent = this.eventQueue.take();
                System.out.println("Observed event: " + obsEvent);
                currentState = getState(currentState.evaluateTransition(obsEvent));
                System.out.println("Moving to state = " + currentState.getLabel());
                if (currentState == null) {
                    throw new InvalidStateMachineException("No transition possible - error in exection of data flow");
                }
                } catch (UnexpectedEventException ex) {
               throw new InvalidStateMachineException("Unknown event observed - does not match the data flow model");
            } catch (InterruptedException ex) {
                throw new InvalidStateMachineException("Event timed out - no event received.");
            }
        }
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

    /**
     * Output a dataflow graph.
     */
    public final void visualiseDataFlowGraph() {
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
                n.addAttribute("ui.label", t.getLabel().getAction());
            }

        }

        graph.display();
    }

    /**
     * Output a dataflow graph.
     */
    public final void visualiseAutomatedGraph(boolean labels) {
        Graph graph = new SingleGraph("Privacy Modeller");
        String styleSheet ="edge {arrow-shape: arrow; arrow-size: 10px, 5px;}";
        graph.addAttribute("ui.stylesheet",styleSheet);

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            Node n = graph.addNode(entry.getValue().getLabel());
            // Generate the label for the fields
            String label = ((StateNode) entry.getValue()).tableToString();
            if(labels)
                n.addAttribute("ui.label", ((StateNode) entry.getValue()).getAutoLabel());
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            List<Transition> transitions = entry.getValue().getTransitions();
            for(Transition t: transitions) {
                AbstractEdge n = graph.addEdge(entry.getValue().getLabel() + "->" + t.readToLabel(), entry.getValue().getLabel(), t.readToLabel(), true);
                if(labels)
                    n.addAttribute("ui.label", "|" + t.getLabel().getRole()+ "|" + t.getLabel().getAction() +"|" + t.getLabel().getData()+" for " + t.getLabel().getPurpose()+"|");
            }

        }

        graph.display();
    }

    /**
     * Output a dataflow graph.
     */
    public final void visualiseAnnotatedGraph() {
        Graph graph = new SingleGraph("Privacy Modeller");
        String styleSheet ="edge {arrow-shape: arrow; arrow-size: 10px, 5px;}";
        graph.addAttribute("ui.stylesheet",styleSheet);

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            Node n = graph.addNode(entry.getValue().getLabel());
            // Generate the label for the fields
            String label = ((StateNode) entry.getValue()).tableToString();
            System.out.println(label);
            n.addAttribute("ui.label", ((StateNode) entry.getValue()).getAutoLabel());
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

        for (Map.Entry<String, State> entry : this.states.entrySet())
        {
            List<Transition> transitions = entry.getValue().getTransitions();
            for(Transition t: transitions) {
                AbstractEdge n = graph.addEdge(entry.getValue().getLabel() + "->" + t.readToLabel(), entry.getValue().getLabel(), t.readToLabel(), true);
                n.addAttribute("ui.label", "|" + t.getLabel().getAttrribute("privacy") +"|");
            }
        }

        graph.display();
    }

    /**
     * Perform a pre order traversal to read all of the transitions into the
     * tSet paramater.
     *
     * @param node The start node of the data flow model
     * @param dataFlowModel The state machine to retrieve transitions for.
     * @param tSet array to fill with
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException
     */
    public static void getTransitions(State node, StateMachine dataFlowModel, List<Transition> tSet) throws InvalidStateMachineException {
        for (Transition read : node.getTransitions()) {
            tSet.add(read);
            getTransitions(dataFlowModel.getState(read.readToLabel()), dataFlowModel, tSet);
        }
    }
}