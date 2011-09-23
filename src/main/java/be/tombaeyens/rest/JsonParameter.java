package be.tombaeyens.rest;

/**
 * @author Tom Baeyens
 */
public class JsonParameter <T> extends Parameter <T> {

  public JsonParameter(Class<T> type) {
    super(type);
  }

  @SuppressWarnings("unchecked")
  public T convert(String parameterValue) {
//    try {
//      JsonConverter jsonConverter = pangu.getJsonConverter();
//      Object jsonObject = jsonConverter.parseJsonText(parameterValue);
//      T persistable = (T) jsonConverter.toBean(jsonObject, type);
//      return persistable;
//    } catch (Exception e) {
//      throw new RestException("couldn't create "+type.getName()+" based on json "+parameterValue, e);
//    }
    
    return null;
  }

  public String getTypeDescription() {
    return "json "+type.getName();
  }
}
