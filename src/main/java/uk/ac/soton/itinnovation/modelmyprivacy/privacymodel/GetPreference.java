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

package uk.ac.soton.itinnovation.modelmyprivacy.privacymodel;

import java.util.List;

/**
 * Each element in the tree has operations to read information about
 * the preference of this node in the tree.
 * @author pjg
 */
public interface GetPreference {

    /**
     * Return the data category that this node in the model represents.
     * @return
     */
    public String getData();

    /**
     * Return the list of all preferences attached to this piece of data.
     * @return List of preference weightings
     */
    public List<UserPreference> getPreferenceList();

    /**
     * Return the list of all preferences attached to this piece of data concerning
     * a given Role.
     * @return List of preference weightings
     */
    public List<UserPreference> getPreferenceListFromRole(String role);

    /**
     * Return the list of all preferences attached to this piece of data concerning
     * a given Purpose.
     * @return List of preference weightings
     */
    public List<UserPreference> getPreferenceListPurpose(String purpose);

    /**
     * Return the list of all preferences attached to this piece of data concerning
     * a given Action.
     * @return List of preference weightings
     */
    public List<UserPreference> getPreferenceListAction(String action);

    /**
     * Return the data preference for this piece of data
     * @return
     */
    public int getPreference();

     /**
     * Return the data preference for this piece of data
     * @return
     */
    public void setPreference(int preference);
}
