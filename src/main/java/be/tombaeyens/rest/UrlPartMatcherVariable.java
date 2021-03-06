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

import java.util.Map;


/**
 * @author Tom Baeyens
 */
public class UrlPartMatcherVariable extends UrlPartMatcher {

  protected String variableName;
  
  public UrlPartMatcherVariable(String variableName) {
    this.variableName = variableName;
  }

  @Override
  public boolean matches(String requestPiece, Map<String, String> urlVariables) {
    urlVariables.put(variableName, requestPiece);
    return true;
  }
}
