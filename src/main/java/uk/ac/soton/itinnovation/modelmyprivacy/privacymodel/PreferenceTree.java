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

/**
 * The model of user preferences. This is a searchable tree to find
 * the preference of a given private data.
 * @author Paul Grace
 */
public class PreferenceTree extends GraphPreference{

    public final static String ROOTDATA = "ANY";
    public final static int MEDIANWESTIN = 6;

    private int WestinClassification;

    public PreferenceTree(String input){
        super(ROOTDATA);
        /**
         * Set the default value.
         */
        WestinClassification = MEDIANWESTIN;
    }


    @Override
    public String getData() {
        return ROOTDATA;
    }

    @Override
    public int getPreference() {
        return WestinClassification;
    }

    @Override
    public void setPreference(int newPreference) {
        WestinClassification = newPreference;
    }

    @Override
    public String toString(){
        StringBuilder sB = new StringBuilder();
        sB.append("[ROOTDATA]: ").append(getPreference());



        return sB.toString();
    }
}
