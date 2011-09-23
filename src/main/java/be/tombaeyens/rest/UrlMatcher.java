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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;



/**
 * @author Tom Baeyens
 */
public class UrlMatcher {
  
  protected RestHandler restHandler;
  protected List<UrlPartMatcher> urlPartMatchers;

  public UrlMatcher(RestHandler restHandler) {
    this.restHandler = restHandler;
    this.urlPartMatchers = new ArrayList<UrlPartMatcher>();
    StringTokenizer tokenizer = new StringTokenizer(restHandler.getUrlPattern(), "/");
    while (tokenizer.hasMoreTokens()) {
      String urlPart = tokenizer.nextToken();
      urlPartMatchers.add(UrlPartMatcher.createUrlPartMatcher(urlPart));
    }
  }

  public Map<String, String> matches(RestRequestContext request) {
    List<String> requestPieces = request.getChoppedPath();
    if (requestPieces.size()!=urlPartMatchers.size()) {
      return null;
    }
    Map<String, String> urlVariables = new HashMap<String, String>();
    for (int i = 0; i < requestPieces.size(); i++) {
      String requestPiece = requestPieces.get(i);
      UrlPartMatcher urlPartMatcher = urlPartMatchers.get(i);
      if (! urlPartMatcher.matches(requestPiece, urlVariables)) {
        return null;
      }
    }
    return urlVariables;
  }

  public RestHandler getRestHandler() {
    return restHandler;
  }
}
