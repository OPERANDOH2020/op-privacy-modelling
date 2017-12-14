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

import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.PrivacyEvent;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.UnexpectedEventException;

import java.util.ArrayList;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.TransitionLabel;
import uk.ac.soton.itinnovation.modelmyprivacy.utils.TableGenerator;

/**
 * A State in the LTS.
 *
 * A state node is a representation of a user's privacy. It is a representation
 * of who has seen personal information.
 */
public class StateNode implements State {

    /**
     * State node label. Matches the label in the state machine diagram of the
     * circle node.
     */
    private final transient String name;

    /**
     * The values describing the state type, start, trigger, etc.
     * Can be start and trigger, or start and normal.
     */
    private final transient StateType stateType;

    /**
     * Each node has a set of one or more nodes that it points to. An end
     * node is the exception and the end node must have zero entries.
     */
    private final transient List<Transition> outStates;

    /**
     * Each node has a set of incoming transitions. A start node is an exception
     *
     */
    private final transient List<Transition> inStates;

    /**
     * The state machine that this state node belongs to.
     */
    private transient StateMachine stateMachine;

    /**
     * The table of variables.
     */
    Table<String, String, StateVariable> table = HashBasedTable.create();

    /**
     * Trace event memory. The following field is only used for trace
     * analysis.
     */
    private PrivacyEvent traceToken;

    /**
     *
     * @param nodeName This is the string identifier labelling the state. It must
     * be at least 2 characters long.
     * @param type must be one of the stateType enumeration. Null values are
     * not allowed.
     * @param inputSM The StateMachine context
     * @throws InvalidStateMachineException error initialising node
     */
    public StateNode(final String nodeName, final StateType type, final StateMachine inputSM)
        throws InvalidStateMachineException {

        if ((nodeName == null) || (type == null)) {
                throw new InvalidStateMachineException("State " + nodeName + " contains"
                        + "null values for input");
        }
        if (nodeName.length() < 2) {
            throw new InvalidStateMachineException("Invalid label - must be"
                    + "at least 2 characters long");
        }
        // Initialise the object values
	this.name = nodeName;
        if (inputSM != null) {
            this.stateMachine = inputSM;
        }

        // Initialise the state transitions information
        this.inStates = new ArrayList();
        this.outStates = new ArrayList();

        this.stateType = type;
    }

    public void initialiseStateVariables(List<Role> roles, List<Field> fields) {
        for(Role r: roles) {
            String rID = r.getRoleIdentity();
            for(Field f: fields) {
                String fID = f.getName();
                StateVariable sV = new StateVariable(rID, fID);
                table.put(rID, fID, sV);
            }
        }
    }

    public void overwriteStateVariables(List<Role> roles, List<Field> fields, Table<String, String, StateVariable> filledTable ){
        for(Role r: roles) {
            String rID = r.getRoleIdentity();
            for(Field f: fields) {
                String fID = f.getName();
                StateVariable newVal = new StateVariable(rID, fID);
                newVal.setCanRead(filledTable.get(rID, fID).getCanRead());
                newVal.setSeen(filledTable.get(rID, fID).getSeen());
                table.put(rID, fID, newVal);
            }
        }
    }

    public Table<String, String, StateVariable> getStateVariables() {
        return this.table;
    }

    public void changeStateVariable(String role, String field, boolean value) {
        StateVariable get = table.get(role, field);
        get.setCanRead(value);
    }

    public void changeSeenStateVariable(String role, String field, boolean value) {
        StateVariable get = table.get(role, field);
        get.setSeen(value);
    }

    /**
     * Read the state label. Within a state machine, labels are
     * unique i.e. no two states can have the same label.
     * @return A string with the state label.
     */
    public final StateMachine getStateMachine() {
	return this.stateMachine;
    }

    /**
     * Read the state label. Within a state machine, labels are
     * unique i.e. no two states can have the same label.
     * @return A string with the state label.
     */
    @Override
    public final String getLabel() {
	return this.name;
    }

    /**
     * Return true if this is an end node state; otherwise return false.
     * @return boolean value indicating end node status
     */
    @Override
    public final boolean isEndNode() {
        return stateType.equals(StateType.END);
    }

    /**
     * Return true if this is a start node state; otherwise return false.
     * @return boolean value indicating start node status
     */
    @Override
    public final boolean isStartNode() {
	return stateType.equals(StateType.START);
    }

     /**
     * Adds a transition between two states in the state machine. If both states
     * do not exist then an InvalidTransitionException is thrown.
     * @param trans the transition of guards that must evaluate to true
     * if the transition is to be taken.
     * @throws InvalidTransitionException Adding invalid transition.
     */
    @Override
    public final void addToTransition(final Transition trans, Map<String,
             State> states)
            throws InvalidTransitionException {
	this.outStates.add(trans);

        // Find the from state label and call to add from states.
        State fromState = states.get(trans.readFromLabel());
        fromState.addFromTransition(trans);
    }

    @Override
    public final void addFromTransition(final Transition trans)
            throws InvalidTransitionException {
	this.inStates.add(trans);
    }

    /**
     * List the set of transitions possible from this state.
     * @return The list of transitions
     */
    @Override
    public final List<Transition> getTransitions() {
        return this.outStates;
    };


    /**
     * Evaluate a new event (a rest operation) against the set of transitions
     * at this state. If there is a complete match then the next state to
     * transition to is returned. If not - we have an error event (i.e.
     * an interoperability error) and hence we throw an unexpected event error.
     *
     * @param input The details of the occured event - a rest operations with
     * data and parameters to compare against the condition.
     * @return the state to move to based upon the event
     * @throws UnexpectedEventException event error - no transition matches the event.
     */
    @Override
    public final String evaluateTransition(final PrivacyEvent input)
            throws UnexpectedEventException {
        // Find transitions with matching resource locations

        this.traceToken = input;

        /**
         * Iterate through each potential event transition to find a matching
         * next state. If no matches then we have an interoperability fail.
         * Report in the exception.
         */
        final Iterator<Transition> transIt = this.outStates.iterator();
        while (transIt.hasNext()) {
            try {
                final Transition evTrans = transIt.next();
                TransitionLabel g = evTrans.getLabel();
                // Match action
                if(g.evaluate(input.getActionField(), input.getRoleField(), input.getDataField())){
                    return evTrans.readToLabel();
                }
            } catch (InvalidRoleException ex) {
                Logger.getLogger(StateNode.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        throw new UnexpectedEventException("Fail: no transition possible");
    };


    /**
     * Evaluate the guards on a transition of this state.
     * @param checks The list of guards to evaluate with.
     * @param conditions The list of conditions that occurred from the event.
     * @param report The output stream to output the data.
     * @return True if all guards evaluate against the conditions.
     */
    private boolean evaluateLabel(final List<TransitionLabel> checks) {

        final Iterator<TransitionLabel> itCheck = checks.iterator();
        while (itCheck.hasNext()) {

        }
        return true;
    }

    @Override
    public final PrivacyEvent getStoredEvent() {
        return this.traceToken;
    }

    public String getAutoLabel() {
        return this.table.toString();
    }

    public String tableToString() {
        TableGenerator tGN = new TableGenerator();
        List<String> columnKeySet = new ArrayList<String>();
        columnKeySet.add("Role");
        // Get the field names
        for(String roleName: this.table.rowKeySet()){
            columnKeySet.add(roleName);
        }
        List<List<String>> tableLists = new ArrayList<List<String>>();
        // for each role create a list of strings = the row
        for(String fieldName: this.table.columnKeySet()){
            List<String> rowValues = new ArrayList<>();
            rowValues.add(fieldName);
            for(String roleName: this.table.rowKeySet()){
                rowValues.add(this.table.get(roleName, fieldName).toString());
            }
            tableLists.add(rowValues);
        }

        return tGN.generateTable(columnKeySet, tableLists, 1);
    }

}