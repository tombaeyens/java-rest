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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.log4j.Logger;


/**
 * @author Tom Baeyens
 */
public class RestResponse {
  
  private static Logger log = Logger.getLogger(RestResponse.class.getName());

  RestRequest restRequest;
  HttpURLConnection httpUrlConnection;
  String content;
  int responseCode = -1;
  String responseMessage = null;

  public RestResponse(RestRequest restRequest) {
    this.restRequest = restRequest;
    this.httpUrlConnection = restRequest.getHttpUrlConnection();
    try {
      responseCode = httpUrlConnection.getResponseCode();
      responseMessage = httpUrlConnection.getResponseMessage();
      log.info("response code: "+responseCode+" "+responseMessage);
    } catch (Exception e) {
      log.error("exception getting response code and message"+e.getMessage(), e);
    }
    
    if (restRequest.getExpectedResponseCode()!=responseCode
         || ( (restRequest.getExpectedResponseMessage()!=null)
              && (!restRequest.getExpectedResponseMessage().equals(responseMessage))
            )
       ){
      throw new RestException(500, "exception while performing request "+restRequest.getUrl()+" ["+responseCode+"] "+responseMessage);
    }

    try {
      InputStream stream = httpUrlConnection.getInputStream();
      content = new String(readInputStream(stream, "rest request response"));
      log.info("response content: "+content);
      
    } catch (Exception e) {
      log.error("exception getting response code and message"+e.getMessage(), e);
    }
  }

  public static byte[] readInputStream(InputStream inputStream, String inputStreamName) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[16*1024];
    try {
      int bytesRead = inputStream.read(buffer);
      while (bytesRead!=-1) {
        outputStream.write(buffer, 0, bytesRead);
        bytesRead = inputStream.read(buffer);
      }
    } catch (Exception e) {
      throw new RestException(500, "couldn't read input stream "+inputStreamName, e);
    }
    return outputStream.toByteArray();
  }

  public HttpURLConnection getHttpUrlConnection() {
    return httpUrlConnection;
  }

  public String getContent() {
    return content;
  }
  
  public int getResponseCode() {
    return responseCode;
  }
  
  public String getResponseMessage() {
    return responseMessage;
  }
}
