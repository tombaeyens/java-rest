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
package be.tombaeyens.rest.parameter;


import be.tombaeyens.rest.BadRequestException;
import be.tombaeyens.rest.Parameter;


public class IntegerParameter extends Parameter<Integer> {
  
  int minValue = Integer.MIN_VALUE;
  int maxValue = Integer.MAX_VALUE;

  public IntegerParameter() {
    super(Integer.class);
  }

  public IntegerParameter(String name, String description) {
    super(Integer.class, name, description);
  }
  
  public IntegerParameter(String name, String description, int minValue, int maxValue) {
    super(Integer.class, name, description);
    this.minValue = minValue;
    this.maxValue = maxValue;
  }
  
  
  public IntegerParameter setMinValue(int minValue) {
    this.minValue = minValue;
    return this;
  }
  
  public IntegerParameter setMaxValue(int maxValue) {
    this.maxValue = maxValue;
    return this;
  }
  
  public IntegerParameter setDefaultValue(Integer defaultValue) {
    super.setDefaultValue(defaultValue);
    return this;
  }

  public IntegerParameter setDescription(String description) {
    super.setDescription(description);
    return this;
  }

  public IntegerParameter setMaxLength(int maxLength) {
    super.setMaxLength(maxLength);
    return this;
  }

  public IntegerParameter setName(String name) {
    super.setName(name);
    return this;
  }

  public IntegerParameter setUrlVariable() {
    super.setUrlVariable();
    return this;
  }

  public Integer convert(String textValue) {
    try {
      Integer intValue = new Integer(textValue);
      if (intValue<minValue) {
        throw new BadRequestException("parameter "+name+" must be greater then "+minValue+": "+textValue);
      }
      if (intValue>maxValue) {
        throw new BadRequestException("parameter "+name+" must be less then "+maxValue+": "+textValue);
      }
      return intValue;
    } catch (NumberFormatException e) {
      throw new BadRequestException("parameter "+name+" is not a valid integer: "+textValue);
    }
  }

  public String getTypeDescription() {
    return "integer ["+minValue+".."+maxValue+"]";
  }
}
