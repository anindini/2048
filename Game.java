import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class Game {
	Scanner scan = new Scanner(System.in);
	Random random = new Random();
	public int [][] board = new int [4][4];	//4X4 2-D Array
	int moves; // Counts valid moves
	boolean quit = false;	// Will be true if user quits or restarts
	boolean win = false;	// Will be true if user wins the game at maximum number on board = 2048
	boolean ignore; // Will be true if user inputs a meaningless key
	public Game() {	//constructor
		int prob1 = random.nextInt(100);
		int prob2 = random.nextInt(100);
		int newNumber1;	//randomly generated value to put on board
		int newNumber2;	//randomly generated value to put on board
		if (prob1 < 80) {	// 80% chance of a 2
			newNumber1 = 2;
		} else {			// 20% change of a 4
			newNumber1 = 4;
		}
		if (prob2 < 80) {
			newNumber2 = 2;
		} else {
			newNumber2 = 4;
		}
		int x1 = random.nextInt(4);
		int y1 = random.nextInt(4);
		int x2 = random.nextInt(4);
		int y2 = random.nextInt(4);
		for (int i = 0; i < 4; i ++) {	// Fills the board with zeros
			for (int j = 0; j < 4; j++) {
				board[i][j] = 0;
			}
		}
		while (x1 == x2 && y1 == y2) {	// If the positions generated are the same, create new coordinates
			x2 = random.nextInt(4);
			y2 = random.nextInt(4);
		}
		board[x1][y1] = newNumber1;	// Add the tiles to the board
		board[x2][y2] = newNumber2;
		System.out.println("Welcome to 2048!");
		System.out.println("Use the 'a', 'd', 'w', and 's' keys to move left, right, up, and down respectively.");
		System.out.println("Press 'q' to quit or 'r' to restart.");
		moves = 0;
	}
	public void play() {	// Runs a turn
		printBoard();
		int [][] clone = new int [board.length][] ;	//clone is a copy of the board, used to check validity of moves
		for (int i = 0; i < board.length; i++) {
			clone[i] = Arrays.copyOf(board[i], board[i].length);
		}
		if (max() == 2048) {
			win = true;
		} else {
			char direction = scan.next().charAt(0);
			System.out.println("Key: " + direction);
			switch (direction) {
			case 'a':	//cases for moving in left, right, up, and down directions
				ignore = false;
				left();
				break;
			case 'd':
				ignore = false;
				right();
				break;
			case 'w':
				ignore = false;
				up();
				break;
			case 's':
				ignore = false;
				down();
				break;
			case 'q':	// If user wants to quit game
				System.out.println("Are you sure you want to quit? Press 'q' again to confirm.");
				char confirmq = scan.next().charAt(0);
				if (confirmq == 'q') {	// Checks for confirmation
					System.out.println("You have quit!");
					quit = true;	// Game ends
				}
				break;
			case 'r':	// If user wants to restart
				ignore = false;
				System.out.println("Are you sure you want to restart? Press 'r' again to confirm.");
				char confirmr = scan.next().charAt(0);
				if (confirmr == 'r') {	// Checks for confirmation
					System.out.println("You have restarted the game!");
					quit = true;				// Current game ends
					Game restart = new Game();	// New game started
					while (restart.moreMoves()) {
						restart.play();
					}
				}
				break;
			default:	// Invalid keys get ignored
				System.out.println("Not a valid key");
				ignore = true;
				break;
			}
			boolean compare = true;	//clone is compared to board before new number is generated, and 
			for (int a = 0; a < board.length; a++) {	//if board is different from clone, moves is incremented
				for (int b = 0; b < board[a].length; b++) {
					if (board[a][b] != clone[a][b]) {
						compare = false;
					}
				}
			}
			if (!quit && !ignore) {
				if (!compare) {	//if board is different from the clone, a new number is randomly generated on board
					int prob = random.nextInt(100);
					int newNumber;
					if (prob < 80) {
						newNumber = 2;
					} else {
						newNumber = 4;
					}
					int x = random.nextInt(4);
					int y = random.nextInt(4);
					while (board[x][y] != 0) {
						x = random.nextInt(4);
						y = random.nextInt(4);
					}
					board[x][y] = newNumber;
					moves++;
					System.out.println("Valid move");
				} else {
					System.out.println("Not valid move");
				}
				System.out.println(moves + " moves so far");
				System.out.println("Maximum number: " + max());
			}
		}
	}
	public void left() {	//for moving left
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < 4; i++) {
				int count = 1;
				while (board[i][j] == 0 && count < (3 - j)) {	//nearest empty space to the right is swapped with board[i][j]
					board[i][j] = board[i][j + count];
					board[i][j + count] = 0;
					count++;
				}
				if (board[i][j] == 0) {	// If board[i][j] is still empty, farthest right tile is swapped
					board[i][j] = board[i][j + count];
					board[i][j + count] = 0;
				} else {
					int x = 0;
					while (x == 0 && count < (4 - j)) {	// Find the nearest nonzero tile to the right
						x = board[i][j + count];
						count++;
					}
					if (x == board[i][j]) {	//if x is equivalent to the value of board[i][j], double the 
						board[i][j] = 2*x;	//value at board[i][j], and make other value 0
						board[i][j + count - 1] = 0;
					}
				}
			}
		}
	}
	public void right() {	
		for (int j = 3; j > 0; j--) {
			for (int i = 0; i < 4; i++) {
				int count = 1;
				while (board[i][j] == 0 && count < j) {
					board[i][j] = board[i][j - count];	//nearest empty space to the left is swapped with board[i] [j]
					board[i][j-count] = 0;
					count++;
				}
				if (board[i][j] == 0) {	// If board[i][j] is still empty, farthest left tile is swapped
					board[i][j] = board[i][j - count];
					board[i][j - count] = 0;
				} else {
					int x = 0;
					while (x == 0 && count < (j + 1)) {
						x = board[i][j - count];	// Find the nearest nonzero tile to the left
						count++;
					}
					if (x == board[i][j]) {	//if x is equivalent to the value of board[i][j], double the 
						board[i][j] = 2*x;	//value at board[i][j], and make other value 0
						board[i][j - (count - 1)] = 0;
					}
				}
			}
		}
	}
	public void up() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				int count = 1;
				while (board[i][j] == 0 && count < (3 - i)) {	//nearest empty space down is swapped with board[i][j]
					board[i][j] = board[i + count][j];
					board[i + count][j] = 0;
					count++;
				}
				if (board[i][j] == 0) {	// If board[i][j] is still empty, farthest down tile is swapped
					board[i][j] = board[i + count][j];
					board[i + count][j] = 0;
				} else {
					int x = 0;
					while (x == 0 && count < (4 - i)) {
						x = board[i + count][j];	// Find the nearest nonzero tile below
						count++;
					}
					if (x == board[i][j]) {
						board[i][j] = 2*x;	//if x is equivalent to the value of board[i][j], double the 
						board[i + count - 1][j] = 0;	//value at board[i][j], and make other value 0
					}
				}
			}
		}
	}
	public void down() {
		for (int i = 3; i > 0; i--) {
			for (int j = 0; j < 4; j++) {
				int count = 1;
				while (board[i][j] == 0 && count < i) {	//nearest empty space up is swapped with board[i][j]
					board[i][j] = board[i - count][j];
					board[i - count][j] = 0;
					count++;
				}
				if (board[i][j] == 0) {	// If board[i][j] is still empty, farthest up tile is swapped
					board[i][j] = board[i - count][j];
					board[i - count][j] = 0;
				} else {
					int x = 0;
					while (x == 0 && count < (i + 1)) {
						x = board[i - count][j];	// Find the nearest nonzero tile above
						count++;
					}
					if (x == board[i][j]) {
						board[i][j] = 2*x;	//if x is equivalent to the value of board[i][j], double the 
						board[i - (count - 1)][j] = 0;	//value at board[i][j], and make other value 0
					}
				}
			}
		}
		
	}
	public boolean moreMoves() {	//checks whether game should continue
		if (quit) {	
			return false;
		}
		if (win) {
			System.out.println("Congratulations!!! You won the game!");
			System.out.println("Maximum number: 2048");
			System.out.println("Total number of moves: " + moves);
			return false;
		}
		for (int i = 0; i < 4; i++) {	//checks whether any empty spots are available
			for (int j = 0; j < 4; j++) {
				if (board[i][j] == 0) {
					return true;
				}
			}
		}
		for (int i = 0; i < 3; i++) {	//checks whether numbers can be combined to twice the value
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == board[i + 1][j] || board[i][j] == board[i][j + 1]) {
					return true;
				}
			}
			if (board[i][3] == board[i + 1][3]) {
				return true;
			}
		}
		return false;
	}
	public int max() {	//calculates the maximum number on the board
		int max = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i][j] > max) {
					max = board[i][j];
				}
			}
		}
		return max;
	}
	public void printBoard() {	//prints the board
		System.out.println("-      -      -      -      -      -");
		for (int [] i : board) {
			System.out.print("|      ");
			for (int j = 0; j < 4; j++) {
				if (i[j] != 0) {
					System.out.format("%-7d", i[j]);
				} else {
					System.out.print("*      ");
				}
			}
			System.out.println("|");
		}
		System.out.println("-      -      -      -      -      -");
		System.out.println();
	}
}



