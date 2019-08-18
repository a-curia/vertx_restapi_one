package com.dbbyte;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import sun.security.provider.certpath.Vertex;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Vertx vertx = Vertx.vertx();


        // create the HTTP Server
        HttpServer httpServer = vertx.createHttpServer(); // this will hold the instance of the HTTP Server

        // create the routers
        Router router = Router.router(vertx); // like a Controller

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type","text/plain");
            response.end("message back to the caller");
        });

        router.route("/hello").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type","text/plain");
            response.end("message back for hello path");
        });

        // chunks for multiple handlers
        Route handler1 = router.route("/chunked").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("First Chunked of data\n");

            routingContext.vertx().setTimer(5000, tid -> {
                routingContext.next();
            });
        });
        Route handler2 = router.route("/chunked").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.write("Second Chunked of data\n");

            routingContext.vertx().setTimer(10000, tid -> {
                routingContext.next();
            });
        });
        Route handler3 = router.route("/chunked").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.end("End Response");
        });



        // set the port to listen to
        httpServer.requestHandler(router).listen(9090);

    }
}
