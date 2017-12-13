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

/**
 * The individual state variable of a state. Each state can have N
 * state variables.
 *
 * A state variable is the indicator of privacy information about a particular
 * piece of data
 *
 * Record - seen
 * Policy - can it be seen
 *
 */
public class StateVariable {

    /**
     * A state variable represent a set of indicators for a Role's access to
     * a field.
     */
    private final String fieldName;
    public String getField() {
        return this.fieldName;
    }

    private final String roleName;
    public String getRole() {
        return this.roleName;
    }

    /**
     * State variable to indicate that this field has been seen by this
     * record.
     */
    private boolean seenByRole;

    /**
     * Actions that may be carried/allowed out. Read policy.
     */
    private boolean canRead;

    /**
     * Actions that may be carried/allowed out. Write policy.
     */
    private boolean canUpdate;

    /**
     * The constructor - create the object for the fixed role and field.
     * @param role The role for the policy variable
     * @param field The field name.
     */
    public StateVariable(String role, String field) {
        this.fieldName = field;
        this.roleName = role;
        this.canRead = false;
        this.canUpdate = false;
        this.seenByRole = false;
    }

    /**
     * Set the seen variable to a given value.
     * @param val true or false
     */
    public void setSeen(boolean val) {
        this.seenByRole = val;
    }

    public boolean getSeen() {
        return this.seenByRole;
    }

    public void setCanRead(boolean val) {
        this.canRead = val;
    }

    public boolean getCanRead() {
        return this.canRead;
    }

    public void setCanUpdate(boolean val) {
        this.canRead = val;
    }

    public boolean getCanUpdate() {
        return this.canRead;
    }
}
