import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RPLSTest {

  Client c;

  // Initialize a client with only the ip and port fields
  @BeforeEach
  void start(){
    c = new Client(5555, "127.0.0.1", null, null, null,
                   null, null, null, null, null, null, null,
                   null, null, null, null, null, null, null,
                   null, null);
  }

  // Tests to make sure Client class is working
  @Test
  void testClient(){
    assertEquals("Client", c.getClass().getName(), "wrong class");
  }

  // Tests to make sure ip is right
  @Test
  void testIP(){
    assertEquals("127.0.0.1", c.ip, "wrong ip");
  }

  // Tests to make sure port is right
  @Test
  void testPort(){
    assertEquals(5555, c.port, "wrong port");
  }

  // Tests to make sure GameInfo class is working
  @Test
  void testGameInfo(){
    assertEquals("GameInfo", c.game.getClass().getName(), "wrong class");
  }

}
    
