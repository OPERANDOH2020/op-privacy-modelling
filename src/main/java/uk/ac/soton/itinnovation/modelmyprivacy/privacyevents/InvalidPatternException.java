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

/**
 * Exception to identify where XML description of state does not follow
 * the schema.
 * @author pjg
 */
public class InvalidPatternException extends Exception {

    /**
     * Error in the XML pattern specification.
     * @param exceptionMessage The qualifying error message.
     */
    public InvalidPatternException(final String exceptionMessage) {
        super(exceptionMessage);
    }

    /**
     * Error in the XML pattern specification.
     * @param exceptionMessage The qualifying error message.
     * @param excep The stack trace.
     */
    public InvalidPatternException(final String exceptionMessage, final Exception excep) {
        super(exceptionMessage, excep);
    }

}
