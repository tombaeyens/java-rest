package be.tombaeyens.rest;

import javax.servlet.http.HttpServletResponse;


/**
 * @author Tom Baeyens
 */
public class BadRequestException extends RestException {

  private static final long serialVersionUID = 1L;

  public BadRequestException(String message) {
    super(HttpServletResponse.SC_BAD_REQUEST, message);
  }

}
