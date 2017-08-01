package io.micrometer.jettysample;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloHandler extends AbstractHandler {
  final String greeting;
  final String body;
  private final MeterRegistry registry;

  public HelloHandler(MeterRegistry registry) {
    this("Hello World", registry);
  }

  public HelloHandler(String greeting, MeterRegistry registry) {
    this(greeting, null, registry);
  }

  public HelloHandler(String greeting, String body, MeterRegistry registry) {
    this.greeting = greeting;
    this.body = body;
    this.registry = registry;
  }


  public void handle(String target,
                     Request baseRequest,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException,
          ServletException {
    if (!target.equalsIgnoreCase("/")) {
      registry.counter("hello_view", "handled", "false").increment();
      return;
    }
    registry.counter("hello_view", "handled", "true").increment();

    response.setContentType("text/html; charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);

    PrintWriter out = response.getWriter();

    out.println("<h1>" + greeting + "</h1>");
    if (body != null) {
      out.println(body);
    }

    baseRequest.setHandled(true);
  }
}
