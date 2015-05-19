/*
 * CS 440/640 Artificial Intelligence
 * Boston University College of Arts and Science
 * 
 * Team : Masaya Ando, Marika Lee, & Jungwong Shin
 * Date : April 27th, 2015
 * 
 * File : littleAsianPlayer.java
 */

import java.util.*;

public class littleAsianPlayer {

	/* 
	 * evaluate() : Evaluates the current board state and returns the next optimal move
	 * @param 	int[][] board, int[]
	 * @returns int[]
	 *
	 * Evaluation Strategy: 
	 * 	1) Permute all possible next move with all three colors 
	 *	2) Given a move in Step 1, test and analyze opponent's best move and return opponent's worst possible move
	 *	3) Given a move in Step 2, check if it will lead to immediate lose 
	 */

	public static int[] evaluate(int[][] board, int[] prevMove) {

		/* Gather Information about Previous Move */
		int c = prevMove[0];
		int x = prevMove[1];
		int y = prevMove[2];
		int z = prevMove[3];

		/* Initialization of Score Keeper for Each Color and Position */
		int[][] scores = initializeScore(); 

		/* Start Step 1 of Evaluation - Permute All Possible Next Move */
		return footDeep(board, x, y, z, scores); 
	}


	/* 
	 * footDeep() : Permutes All Possible Next Move and Passes Scenario to kneeDeep() ('Pseudo-Move')
	 * @param	int[][], int, int, int, int[][]
	 * @returns int[]							
	 */
	public static int[] footDeep(int[][] board, int x, int y, int z, int[][] scores) {

		/* Assign Names to Index for Legibility */
		int topRight = board[x + 1]	[y];
		int midRight = board[x]		[y + 1];
		int botRight = board[x - 1]	[y + 1];
		int botLeft  = board[x - 1]	[y];
		int midLeft  = board[x]		[y - 1];
		int topLeft  = board[x + 1]	[y - 1];

		/* For Each Color, Check if a Move to Neighbor is Valid and Safe */
		for (int color = 1; color <= 3; color++) {		
			/* Check if Board Position is White and is Safe from Producing a Losing Triangle */
			if (isWhite(topRight) && isSafe(board, color, x+1, y)) {
				/* Clone the Current Board State */
				int[][] _board = clone(board);
				/* 'Pseduo-Move' into the Position */
				_board[x+1][y] = color; 
				/* Given the New Board State, Evaluate the Opponent's Possible Move */
				scores[color - 1][0] = kneeDeep(_board, x+1, y); 
			}
			if (isWhite(midRight) && isSafe(board, color, x, y+1)) {
				int[][] _board = clone(board);
				_board[x][y+1] = color; 
				scores[color - 1][1] = kneeDeep(_board, x, y+1); 
			}
			if (isWhite(botRight) && isSafe(board, color, x+1, y+1)) {
				int[][] _board = clone(board);
				_board[x+1][y+1] = color; 
				scores[color - 1][2] = kneeDeep(_board, x+1, y+1); 
			}
			if (isWhite(botLeft) && isSafe(board, color, x-1, y)) {
				int[][] _board = clone(board);
				_board[x-1][y] = color; 
				scores[color - 1][3] = kneeDeep(_board, x-1, y); 
			}
			if (isWhite(midLeft) && isSafe(board, color, x, y-1)) {
				int[][] _board = clone(board);
				_board[x][y-1] = color; 
				scores[color - 1][4] = kneeDeep(_board, x, y-1); 
			}
			if (isWhite(topLeft) && isSafe(board, color, x+1, y-1)) {
				int[][] _board = clone(board);
				_board[x+1][y-1] = color; 
				scores[color - 1][5] = kneeDeep(_board, x+1, y-1); 
			}
		}

		/* Compute the Best Case Score / Maximize Score */
		int maxScore = -1;
		int maxColor = -1;
		int maxPosition = -1;
		int[] move = { maxColor, maxPosition };
		
		for (int color = 1; color < scores.length+1; color++) {
			for (int j = 0; j < scores[color-1].length; j++) {
				int score = scores[color-1][j];
				if (maxScore < score) {
					maxScore = score;
					move[0] = color;
					move[1] = j; 
				}
			}
		}
		return move; 
	}


	/*
	 * kneeDeep() : Given a 'Pseduo-Move' board state, it evaluates the opponent's possible move
	 * @param	int[][], int, int
	 * @returns int
	 */
	public static int kneeDeep(int[][] board, int x, int y) {

		/* Initialization of Score Keeper for Each Color and Position */
		int[][] scores = initializeScore();

		/* Assign Names to Index for Legibility */
		int topRight = board[x + 1][y];
		int midRight = board[x][y + 1];
		int botRight = board[x - 1][y + 1];
		int botLeft  = board[x - 1][y];
		int midLeft  = board[x][y - 1];
		int topLeft  = board[x + 1][y - 1];

		/* For Each Color, Check if a Move to Neighbor is Valid and Safe */
		for (int color = 1; color <= 3; color++) {	
			/* Check if Board Position is White and is Safe from Producing a Losing Triangle */	
			if (isWhite(topRight) && isSafe(board, color, x+1, y)) {
				/* Clone the Current Board State */
				int[][] _board = clone(board);
				/* 'Pseduo-Move' into the Position */
				_board[x+1][y] = color; 
				/* Given the New Board State, Evaluate the Player's Possible Move */
				scores[color - 1][0] = waistDeep(_board, x+1, y); 
			}
			if (isWhite(midRight) && isSafe(board, color, x, y+1)) {
				int[][] _board = clone(board);
				_board[x][y+1] = color; 
				scores[color - 1][1] = waistDeep(_board, x, y+1); 
			}
			if (isWhite(botRight) && isSafe(board, color, x+1, y+1)) {
				int[][] _board = clone(board);
				_board[x+1][y+1] = color; 
				scores[color - 1][2] = waistDeep(_board, x+1, y+1); 
			}
			if (isWhite(botLeft) && isSafe(board, color, x-1, y)) {
				int[][] _board = clone(board);
				_board[x-1][y] = color; 
				scores[color - 1][3] = waistDeep(_board, x-1, y); 
			}
			if (isWhite(midLeft) && isSafe(board, color, x, y-1)) {
				int[][] _board = clone(board);
				_board[x][y-1] = color; 
				scores[color - 1][4] = waistDeep(_board, x, y-1); 
			}
			if (isWhite(topLeft) && isSafe(board, color, x+1, y-1)) {
				int[][] _board = clone(board);
				_board[x+1][y-1] = color; 
				scores[color - 1][5] = waistDeep(_board, x+1, y-1); 
			}
		}

		/* Compute the Worst Case Score / Minimized Score */
		int minScore = 100;
		for (int[] scoreColor : scores) {
			for (int score : scoreColor) {
				if ((score < minScore) && (score > -1)) {
					minScore = score; 
				}
			}
		}

		/* Previous Player Picked the Last Available Circle. Opponent can Play Any Circle */
		if (minScore == 100) {
			return -1; 
		} else {
			return minScore; 
		}
	}


	/*
	 * waistDeep() :  Given a 'Pseduo-Move' board state, it counts the player's number of safe move
	 * @param	int[][], int, int
	 * @returns int 
	 */
	public static int waistDeep(int[][] board, int x, int y) {

		/* Initialize the Number of Safe Moves*/
		int safeMoves = 0; 

		/* Assign Names to Index for Legibility */
		int topRight = board[x + 1][y];
		int midRight = board[x][y + 1];
		int botRight = board[x - 1][y + 1];
		int botLeft  = board[x - 1][y];
		int midLeft  = board[x][y - 1];
		int topLeft  = board[x + 1][y - 1];

		/* For Each Color, Check if Move is Valid and Safe */
		for (int color = 1; color <= 3; color++) {
			/* Check if Board Position is White and is Safe from Producing Losing Triangle */
			if (isWhite(topRight) && isSafe(board, color, x+1, y)) {
				/* Increment # of Safe Move */
				safeMoves++;
			}
			if (isWhite(midRight) && isSafe(board, color, x, y+1)) {
				safeMoves++;
			}
			if (isWhite(botRight) && isSafe(board, color, x-1, y+1)) {
				safeMoves++;
			}
			if (isWhite(botLeft) && isSafe(board, color, x-1, y)) {
				safeMoves++;
			}
			if (isWhite(midLeft) && isSafe(board, color, x, y-1)) {
				safeMoves++;
			}
			if (isWhite(topLeft) && isSafe(board, color, x+1, y-1)) {
				safeMoves++;
			}
		}
		return safeMoves;
	}


	/* 
	 * pickFreeCircle() : Selects an empty circle to play when there are no more possible neighboring move
	 * @param	int[][]
	 * @returns int[]
	*/
	public static int[] pickFreeCircle(int[][] board) {
		boolean found = false; 
		/* Set up the Skeleton for Next Move */
		int[] consolationMove = new int[3];

		/* Look for a Free Circle for Next Move */
		/* Break Out of While Loop, when Free Spot is Found */
		while (!found) {
			for (int i = 1; i < board.length; i++) {
				for (int j = 1; j < board[i].length; j++) {
					for (int color = 1; color <= 3; color++) {
						if (isWhite(board[i][j]) && (isSafe(board, color, i, j))) {
							consolationMove[0] = color; 
							consolationMove[1] = i;
							consolationMove[2] = j;
							found = true; 
  						}
					}
					
				}
			}
		}
		return consolationMove; 
	}


	/* 
	 * clone() : Copies an identical copy of the board
	 * @param	int[][]
	 * @returns int[][]
	 */
	private static int[][] clone(int[][] board){
		/* Set up the Skeleton of the Clone */
		int[][] clone = new int[board.length][board.length];
		/* Copy Values of board into clone */
		for(int i = 0; i < clone.length; i++){
			for(int j = 0; j < clone.length; j++){
				clone[i][j] = board[i][j];
			}
		}
		return clone;
	}


	/* 
	 * initializeScore() : Initializes and returns a new score keeper
	 * @param	void
	 * @returns void
	*/
	private static int[][] initializeScore() {
		/* Set up integer array for scores */
		int[] scoreRed = new int[6];
		int[] scoreGreen = new int[6];
		int[] scoreBlue = new int[6];
		int[][] scores = { scoreRed, scoreGreen, scoreBlue };

		/* Assign -1 for All Scores */
		for (int i = 0; i < scores.length; i++) {
			for (int j = 0; j < scores[i].length; j++) {
				scores[i][j] = -1;
			}
		}
		return scores; 
	}


	/* 
	 * isDifferent() : Compares the colors of two circles
	 * @param	int, int
	 * @returns boolean
	*/
	private static boolean isDifferent(int circle1, int circle2) {
		/* Compare the color of circle1 and circl2 */
		return circle1 != circle2; 
	}


	/* 
	 * isWhite() : Checks if the circle is white, thus playable
	 * @param	int
	 * @returns boolean
	*/
	private static boolean isWhite(int circleColor) {
		/* Check if the color of the circle is white */
		return circleColor == 0; 
	}


	/* 
	 * isSafe() : Checks if a move would produce a losing triangle
	 * @param	int[][]. int, int, int, int
	 * @returns boolean
	*/
	private static boolean isSafe(int[][] board, int color, int x, int y) {
		boolean isSafe = true; 

		/* Set Up Board Indexes. -1 means Unplayable */
		int triangleSum = 6;
		int topRight = -1;
		int midRight = -1;
		int botRight = -1;
		int botLeft  = -1;
		int midLeft  = -1;
		int topLeft  = -1;

		/* Check Array Out of Bound Exception */
		try { topRight = board[x + 1][y]; } 	catch (ArrayIndexOutOfBoundsException e) {} finally {}
		try { midRight = board[x][y + 1]; } 	catch (ArrayIndexOutOfBoundsException e) {} finally {}
		try { botRight = board[x - 1][y + 1]; } catch (ArrayIndexOutOfBoundsException e) {} finally {}
		try { botLeft  = board[x - 1][y]; } 	catch (ArrayIndexOutOfBoundsException e) {} finally {}
		try { midLeft  = board[x][y - 1]; } 	catch (ArrayIndexOutOfBoundsException e) {} finally {}
		try { topLeft  = board[x + 1][y - 1]; } catch (ArrayIndexOutOfBoundsException e) {} finally {}

		/* Check Each Triangle to See if it Produces a Losing Triangle */
		/* Check if the two positions have different colors AND check if the positions are not white */
		if (isDifferent(topRight, midRight) && !isWhite(topRight) && !isWhite(midRight)) {	
			/* Because Red=1, Green=2, and Blue=3, the sum of the losing triangle must be 6,
			 * Hence, if 6 - circle1 - cricle2 == color, the player loses  */		
			if ((triangleSum-topRight-midRight) == color)  {
				isSafe = false; 
			}
		}
		if (isDifferent(midRight, botRight) && !isWhite(midRight) && !isWhite(botRight)) {
			if ((triangleSum-midRight-botRight) == color)  {
				isSafe = false; 
			}
		}
		if (isDifferent(botRight, botLeft) && !isWhite(botRight) && !isWhite(botLeft)) {
			if ((triangleSum-botRight-botLeft) == color)  {
				isSafe = false; 
			}
		}
		if (isDifferent(botLeft, midLeft) && !isWhite(botLeft) && !isWhite(midLeft)) {
			if ((triangleSum-botLeft-midLeft) == color)  {
				isSafe = false; 
			}
		}
		if (isDifferent(midLeft, topLeft) && !isWhite(midLeft) && !isWhite(topLeft)) {
			if ((triangleSum-midLeft-topLeft) == color)  {
				isSafe = false; 
			}
		}
		if (isDifferent(topLeft, topRight) && !isWhite(topLeft) && !isWhite(topRight)) {
			if ((triangleSum-topLeft-topRight) == color)  {
				isSafe = false; 
			}
		}

		return isSafe; 
	}


	/* 
	 * printScore() : Debug function that prints the score keeper
	 * @param	int[][]
	 * @returns void
	*/
	private static void printScore(int[][] scores) {
		for (int i = 0; i < scores.length; i++) {
			     if (i == 0) { System.out.print("Score Red:\t"); } 
			else if (i == 1) { System.out.print("Score Green:\t"); } 
			else if (i == 2) { System.out.print("Score Blue:\t"); }

			System.out.print("{");
			for (int j = 0; j < scores[i].length ; j++) {
				System.out.print(" " +  scores[i][j] + " ");
			}
			System.out.print("}\n");
		}
		System.out.println();	
	}


	/* 
	 * printBoard() : Debug function that beautifully prints the current board state
	 * @param	int[][]
	 * @returns void
	*/
	private static void printBoard(int[][] board) {
		String string = "";
		for (int i = board.length - 1; i >= 0 ; i--) {
			for (int space = 0; space < 2*(i - 1); space++) {
				string += " ";
			}
			if (i == 0) {
				string += "  ";
			}
			string += "[";
			if (i > 0) {
				for (int j = 0; j < board.length - i + 1; j++) {
					string += board[i][j];
					if (j < board.length - i) { 
						string += "   "; 
					}
				}
			} else {
				for (int j = 0; j < board.length - i - 1; j++) {
					string += board[i][j+1];
					if (j < board.length - i - 2) { 
						string += "   "; 
					}
				}
			}
			string += "]\n";
		}
		System.out.println(string);	 
	}


	/* 
	 * parseBoard() : Parses input to obtain information about current board state
	 * @param	String[]
	 * @returns int[][]
	*/
	private static int[][] parseBoard(String[] tokens) {
		/* Gather Information about Board State */
		int size = tokens.length - 1; 
		int index = tokens.length - 2;

		/* Create Current Board State */
		int[][] board = new int[size][size];
		for (int i = 0; i < size; i++) {
			/* Ignore first character of token because it is '[' */
			for (int j = 1; j < tokens[i].length(); j++) {
				board[index - i][j - 1] = Integer.parseInt(Character.toString( tokens[i].charAt(j) ));
			}
		}
		/* Shift the Column of Last Row to Create Atropos Game  */
		for (int i = board[0].length-1; i > 0; i--) {
			board[0][i] = board[0][i - 1];
		}
		/* Top Corner of the Triangle is Not Playable */
		board[0][0] = -1; 

		return board; 
	}


	/* 
	 * parsePrevMove() : Parse input to obtain information about previous move
	 * @param	String
	 * @returns int[]
	*/
	private static int[] parsePrevMove(String prevMoveStr) {
		/* String Manipulation to Remove Unwanted Characters */
		prevMoveStr = prevMoveStr.substring(9);
		prevMoveStr = prevMoveStr.replace("(", "");
		prevMoveStr = prevMoveStr.replace(")", "");
		String[] prevMoveToken = prevMoveStr.split(",");

		/* Store Each Information and Return to main() */
		int prevC = Integer.parseInt(prevMoveToken[0]);
		int prevX = Integer.parseInt(prevMoveToken[1]);
		int prevY = Integer.parseInt(prevMoveToken[2]);
		int prevZ = Integer.parseInt(prevMoveToken[3]);
		
		int[] prevMove = { prevC, prevX, prevY, prevZ };

		return prevMove; 
	} 


	/*	
	 * getNextMove() : Returns the string for next move
	 * @param	int, int[], int[], int[][]
	 * @returns String
	 */
	private static String getNextMove(int size, int[] prevMove, int[] nextMove, int[][] board) {
		String nextMoveStr = "";

		/* Fetch Information about Previous Move */
		int prevX = prevMove[1];
		int prevY = prevMove[2];

		/* Fetch Information about Next Move */
		int newC = nextMove[0];
		int newPosition = nextMove[1];
		int newX = -1;
		int newY = -1;
		int newZ = -1; 

		/* Set Up Board Position of Next Move */
		if (newPosition == 0) {
			newX = prevX + 1; 
			newY = prevY; 
			newZ = size - newX - newY;
		} else if (newPosition == 1) {
			newX = prevX;
			newY = prevY + 1;
			newZ = size - newX - newY;
		} else if (newPosition == 2) {
			newX = prevX - 1;
			newY = prevY + 1; 
			newZ = size - newX - newY;
		} else if (newPosition == 3) {
			newX = prevX - 1;
			newY = prevY; 
			newZ = size - newX - newY;
		} else if (newPosition == 4) {
			newX = prevX;
			newY = prevY - 1; 
			newZ = size - newX - newY;
		} else if (newPosition == 5) {
			newX = prevX + 1;
			newY = prevY - 1; 
			newZ = size - newX - newY;
		} else {
			/* No Empty Neighboring Circles. Pick Any Free Circle. */
			int consolationMove[] = pickFreeCircle(board);
			newC = consolationMove[0];
			newX = consolationMove[1];
			newY = consolationMove[2];
			newZ = size - newX - newY;
		}

		/* Build Next Move Statement */
		nextMoveStr = "(" + newC + "," + newX + "," + newY + "," + newZ + ")";

		return nextMoveStr; 
	}


	public static void main(String[] args) {

		/*  Input: "[boardRow1][boardRow1]...[boardRowN]LastPlay:(c, x, y, z)" 
		 *	 where 
		 *		x = height, y = left, z = right
		 *		c = color where 0 = white, 1 = red, 2 = green, 3 = blue */

		/* Split Input into Tokens Delimited by "]" */
		String[] tokens = args[0].split("]");

		/* Parse Input to Simulate Current Board State */
		int[][] board = parseBoard(tokens); 

		/* Parse Input to Gather Information about Previous Move */
		int size = tokens.length - 1;
		String prevMoveStr = tokens[size];

		/* First Turn of the Game */
		if (prevMoveStr.equals("LastPlay:null")) {
			/* Randomly Pick a Color and a Circle to Play */
			Random r = new Random();
			int c = r.nextInt(2) + 1;
			String h1 = "" + board[1].length; 
			int x = 1; 
			int y = h1.length() / 2; 
			int z = h1.length() - y - 1; 
			String nextMoveStr = "(" + c + "," +  x + "," + y + "," + z + ")";
			System.out.println(nextMoveStr);
			return; 
		} 

		/* Non-First Turn of the Game = Continue Execution */
		int[] prevMove = parsePrevMove(prevMoveStr);

		/* Evaluate the Current Board State and Return Optimal Next Move */
		int[] nextMove = evaluate(board, prevMove);

		/* Gather Information about Next Move */
		String move = getNextMove(size, prevMove, nextMove, board);

		/* Output Next Move */
		System.out.println(move);
		return;
	}
}