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
public abstract class UrlPartMatcher {

  public static UrlPartMatcher createUrlPartMatcher(String urlPart) {
    if (urlPart.startsWith("{") && urlPart.endsWith("}")) {
      return new UrlPartMatcherVariable(urlPart.substring(1, urlPart.length()-1));
    } 
    return new UrlPartMatcherStatic(urlPart);
  }

  public abstract boolean matches(String requestPiece, Map<String, String> urlVariables);
}
