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
import java.util.ArrayList;
import java.util.List;


/**
 * A transition describes the transition from state of the machine
 * to another state in the machine.
 *
 * Each transition has a set of conditions (guards) that must be true i.e.
 * the transition is taken iff all conditions of the transition are matched
 * by an event that has a occurred.
 *
 * In the case of the Interoperability Test Engine: an event is a REST based
 * operation.
 *
 * @author pjg@it-innovation.soton.ac.uk
 */
public class Transition {

    /**
     * The label/id of the transition where directed to ie. the state label.
     */
    private transient String transitionLabel;

    /**
     * The list of guard transitions attached to the transition.
     */
    private transient List<Guard> conditions = new ArrayList();


    /**
     * Create a basic transition.
     * @param label The identifier of the transition's to i.e. the label of the
     * to state.
     */
    public Transition(final String label) {
        this.transitionLabel = label;
    }

    /**
     * Create a guard transition.
     * @param label The to label.
     * @param guards The list of guards to evaluate events against.
     */
    public Transition(final String label, final List<Guard> guards) {
        this(label);
        this.conditions = guards;
    }


    /**
     * Get the to label of the state transition is directed at.
     * @return The string id of the to state.
     */
    public final String readLabel() {
        return this.transitionLabel;
    }


    /**
     * Add a new guard to the transition.
     * @param guard The guard rule.
     * @throws InvalidGuardException Error in the guard specification.
     */
    public final void addGuard(final Guard guard) throws InvalidGuardException {
        this.conditions.add(guard);
    }

    /**
     * Get all the guards on the transition.
     * @return The list of guards.
     */
    public final List<Guard> listGuards() {
        return this.conditions;
    }

}