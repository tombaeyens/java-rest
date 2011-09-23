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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Tom Baeyens
 */
public class RestRequestContext {

  protected String authenticatedUserId;
  protected List<String> parameterErrors = null;
  protected HttpServletMethod httpServletMethod;
  protected HttpServletRequest httpServletRequest;
  protected HttpServletResponse httpServletResponse;
  protected Map<String, String> urlVariables;
  protected List<String> choppedPath;
  
  public RestRequestContext(HttpServletMethod httpServletMethod, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    this.httpServletMethod = httpServletMethod;
    this.httpServletRequest = httpServletRequest;
    this.httpServletResponse = httpServletResponse;
  }

  public void sendResponseJsonText(String json) {
    sendResponse("text/json", json);
  }

  public void sendResponsePlainText(String text) {
    sendResponse("text/plain", text);
  }

  public void sendResponse(String mimeType, String text) {
    httpServletResponse.setContentType(mimeType);
    try {
      httpServletResponse
        .getOutputStream()
        .println(text);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getPathInfo() {
    return httpServletRequest.getPathInfo();
  }
  
  public void setUrlVariables(Map<String, String> urlVariables) {
    this.urlVariables = urlVariables;
  }
  
  public void addParameterError(String msg) {
    if (parameterErrors==null) {
      parameterErrors = new ArrayList<String>();
    }
    parameterErrors.add(msg);
  }
  
  public List<String> getChoppedPath() {
    if (choppedPath==null) {
      choppedPath = new ArrayList<String>();
      StringTokenizer tokenizer = new StringTokenizer(getPathInfo(), "/");
      while (tokenizer.hasMoreTokens()) {
        choppedPath.add(tokenizer.nextToken());
      }
    }
    return choppedPath;
  }
  
  public Map<String, String> getUrlVariables() {
    return urlVariables;
  }

  public HttpServletMethod getHttpServletMethod() {
    return httpServletMethod;
  }

  public HttpServletRequest getHttpServletRequest() {
    return httpServletRequest;
  }

  public HttpServletResponse getHttpServletResponse() {
    return httpServletResponse;
  }
  
  public String getAuthenticatedUserId() {
    return authenticatedUserId;
  }
  
  public void setAuthenticatedUserId(String authenticatedUserId) {
    this.authenticatedUserId = authenticatedUserId;
  }
}
