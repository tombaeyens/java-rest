package be.tombaeyens.rest.handler;


import be.tombaeyens.rest.HttpServletMethod;
import be.tombaeyens.rest.RestHandler;
import be.tombaeyens.rest.RestRequestContext;
import be.tombaeyens.rest.parameter.StringParameter;



/**
 * @author Tom Baeyens
 */
public class ItemHandler extends RestHandler {

  public HttpServletMethod getMethod() {
    return HttpServletMethod.GET;
  }

  public String getUrlPattern() {
    return "/item/{itemId}";
  }

  protected StringParameter itemIdParameter = (StringParameter) new StringParameter()
    .setUrlVariable()
    .setName("itemId") 
    .setDescription("the oid of the case")
    .setMaxLength(20);

  public void handle(RestRequestContext restRequestContext) {
    String caseOid = itemIdParameter.get(restRequestContext);
    
    // Item item = .. retrieve item...;
    Object item= null;
    
    // convert to json
    String jsonText = null;

    restRequestContext.sendResponseJsonText(jsonText);
  }
}
