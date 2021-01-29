import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.function.Consumer;


class RPLSTest {

	// Testing methods, not threads -> this will cause the threads to spit errors
	// obviously because no client is connected, and callback is always null;
	private Consumer<Serializable> callback;
	GameInfo game = new GameInfo(callback);
	GameLogic gameLogic = new GameLogic();

	@BeforeEach
	void setup(){
		game.p2Points=0;
		game.p1Points=0;
		game.lastWinner=0;
	}

	@Test
	void testPort(){
		// test if the port input is right
		game.changePort(5555);
		assertEquals(game.port, 5555);
		game.changePort(1122);
		assertEquals(game.port, 1122);
	}

	@Test
	void testPlayerHands(){
		// test if the player hands working
		game.p1Hand = "rock";
		game.p2Hand = "rock";
		assertEquals(game.p1Hand, game.p2Hand);
	}

	@Test
	void testDeclareWinner(){
		// test if the method updates a single client
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();	// set system for redirect the stream
		System.setOut(new PrintStream(outContent));						// redirect the stream
		String c = "Player 1 wins.";
		String b = "Player 1 has 1 points.";
		gameLogic.declareWinner(1);
		assertEquals(c+System.getProperty("line.separator")+b+System.getProperty("line.separator"),outContent.toString());
	}

	@Test
	void testCheckWinner(){
		// test if the method updates two clients
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();	// set system for redirect the stream
		System.setOut(new PrintStream(outContent));						// redirect the stream
		String c = "Player 1 wins the game!";
		String b = "Player 2 wins the game!";

		int p1 = 3;
		int p2 = 2;
		gameLogic.checkGameWinner(p1,p2);
		assertEquals(c+System.getProperty("line.separator"), outContent.toString());
		p2 = 3;
		p1 = 1;
		gameLogic.checkGameWinner(p1,p2);
		assertEquals(c+System.getProperty("line.separator")+b+System.getProperty("line.separator"), outContent.toString());
	}

	@Test
	void testWhoWon(){
		// test the function that determines winner
		String player1 = "paper";
		String player2 = "rock";
		int result = gameLogic.whoWon(player1, player2);
		assertEquals(result, 1);	// player 1 should be the winner
	}



}
