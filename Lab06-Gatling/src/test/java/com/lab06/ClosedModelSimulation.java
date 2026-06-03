package com.lab06;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ClosedModelSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://example.com")
            .acceptHeader("text/html")
            .userAgentHeader("Gatling Closed Model Test");

    ScenarioBuilder scenario = scenario("Closed Model Load Test")
            .exec(
                    http("Open Home Page")
                            .get("/")
                            .check(status().is(200))
            )
            .pause(1);

    {
        setUp(
                scenario.injectClosed(
                        constantConcurrentUsers(10).during(30)
                )
        )
        .protocols(httpProtocol)
        .assertions(
                global().successfulRequests().percent().gt(95.0),
                global().responseTime().mean().lt(1000)
        );
    }
}