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

package uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram;

/**
 * Information attached to the data flow transition.
 */
public class FlowLabel {


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
     * Construct the guard. Note, all elements are translated to lowercase for
     * case independent matching. This is because, there may be little
     * standardisation of http fields.
     * @throws InvalidGuard Exception indicating guard could not be produced from the inputs
     */
    public FlowLabel(String dataInput, String purposeInput)  {

        this.data = dataInput;
        this.purpose = purposeInput;

    }

}

