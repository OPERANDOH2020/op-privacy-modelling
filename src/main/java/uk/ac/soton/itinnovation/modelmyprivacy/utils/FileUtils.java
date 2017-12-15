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

package uk.ac.soton.itinnovation.modelmyprivacy.utils;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.LTSLogger;

/**
 * Utility class of file reading operations.
 * @author pjg
 */
public final class FileUtils {

    /**
     * Java system property for the current executing directory.
     */
    public static final String THISDIR = System.getProperty("user.dir");

    /**
     * Java system property for a file SEPARATOR character.
     */
    public static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * Utility class, simple constructor.
     */
    private FileUtils() {
        // No implementation needed
    }

    /**
     * Location of general resource file (not resource JAR files).
     * @param file The file to get the resource path of.
     * @return The path of the specified resource file.
     */
    public static Path resourceFiles(final String file) {
        return Paths.get(File.separator + "root" + File.separator
                + "InteroperabilityTool" + File.separator
                + "src" + File.separator + "main" + File.separator
                + "resources" + File.separator + file);
    }

    /**
     * Retrieve the URL of a file, given its filename.
     * @param fileName The file name descriptor.
     * @return The file reference in URL form.
     */
    public static URL getURL(final String fileName) {

        return Thread.currentThread().getContextClassLoader().getResource(fileName);

    }


    /**
     * Read the contents of a file.
     * @param path The file path
     * @param encoding The file encoding.
     * @return The contents of the file.
     * @throws IOException IO exception reading the file.
     */
    public static String readFile(final String path, final Charset encoding)
            throws IOException {
        try {


            final URL resourceUrl = FileUtils.getURL(path);
            final Path resourcePath = Paths.get(resourceUrl.toURI());

            final byte[] encoded = Files.readAllBytes(resourcePath);
            return encoding.decode(ByteBuffer.wrap(encoded)).toString();
        } catch (URISyntaxException ex) {
            LTSLogger.LOG.error("Cannot find resource file: " + path, ex);
        }
        return null;
    }

    public static String readJsonFromFile(String fileLoc) {

        InputStream fis = null;
        try {
            fis = FileUtils.class.getClassLoader().getResourceAsStream(fileLoc);
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
