package com.lab06;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ComputerDatabaseSimulation extends Simulation {

    FeederBuilder.Batchable<String> feeder =
            csv("data/search.csv").random();

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://example.com")
            .acceptHeader("text/html")
            .userAgentHeader("Gatling Lab06 Test");

    ScenarioBuilder scenario = scenario("Example Website Load Test with Feeder")
            .feed(feeder)
            .exec(
                    http("Open Example Home Page")
                            .get("/")
                            .check(status().is(200))
            )
            .pause(1)
            .exec(
                    http("Open Example Search Page")
                            .get("/")
                            .queryParam("q", "#{keyword}")
                            .check(status().is(200))
            );

    {
        setUp(
                scenario.injectOpen(
                        rampUsers(20).during(30)
                )
        )
        .protocols(httpProtocol)
        .assertions(
                global().responseTime().mean().lt(1000),
                global().successfulRequests().percent().gt(95.0)
        );
    }
}