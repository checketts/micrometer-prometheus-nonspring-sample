package io.micrometer.jettysample;

import io.micrometer.core.instrument.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.jetty.JettyStatisticsCollector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;

public class JettySampleApplication {

  public static void main(String[] args) {

    Server server = new Server(8080);
    HandlerCollection handlers = new HandlerCollection();

    StatisticsHandler statisticsHandler = new StatisticsHandler();
    statisticsHandler.setHandler(handlers);
    statisticsHandler.setServer(server);
    server.setHandler(statisticsHandler);

    PrometheusMeterRegistry registry = new PrometheusMeterRegistry(CollectorRegistry.defaultRegistry);
    handlers.addHandler(new PrometheusHandler(registry.getPrometheusRegistry()));
    handlers.addHandler(new HelloHandler(registry));

    //TODO instrument via micrometer:
    new JettyStatisticsCollector(statisticsHandler).register();

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
