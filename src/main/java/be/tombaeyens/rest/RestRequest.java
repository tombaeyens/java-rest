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

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;


/**
 * @author Tom Baeyens
 */
public class RestRequest {
  
  private static Logger log = Logger.getLogger(RestRequest.class.getName());

  protected String url;
  protected String contentType = "application/x-www-form-urlencoded";
  protected String userId;
  protected String password;
  protected int expectedResponseCode = -1;
  protected String expectedResponseMessage = null;

  protected HttpURLConnection httpUrlConnection;

  public RestRequest(String url) {
    try {
      this.url = url;
      this.httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
    } catch (Exception e) {
      log.error("couldn't create rest request: "+e.getMessage(), e);
    }
  }
  
  public RestRequest(String baseContextUrl, String contextPath) {
    this(baseContextUrl+contextPath);
  }
  
  public RestRequest authenticate(String userId, String password) {
    String authHeaderValue = userId+":"+password;
    String encodedAuthHeaderValue = new String(Base64.encodeBase64(authHeaderValue.getBytes()));
    httpUrlConnection.addRequestProperty("Auth", "Basic "+encodedAuthHeaderValue);
    return this;
  }
  
  public int getExpectedResponseCode() {
    return expectedResponseCode;
  }
  
  public RestRequest setExpectedResponseCode(int expectedResponseCode) {
    this.expectedResponseCode = expectedResponseCode;
    return this;
  }

  public String getExpectedResponseMessage() {
    return expectedResponseMessage;
  }
  
  public RestRequest setExpectedResponseMessage(String expectedResponseMessage) {
    this.expectedResponseMessage = expectedResponseMessage;
    return this;
  }
  
  public String getContentType() {
    return contentType;
  }
  
  public RestRequest setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }


  public RestResponse post(String postParameters) {
    try {
      httpUrlConnection.setDoOutput(true);
      httpUrlConnection.setRequestProperty( "Content-Type", contentType );
      httpUrlConnection.setRequestProperty( "Content-Length", Integer.toString(postParameters.length()) );
      httpUrlConnection.getOutputStream().write(postParameters.getBytes());
      log.info("posting "+url+" with encoded parameters \n"+postParameters);

      return new RestResponse(this);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RestException(500, "posting failed: \n "+postParameters, e);
    }
  }

  public RestResponse get() {
    log.info("getting "+url);
    return new RestResponse(this);
  }
  
  public HttpURLConnection getHttpUrlConnection() {
    return httpUrlConnection;
  }
  
  public String getUrl() {
    return url;
  }
}
