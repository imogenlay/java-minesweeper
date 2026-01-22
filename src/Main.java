import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Minesweeper!");
		System.out.println("What dimensions would you like the board?");

		Grid grid = new Grid(12, 12);
		System.out.println(grid);

		while (true)
		{
			System.out.println("Please input a coordinate:");
			String input = scanner.nextLine();

			grid.attemptRunCommand(input);
			System.out.println(grid);

			byte gameState = grid.getGameState();
			if (gameState == Const.PLAYING)
				continue;

			if (gameState == Const.WIN)
				System.out.println("HOORAY! You are the winner!");
			else
				System.out.println("You lose!");

			System.out.println("Do you want to play again?");
			input = scanner.nextLine();
			if (input.contains("y"))
				continue;

			break;
		}

		System.out.println("Thank you for playing!");
		scanner.close();
	}
}