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

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * The set of methods to perform reusable test actions on Operando modules.
 */
public class TestHelperMethods {

    public String readJsonFromFile(String fileLoc) {

        InputStream fis = null;
        try {
            fis = this.getClass().getClassLoader().getResourceAsStream(fileLoc);
            String content = CharStreams.toString(new InputStreamReader(fis, Charsets.UTF_8));
            Closeables.closeQuietly(fis);
            return content;

        } catch (IOException e) {
            // Display to console for debugging purposes.
            System.err.println("Could not read Json from file: " + fileLoc);
            return null;
        }
    }
}