
package io.netty.example.test.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.example.http.websocketx.client.WebSocketClient;
import io.netty.example.http.websocketx.server.WebSocketServer;

public class NettyLeakTest {

  private static final Logger logger = LoggerFactory.getLogger(NettyLeakTest.class);

  private static final String URL = System.getProperty("url", "ws://127.0.0.1:8080/websocket");

  private WebSocketServer server;

  @Before
  public void setup() {

    server = new WebSocketServer();
    try {
      server.start();
    } catch (Exception e) {
      logger.error("Exception starting WS server");
    }
  }

  @After
  public void teardown() {
    try {
      server.stop();
    } catch (InterruptedException e) {
      logger.error("Exception stopping WS server");
    }
  }

  @Test
  public void test() throws Exception {

    long count = 0;
    WebSocketClient client;
    while (true) {
      logger.info("Starting loop on iteration {}", ++count);
      client = new WebSocketClient();
      client.startClient(URL);
      client.send(count);
      // Give some time to free resources
      Thread.sleep(20);
      client.cleanResources();
      client = null;
    }
  }
}
