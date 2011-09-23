/* Alfresco.com
 * Copyright (C) 2005-2011 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package be.tombaeyens.rest;

import javax.servlet.http.HttpServletResponse;




/**
 * @author Tom Baeyens
 */
public abstract class Parameter <T> {
  
  protected Class<T> type;
  protected String name;
  protected String description;
  protected int maxLength = Integer.MAX_VALUE;
  protected boolean isParameter = true;
  /** defaultValue==null means this parameter is required */
  protected T defaultValue;

  public Parameter(Class<T> type) {
    this.type = type;
  }

  public Parameter(Class<T> type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }
  
  public Parameter<T> setName(String name) {
    this.name = name;
    return this;
  }

  public Parameter<T> setDescription(String description) {
    this.description = description;
    return this;
  }

  public Parameter<T> setMaxLength(int maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public Parameter<T> setUrlVariable() {
    this.isParameter = false;
    return this;
  }

  public Parameter<T> setDefaultValue(T defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }
  
  public abstract T convert(String parameterValue);
  public abstract String getTypeDescription();

  public T get(RestRequestContext restRequestContext) {
    String textValue = null;
    if (isParameter) {
      textValue = restRequestContext
        .getHttpServletRequest()
        .getParameter(name);
    } else {
      textValue = restRequestContext
        .getUrlVariables()
        .get(name);
    }
    
    if (textValue==null) {
      if (defaultValue!=null) {
        return defaultValue;
      }
      throw new RestException(HttpServletResponse.SC_BAD_REQUEST, "parameter "+name+" is required");
    }
    
    return convert(textValue);
  }

  public Class<T> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public boolean isRequired() {
    return defaultValue==null;
  }

  public String getDescription() {
    return description;
  }
  
  public int getMaxLength() {
    return maxLength;
  }
  
  public boolean isParameter() {
    return isParameter;
  }
}
