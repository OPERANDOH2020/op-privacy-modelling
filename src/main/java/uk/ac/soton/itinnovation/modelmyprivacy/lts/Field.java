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

import java.util.ArrayList;
import java.util.List;

/**
 * A field is an individual piece of data. Such a piece of data
 * belongs to a category of data e.g. contact info, medical information,
 * etc. The field may also be sensitive in terms of protection, and also
 * it may be an explicit identifier which on its own can be used to
 * identify a user.
 *
 * A field can also be a record.
 */
public class Field {

    /**
     * The field name (unique with a record).
     */
    private final String name;

    /**
     * The set of categories a field belongs to.
     */
    private final List<String> categories;

    /**
     * Indication whether this field can be used as an EI.
     */
    private boolean explicitIdentifier;

    /**
     * Indication whether this field can be used as an QI
     */
    private boolean quasiIdentifier;

    /**
     * Indication if this a sensitive field.
     */
    private boolean sensitive;

    /**
     * Indication if this is a record.
     */
    private boolean record;

    /**
     * If this is a record, then the reference to reference object containing
     * a set of fields.
     */
    private List<Field> recordReference = null;

    /**
     * Create an instance of the field object. The name is the fixed parameter.
     * The rest of the data must be created using the getter and setter APIs.
     * @param name The field name within a record.
     */
    public Field(String name) {
        this.name = name;
        this.explicitIdentifier = false;
        this.quasiIdentifier = false;
        this.sensitive = false;
        this.record = false;
        this.categories = new ArrayList<String>();
    }

    /**
     * Get the field name.
     * @return The String value of the field name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter function for the record boolean value to state that this field
     * is a record (i.e. contains one or more fields or records).
     * @param value The indicator value; true if this field is a record.
     */
    public void setRecord(boolean value) {
        this.record = value;
    }

    /**
     * Getter function for the record boolean value.
     * @return Whether this is a record field or not.
     */
    public boolean getRecord() {
        return this.record;
    }

    /**
     * Setter function for the sensitive boolean value to state that this is
     * a potentially sensitive field.
     * @param value The indicator value; true if this field is sensitive.
     */
    public void setSensitive(boolean value) {
        this.sensitive = value;
    }

    /**
     * Getter function for the sensitive boolean value.
     * @return Whether this is a sensitive field or not.
     */
    public boolean getSensitive() {
        return this.sensitive;
    }

    /**
     * Setter function for the EI boolean value to state that this is
     * an explicit identifier field.
     * @param value The indicator value; true if this field is sensitive.
     */
    public void setEI(boolean value) {
        this.explicitIdentifier = value;
    }

    /**
     * Getter function for the EI boolean value.
     * @return Whether this is a explicit identifier field or not.
     */
    public boolean getEI() {
        return this.explicitIdentifier;
    }

     /**
     * Setter function for the QI boolean value to state that this is
     * an explicit identifier field.
     * @param value The indicator value; true if this field is quasi id.
     */
    public void setQI(boolean value) {
        this.quasiIdentifier = value;
    }

    /**
     * Getter function for the QI boolean value.
     * @return Whether this is a quasi identifier field or not.
     */
    public boolean getQI() {
        return this.quasiIdentifier;
    }

    /**
     * Set the field to point to a record.
     * @param newRecord The record this field points to.
     */
    public void setRecordField(List<Field> newRecord) {
        this.recordReference = newRecord;
        this.record = true;
    }

    /**
     * Get the pointed to record.
     * @return The record this field points to.
     */
    public List<Field> getRecordField() {
        return this.recordReference;
    }

    /**
     * Add a category. Note, that because the model is created dynamically
     * there is no need to have a remove option.
     * @param category The data category to add.
     */
    public void addCategory(String category){
        if(category != null) {
            this.categories.add(category);
        }
    }

    /**
     * Get a list of categories.
     * @return the List of string categories.
     */
    public List<String> getCategory() {
        return this.categories;
    }
}
