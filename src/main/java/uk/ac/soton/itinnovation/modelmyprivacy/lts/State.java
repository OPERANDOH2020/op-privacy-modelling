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

import java.util.List;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.PrivacyEvent;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.UnexpectedEventException;


/**
 * A State in the Labelled Transition System represents the Privacy
 * of a user.
 *
 * @author pjg@it-innovation.soton.ac.uk
 */
public interface State {

    /**
     * Enumeration defined for the three different states.
     */
    public enum StateType {
        /**
         * A start node of a state machine that waits on rest inputs.
         */
        START,
        /**
         * And end node of the state machine where termination occurs.
         */
        END,
        /**
         * A state to receive REST inputs.
         */
        NORMAL,

    }

    /**
     * Return true if this is an end node state; otherwise return false.
     * @return boolean value indicating end node status.
     */
    boolean isEndNode();

    /**
     * Update the counter state value.
     * @param change The value to change e.g. 1 to add, -1 to subtract
     */
    void counter(int change);

    /**
     * Get the counter state value.
     * @return The counter value
     */
    int getCounter();

    /**
     * Return true if this is a start node state; otherwise return false.
     * @return boolean value indicating start node status.
     */
    boolean isStartNode();

    /**
     * Read the state label. Within a state machine, labels are
     * unique i.e. no two states can have the same label.
     * @return A string with the state label.
     */
    String getLabel();

    /**
     * Read the saved event. This is only available from none trigger nodes
     * and only after the state has been transitioned through.
     * @return The saved .
     */
    PrivacyEvent getStoredEvent();

    /**
     * Adds a guard transition between two states in the state machine. If both
     * states do not exist then an InvalidTransitionException is thrown.
     * @param newTransition The transition data to add. This is a list of guards
     * in the xml <guards> tag.
     * @throws InvalidTransitionException Error in the transition specification input.
     */
     void addTransition(Transition newTransition)
            throws InvalidTransitionException;

    /**
     * List the set of transitions possible from this state.
     * @return The list of transitions
     */
    List<Transition> getTransitions();

    /**
     * Evaluate a new event (a rest operation) against the set of transitions
     * at this state. If there is a complete match then the next state to
     * transition to is returned. If not - we have an error event (i.e.
     * an interoperability error) and hence we throw an unexpected event error.
     *
     * @param input The details of the occured event - a rest operations with
     * data and parameters to compare against the condition.
     * @param out As the transition is evaluated it reports actions to the
     * interoperability report passed here
     *
     * @return the state to move to based upon the event
     * @throws UnexpectedEventException Event detected that doesn't match the
     * behaviour described in the state machine.
     */
    String evaluateTransition(PrivacyEvent input)
            throws UnexpectedEventException;

 }