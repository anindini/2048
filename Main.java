public class Main {
public static void main(String [] args) {
	Game game = new Game();	// Initializes game
	while (game.moreMoves()) {
		game.play();	// Keeps turns running
	}	
   }
}




