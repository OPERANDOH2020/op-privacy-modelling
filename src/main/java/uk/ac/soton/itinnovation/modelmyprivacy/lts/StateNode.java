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


import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.Guard;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.PrivacyEvent;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.UnexpectedEventException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * State in a state machine.
 *
 * @author pjg
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
    private final transient List<Transition> nextStates;

    /**
     * After a transition has occurred, the event that this state receives
     * will be saved for future reference.
     */
    private transient PrivacyEvent savedEvent;

    /**
     * The state machine that this state node belongs to.
     */
    private transient StateMachine stateMachine;

    /**
     * Counter. If this is a loop node
     */
    private int counter = 0;
    /**
     * Constant to the content label of a rest event.
     */
    private static final String CONTENTLABEL = "content";

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
        this.nextStates = new ArrayList();
        this.stateType = type;
    }

    /**
     * Update this state's counter value.
     * @param change The value to update the counter by e.g. increment = 1, dec
     * = -1.
     */
    @Override
    public void counter(int change){
        this.counter = this.counter + change;
    }

    @Override
    public int getCounter() {
        return this.counter;
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
     * Adds a transition between two states in the state machine. If
 both states do not exist then an InvalidTransitionException is thrown.
     * @param trans the transition of guards that must evaluate to true
     * if the transition is to be taken.
     * @throws InvalidTransitionException Adding invalid transition.
     */
    @Override
    public final void addTransition(final Transition trans)
            throws InvalidTransitionException {
	this.nextStates.add(trans);
    }


    /**
     * List the set of transitions possible from this state.
     * @return The list of transitions
     */
    @Override
    public final List<Transition> getTransitions() {
        return this.nextStates;
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

        this.savedEvent = input;

        /**
         * Iterate through each potential event transition to find a matching
         * next state. If no matches then we have an interoperability fail.
         * Report in the exception.
         */
        final Iterator<Transition> transIt = this.nextStates.iterator();
        while (transIt.hasNext()) {
            final Transition evTrans = transIt.next();
            if (!evTrans.listGuards().isEmpty()) {
                try {
                    for(Guard g: evTrans.listGuards()){
                        // Match action
                        if(g.evaluate(input.getActionField(), input.getRoleField(), input.getDataField())){
                            return evTrans.readLabel();
                        }
                    }
                } catch (InvalidInputException ex) {
                    ex.printStackTrace();
                }

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
    private boolean evaluateGuards(final List<Guard> checks) {

        final Iterator<Guard> itCheck = checks.iterator();
        while (itCheck.hasNext()) {

        }
        return true;
    }

    @Override
    public final PrivacyEvent getStoredEvent() {
        return this.savedEvent;
    }

}