import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		// symbols start at 0x2500
		//
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Minesweeper!");

		Grid grid = new Grid(12, 12);
		System.out.println(grid.printGrid());
		for (int i = 0; i < 11; i++)
		{
			System.out.println("Please input a coordinate!");
			String input = scanner.nextLine();

			grid.attemptRunCommand(input);
			System.out.println(grid.printGrid());
		}
	}
}