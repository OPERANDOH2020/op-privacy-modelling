/////////////////////////////////////////////////////////////////////////
//
// © University of Southampton IT Innovation Centre, 2016
//
// Copyright in this software belongs to University of Southampton
// IT Innovation Centre of Gamma House, Enterprise Road,
// Chilworth Science Park, Southampton, SO16 7NS, UK.
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
//      Created By :            Panos Melas
//      Created Date :          2016-04-28
//      Created for Project :   OPERANDO
//
/////////////////////////////////////////////////////////////////////////


package uk.ac.soton.itinnovation.modelmyprivacy.privacymodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;


/**
 * UserPreference
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-11-03T13:32:54.440Z")
public class UserPreference   {
  @JsonProperty("informationtype")
  private String informationtype = null;

  @JsonProperty("category")
  private String category = null;

  @JsonProperty("preference")
  private String preference = null;

  @JsonProperty("role")
  private String role = null;

  @JsonProperty("action")
  private String action = null;

  @JsonProperty("purpose")
  private String purpose = null;

  @JsonProperty("recipient")
  private String recipient = null;

  public UserPreference informationtype(String informationtype) {
    this.informationtype = informationtype;
    return this;
  }

   /**
   * The type of private information; e.g. is it information that identifies the user (e.g. id number)? is it location information about the user? Is it about their activities?
   * @return informationtype
  **/
  public String getInformationtype() {
    return informationtype;
  }

  public void setInformationtype(String informationtype) {
    this.informationtype = informationtype;
  }

  public UserPreference category(String category) {
    this.category = category;
    return this;
  }

   /**
   * The category of the service invading the privacy of the user.
   * @return category
  **/
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public UserPreference preference(String preference) {
    this.preference = preference;
    return this;
  }

   /**
   * The user's privacy preference. High means they are sensitive to disclosing private information. Medium they have concerns; and low means they have few privacy concerns with this question.
   * @return preference
  **/
  public String getPreference() {
    return preference;
  }

  public void setPreference(String preference) {
    this.preference = preference;
  }

  public UserPreference role(String role) {
    this.role = role;
    return this;
  }

   /**
   * The role of a person or service that the private information is being disclosed to or used by. This is an optional parameter in the case where users drill down to more detailed preferences.
   * @return role
  **/
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public UserPreference action(String action) {
    this.action = action;
    return this;
  }

   /**
   * The action being carried out on the private date e.g. accessing, disclosing to a third party. This is an optional parameter in the case where users drill down to more detailed preferences.
   * @return action
  **/
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public UserPreference purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

   /**
   * The purpose for which the service is using the private data. This is an optional parameter in the case where users drill down to more detailed preferences.
   * @return purpose
  **/
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public UserPreference recipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * The recipient of any disclosed privacy information. This is an optional parameter in the case where users drill down to more detailed preferences.
   * @return recipient
  **/
  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserPreference userPreference = (UserPreference) o;
    return Objects.equals(this.informationtype, userPreference.informationtype) &&
        Objects.equals(this.category, userPreference.category) &&
        Objects.equals(this.preference, userPreference.preference) &&
        Objects.equals(this.role, userPreference.role) &&
        Objects.equals(this.action, userPreference.action) &&
        Objects.equals(this.purpose, userPreference.purpose) &&
        Objects.equals(this.recipient, userPreference.recipient);
  }

  @Override
  public int hashCode() {
    return Objects.hash(informationtype, category, preference, role, action, purpose, recipient);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("    \"informationtype\": \"").append(toIndentedString(informationtype)).append("\"\n");
    sb.append("    \"category\": \"").append(toIndentedString(category)).append("\"\n");
    sb.append("    \"preference\": \"").append(toIndentedString(preference)).append("\"\n");
    sb.append("    \"role\": \"").append(toIndentedString(role)).append("\"\n");
    sb.append("    \"action\": \"").append(toIndentedString(action)).append("\"\n");
    sb.append("    \"purpose\": \"").append(toIndentedString(purpose)).append("\"\n");
    sb.append("    \"recipient\": \"").append(toIndentedString(recipient)).append("\"\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}