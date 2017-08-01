package io.micrometer.jettysample;

import io.micrometer.core.instrument.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PrometheusHandler extends AbstractHandler{
  private final PrometheusMeterRegistry registry;


  public PrometheusHandler(PrometheusMeterRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void handle(String target,
                     Request baseRequest,
                     HttpServletRequest request,
                     HttpServletResponse response) throws IOException,
          ServletException {
    if(!target.equalsIgnoreCase("/metrics")){
      return;
    }

    response.setContentType(TextFormat.CONTENT_TYPE_004);
    response.setStatus(HttpServletResponse.SC_OK);

    response.getWriter().append(registry.scrape());

    baseRequest.setHandled(true);
  }
}
