import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Welcome to Minesweeper!");
		int x, y;
		String input;
		Grid grid;
		
		Scanner scanner = new Scanner(System.in);
		boolean continuePlaying = true;

		while (continuePlaying)
		{
			System.out.println("What dimensions would you like the board? Minimum: 8x8");
			input = scanner.nextLine();
			int[] coord = Utils.parseCoordInput(input);
			x = coord[0];
			y = coord[1];
			if (x == -1 || y == -1)
				continue;
			if (x < 8 || y < 8)
			{
				System.out.println("Minimum size is 8 by 8.");
				continue;
			}
			x = Math.min(x, 40);
			y = Math.min(y, 40);

			grid = new Grid(x, y);
			System.out.println(grid);

			while (continuePlaying)
			{
				System.out.println("Please input a coordinate:");
				input = scanner.nextLine();

				grid.attemptRunCommand(input);
				System.out.println(grid);

				byte gameState = grid.getGameState();
				if (gameState == Const.PLAYING)
					continue;

				if (gameState == Const.WIN)
					System.out.println("HOORAY! You are the winner!");
				else
					System.out.println("You hit a mine! You lose!");

				System.out.println("Do you want to play again?");
				input = scanner.nextLine();
				if (input.contains("y"))
					break;

				continuePlaying = false;
			}
		}

		System.out.println("Thank you for playing!");
		scanner.close();
	}
}