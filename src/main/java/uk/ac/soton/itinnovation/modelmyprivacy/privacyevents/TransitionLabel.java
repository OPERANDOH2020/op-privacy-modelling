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

package uk.ac.soton.itinnovation.modelmyprivacy.privacyevents;

import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidRoleException;

/**
 * A guard is a rule applied to an event that evaluates to true or false.
 * A transition can only be realised where
 *
 * @author pjg@it-innovation.soton.ac.uk
 */
public class TransitionLabel {

    /**
     * The type of the guard. That is the function.
     */
    private final transient String role;
    public final String getRole() {
        return role;
    }
    /**
     * The data type of the guarded value.
     */
    private final transient String action;
    public final String getAction() {
        return action;
    }
    /**
     * What will be used as the comparitor for the guard. If V is the tested
     * value; then when V is applied to the function the evaluation against
     * this compareto value must be true.
     */
    private transient String data;

    /**
     * Return the comparison value that this guard is evaluating.
     * @return The guard value to compare against.
     */
    public final String getData() {
        return data;
    }

    /**
     * What will be used as the comparitor for the guard. If V is the tested
     * value; then when V is applied to the function the evaluation against
     * this compareto value must be true.
     */
    private transient String purpose;

    /**
     * Return the comparison value that this guard is evaluating.
     * @return The guard value to compare against.
     */
    public final String getPurpose() {
        return purpose;
    }

    /**
     * What will be used as the comparitor for the guard. If V is the tested
     * value; then when V is applied to the function the evaluation against
     * this compareto value must be true.
     */
    private transient double risk;

    /**
     * Return the comparison value that this guard is evaluating.
     * @return The guard value to compare against.
     */
    public final double getRisk() {
        return risk;
    }

    public final void setRisk(double risk) {
        this.risk = risk;
    }

    /**
     * Construct the guard. Note, all elements are translated to lowercase for
     * case independent matching. This is because, there may be little
     * standardisation of http fields.
     * @throws InvalidGuard Exception indicating guard could not be produced from the inputs
     */
    public TransitionLabel(final String roleInput, final String actionInput, String dataInput, String purposeInput) throws InvalidTransitionLabel {
        this.role = roleInput;
        this.action = actionInput;
        this.data = dataInput;
        this.purpose = purposeInput;
    }

    /**
     * Evaluate this guard against the provided input.
     * @return The result of the evaluated guard against the input.
     * @throws InvalidRoleException Error in input and exception thrown during compare.
     */
    public final boolean evaluate(final String action, String role, String data) throws InvalidRoleException {
        if(!action.equalsIgnoreCase(this.action))
            return false;
        if(!role.equalsIgnoreCase(this.role))
            return false;
        if(!data.equalsIgnoreCase(this.data))
            return false;
        return true;
    }
}

