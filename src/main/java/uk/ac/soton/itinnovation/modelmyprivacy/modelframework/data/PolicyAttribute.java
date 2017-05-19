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

package uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;





@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-03-30T12:50:20.171Z")
public class PolicyAttribute   {

  private String attributeName = null;
  private String attributeValue = null;


  /**
   **/
  public PolicyAttribute attributeName(String attributeName) {
    this.attributeName = attributeName;
    return this;
  }


  @JsonProperty("attribute_name")
  public String getAttributeName() {
    return attributeName;
  }
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }


  /**
   **/
  public PolicyAttribute attributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
    return this;
  }

  @JsonProperty("attribute_value")
  public String getAttributeValue() {
    return attributeValue;
  }
  public void setAttributeValue(String attributeValue) {
    this.attributeValue = attributeValue;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PolicyAttribute policyAttribute = (PolicyAttribute) o;
    return Objects.equals(attributeName, policyAttribute.attributeName) &&
        Objects.equals(attributeValue, policyAttribute.attributeValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeName, attributeValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\n");

    sb.append("    \"attribute_name\": \"").append(toIndentedString(attributeName)).append("\",\n");
    sb.append("    \"attribute_value\": \"").append(toIndentedString(attributeValue)).append("\"\n");
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

