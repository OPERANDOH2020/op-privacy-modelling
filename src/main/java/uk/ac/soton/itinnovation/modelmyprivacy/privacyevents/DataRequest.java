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

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;


public class DataRequest   {

  /**
   * Organisation
   */
  private String requesterId = null;

  /**
   * Role
   */
  private String subject = null;

  /**
   * Data
   */
  private String requestedUrl = null;

  /**
   * The action being carried out on the private date e.g. accessing, disclosing to a third party.
   */
  public enum ActionEnum {
    ANONYMIZE("Anonymize"),
    DISCLOSE("Disclose"),
    ARCHIVE("Archive"),
    READ("Read"),
    UPDATE("Update"),
    DELETE("Delete"),
    CREATE("Create");

    private String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private ActionEnum action = null;

  /**
   * Id of the requester (typically the id of an OSP).
   **/
  public DataRequest requesterId(String requesterId) {
    this.requesterId = requesterId;
    return this;
  }

  @JsonProperty("requester_id")
  public String getRequesterId() {
    return requesterId;
  }

  public void setRequesterId(String requesterId) {
    this.requesterId = requesterId;
  }

  /**
   * A description of the subject who the policies grants/doesn't grant to carry out.
   **/
  public DataRequest subject(String subject) {
    this.subject = subject;
    return this;
  }

  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
  }

  /**
   * The Requested URL of the data.
   **/
  public DataRequest requestedUrl(String requestedUrl) {
    this.requestedUrl = requestedUrl;
    return this;
  }

  @JsonProperty("requested_data")
  public String getRequestedUrl() {
    return requestedUrl;
  }
  public void setRequestedUrl(String requestedUrl) {
    this.requestedUrl = requestedUrl;
  }

  /**
   * The action being carried out on the private date e.g. accessing, disclosing to a third party.
   **/
  public DataRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }
  public void setAction(ActionEnum action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRequest oSPDataRequest = (DataRequest) o;
    return Objects.equals(requesterId, oSPDataRequest.requesterId) &&
        Objects.equals(subject, oSPDataRequest.subject) &&
        Objects.equals(requestedUrl, oSPDataRequest.requestedUrl) &&
        Objects.equals(action, oSPDataRequest.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requesterId, subject, requestedUrl, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("    \"requester_id\": \"").append(toIndentedString(requesterId)).append("\",\n");
    sb.append("    \"subject\": \"").append(toIndentedString(subject)).append("\",\n");
    sb.append("    \"requested_data\": \"").append(toIndentedString(requestedUrl)).append("\",\n");
    sb.append("    \"action\": \"").append(toIndentedString(action)).append("\"\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

