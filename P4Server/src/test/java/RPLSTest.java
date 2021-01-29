import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/*
 * This class will test the classes of this project to see if they are working as intended
 */
class RPLSTest {

        Server s;

        @BeforeEach
        void initialize(){
          s = new Server(null, null, 5555);
          s.server.stop(); 
       }

        // Test to see if Server class works
        @Test
        void testServer() {
          assertEquals("Server", s.getClass().getName(), "wrong class");
        }

        // Test to see if server is getting the right port
        void testPort(){
          assertEquals(5555, s.port, "wrong port");
        }

        // Test to see if GameInfo class works
        @Test
        void testGameInfo(){
          GameInfo g = new GameInfo();
          assertEquals("GameInfo", g.getClass().getName(), "wrong class");
        }

}
