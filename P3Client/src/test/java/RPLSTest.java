import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.function.Consumer;

class RPLSTest {

	protected Consumer<Serializable> callback;
	Client client = new Client(callback);

	@Test
	void testIp(){
		String newIP = "123.123.123.123";
		client.changeIp(newIP);
		assertEquals(client.ip, newIP);
	}

	@Test
	void testPort(){
		int newPort = 6669;
		client.changePort(newPort);
		assertEquals(client.port, newPort);
	}

	@Test
	void testGetPoints(){
		client.p1Points = 3;
		int points = client.getP1Points();
		assertEquals(points, 3);
	}

	@Test
	void testGetPoints2(){
		client.p2Points = 3;
		int points = client.getP2Points();
		assertEquals(points, 3);
	}

	@Test
	void testSend(){
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();	// set system for redirect the stream
		System.setOut(new PrintStream(outContent));						// redirect the stream
		client.sendTest("hello");
		assertEquals("Sent: hello"+System.getProperty("line.separator"), outContent.toString());
	}

}
