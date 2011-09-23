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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import be.tombaeyens.rest.handler.HelpHandler;
import be.tombaeyens.rest.handler.ItemHandler;



/**
 * @author Tom Baeyens
 */
public class RestServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static Logger log = Logger.getLogger(RestServlet.class.getName());

  protected Map<String, RestHandler> staticHandlers = new HashMap<String, RestHandler>();
  protected List<UrlMatcher> dynamicHandlers = new ArrayList<UrlMatcher>();

  public RestServlet() { 
    register(new HelpHandler(this));
    register(new ItemHandler());
  }

  public void register(RestHandler handler) {
    String urlPattern = handler.getUrlPattern();
    if (urlPattern.indexOf('{')==-1) {
      staticHandlers.put(urlPattern, handler);
    } else {
      dynamicHandlers.add(new UrlMatcher(handler));
    }
  }

  private RestHandler getRestHandler(RestRequestContext restRequestContext) {
    String pathInfo = restRequestContext.getPathInfo();
    RestHandler restHandler = staticHandlers.get(pathInfo);
    if (restHandler==null) {
      Iterator<UrlMatcher> iter = dynamicHandlers.iterator();
      while (restHandler==null && iter.hasNext()) {
        UrlMatcher urlMatcher = iter.next();
        Map<String, String> urlVariables = urlMatcher.matches(restRequestContext);
        if (urlVariables!=null) {
          restRequestContext.setUrlVariables(urlVariables);
          restHandler = urlMatcher.getRestHandler();
        }
      }
      if (restHandler==null) {
        throw new BadRequestException("invalid url "+pathInfo);
      }
    }
    return restHandler;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    authenticateAndHandle(HttpServletMethod.GET, request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    authenticateAndHandle(HttpServletMethod.POST, request, response);
  }

  protected void authenticateAndHandle(HttpServletMethod method, HttpServletRequest request, HttpServletResponse response) {
    RestRequestContext restRequestContext = new RestRequestContext(method, request, response);
    try {
      RestHandler restHandler = getRestHandler(restRequestContext);
      if (restHandler.requiresAuthentication()) {
        authenticate(restRequestContext);
      }
      restHandler.handle(restRequestContext);

    } catch (RestException e) {
      logException(e, restRequestContext);
      try {
        restRequestContext.getHttpServletResponse().sendError(e.getResponseCode(), e.getMessage());
      } catch (Exception e1) {
        log.error("problem while sending error", e);
      }
      throw e;
      
    } catch (Throwable e) {
      logException(e, restRequestContext);
      throw new RuntimeException("rest exception: "+e.getMessage(), e);
    }
  }

  protected void logException(Throwable e, RestRequestContext restRequestContext) {
    log.error("exception while processing "+restRequestContext.getHttpServletRequest().getPathInfo()+" : "+e.getMessage(), e);
  }

  protected void authenticate(RestRequestContext restRequestContext) {
    HttpServletRequest httpServletRequest = restRequestContext.getHttpServletRequest();
    String headerText = httpServletRequest.getHeader("Auth");
    if (headerText==null) {
      headerText = httpServletRequest.getHeader("Authorization");
    }
    if ( headerText!=null 
         && headerText.startsWith("Basic ") 
       ) {
      String encodedHeader = headerText.substring(6);
      String decodedHeader = new String(Base64.decodeBase64(encodedHeader));
      int colonIndex = decodedHeader.indexOf(':');
      if (colonIndex!=-1) {
        String username = decodedHeader.substring(0, colonIndex);
        String password = decodedHeader.substring(colonIndex+1);
        
        log.error("username " + username + ", password " + password);
        
//        User user = someUserService.findUserById(username);
//        
//        if (user!=null) {
//          boolean authenticationOk = someUserService.isPasswordOk(password, username);
//          
//          if (authenticationOk) {
//             restRequestContext.setAuthenticatedUserId(username);
//             return;
//          }
//        }
      }
    }
    
    restRequestContext.getHttpServletResponse().setHeader("WWW-Authenticate", "Basic realm=\"Pangu\"");
    throw new RestException(HttpServletResponse.SC_UNAUTHORIZED, "basic authentication required");
  }

  public Map<String, RestHandler> getStaticHandlers() {
    return staticHandlers;
  }
  
  public List<UrlMatcher> getDynamicHandlers() {
    return dynamicHandlers;
  }
}
