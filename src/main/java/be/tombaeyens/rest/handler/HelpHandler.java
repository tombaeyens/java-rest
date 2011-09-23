package be.tombaeyens.rest.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import be.tombaeyens.rest.HttpServletMethod;
import be.tombaeyens.rest.Parameter;
import be.tombaeyens.rest.RestHandler;
import be.tombaeyens.rest.RestRequestContext;
import be.tombaeyens.rest.RestServlet;
import be.tombaeyens.rest.UrlMatcher;



/**
 * @author Tom Baeyens
 */
public class HelpHandler extends RestHandler {

  RestServlet restServlet;

  public HelpHandler(RestServlet restServlet) {
    this.restServlet = restServlet;
  }

  public HttpServletMethod getMethod() {
    return HttpServletMethod.GET;
  }

  public boolean requiresAuthentication() {
    return false;
  }

  public String getUrlPattern() {
    return "/help";
  }

  public void handle(RestRequestContext restRequestContext) {
    HttpServletResponse httpServletResponse = restRequestContext.getHttpServletResponse();
    httpServletResponse.setContentType("text/html");
    try {
      ServletOutputStream out = httpServletResponse.getOutputStream();
      out.println("<html><body>");
      out.println("<h1>Pangu Rest Interface</h1>");
      out.println("<p>This page documents the rest interface of Pangu</p>");
      
      /*Map<String, String> envMap = System.getenv();
      for (String envKey : envMap.keySet()) {
    	  out.println("<p>key " + envKey + " value " + envMap.get(envKey) + "</p>");
      }
      
      String serviceParam = System.getenv().get("VCAP_SERVICES");
      Object jsonObj = JSONValue.parse(serviceParam);
      JSONObject serviceObject = (JSONObject) jsonObj;
      JSONArray mongoArray = (JSONArray) serviceObject.get("mongodb-1.8");
      JSONObject mongoObject = (JSONObject) mongoArray.get(0);
      JSONObject credentialsObject = (JSONObject) mongoObject.get("credentials");
      String hostname = credentialsObject.get("hostname").toString();
      String username = credentialsObject.get("username").toString();
      String password = credentialsObject.get("password").toString();
      Object passwordObj = credentialsObject.get("password");
      String name = credentialsObject.get("db").toString();
      String port = credentialsObject.get("port").toString();
      out.println("<p>");
      out.println("hostname " + hostname);
      out.println("</p>");
      out.println("<p>");
      out.println("username " + username);
      out.println("</p>");
      out.println("<p>");
      out.println("password " + password);
      out.println("</p>");
      out.println("<p>");
      out.println("passwordObj " + passwordObj);
      out.println("</p>");
      out.println("<p>");
      out.println("name " + name);
      out.println("</p>");
      out.println("<p>");
      out.println("port " + port);
      out.println("</p>");*/
      
      Map<String, RestHandler> restHandlers = new HashMap<String, RestHandler>();
      restHandlers.putAll(restServlet.getStaticHandlers());
      for (UrlMatcher urlMatcher: restServlet.getDynamicHandlers()) {
        RestHandler restHandler = urlMatcher.getRestHandler();
        restHandlers.put(restHandler.getUrlPattern(), restHandler);
      }
      SortedSet<String> urlPatterns = new TreeSet<String>(restHandlers.keySet());
      for (String urlPattern: urlPatterns) {
        RestHandler restHandler = restHandlers.get(urlPattern);
        String parametersTemplate = "";
        List<String> parameterDescriptions = new ArrayList<String>();
        for (Field handlerField: restHandler.getClass().getDeclaredFields()) {
          if (Parameter.class.isAssignableFrom(handlerField.getType())) {
            handlerField.setAccessible(true);
            Parameter<?> parameter = (Parameter<?>) handlerField.get(restHandler);
            String parameterName = parameter.getName();
            if (parameter.isParameter()) {
              if (parametersTemplate.length()==0) {
                parametersTemplate += "?";
              } else {
                parametersTemplate += "&";
              }
              if (parameter.isRequired()) {
                parametersTemplate += parameterName+"={"+parameterName+"}";
              } else {
                parametersTemplate += "["+parameterName+"={"+parameterName+"}]";
              }
            }
            StringBuilder parameterDescription = new StringBuilder();
            parameterDescription.append("  <li><b><code>"+parameterName+"</code></b> ");
            if (parameter.isParameter()) {
              parameterDescription.append(": parameter \n");
            } else {
              parameterDescription.append(": url part variable \n");
            }
            parameterDescription.append("    <ul> \n");
            parameterDescription.append("      <li>"+parameter.getDescription()+"</li> \n");
            parameterDescription.append("      <li>"+(parameter.isRequired() ? "required" : "optional")+"</li> \n");
            parameterDescription.append("      <li>"+parameter.getTypeDescription()+"</li> \n");
            parameterDescription.append("    </ul> \n");
            parameterDescription.append("  </li> \n");
            parameterDescriptions.add(parameterDescription.toString());
          }
        }
        
        out.print("<h3 style='margin-bottom:5px;'><code>");
        out.print(restHandler.getMethod().toString()+" "+urlPattern+parametersTemplate);
        out.println("</code></h3>");
        out.println("  <ul style='margin-top:5px;'>");
        
        for (String parameterDescription: parameterDescriptions) {
          out.print(parameterDescription);
        }
        
        out.println("  </ul>");
        
        out.println("</body></html>");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
