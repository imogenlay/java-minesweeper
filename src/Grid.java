import java.util.Arrays;
import java.util.Random;

public class Grid
{
	private final int width;
	private final int height;
	private final byte[] mineGrid;
	private byte gameState = Const.PLAYING;

	public Grid(int _width, int _height)
	{
		width = _width;
		height = _height;
		mineGrid = new byte[width * height];
		restart();
	}

	public byte getGameState() { return gameState; }

	public void restart()
	{
		Random random = new Random();
		for (int i = 0; i < 50; i++)
		{
			int bombX = random.nextInt(width);
			int bombY = random.nextInt(height);
			mineGrid[getIndex(bombX, bombY)] = Const.GRID_MINE;
		}
	}

	public int getIndex(int x, int y)
	{
		if (x < 0 || y < 0 || x >= width || y >= height)
			return -1;
		int index = y * width + x;
		return index;
	}

	public void attemptRunCommand(String command)
	{
		String[] commandParts = command.trim().split("\\s+");
		if (commandParts.length != 2)
		{
			System.out.println("Could not read command. Only add two numbers separated by a space.");
			return;
		}

		int x, y;
		int[] out = new int[1];
		if (Utils.tryParseInt(commandParts[0], out))
			x = out[0];
		else
		{
			System.out.println("Could not read x value. Only add two numbers separated by a space.");
			return;
		}

		if (Utils.tryParseInt(commandParts[1], out))
			y = out[0];
		else
		{
			System.out.println("Could not read y value. Only add two numbers separated by a space.");
			return;
		}

		revealGrid(x, y, true);

		// Scan grid to see if we have a winner!
		boolean winner = true;
		for (int i = 0; i < mineGrid.length; i++)
			if (mineGrid[i] == Const.GRID_UNKNOWN)
			{
				winner = false;
				break;
			}

		if (winner)
			gameState = Const.WIN;
	}

	private void revealGrid(int x, int y, boolean force)
	{
		byte value = getValueAtCoordinate(x, y);

		if (value == Const.GRID_OUT_OF_BOUNDS)
			return;

		if (value == Const.GRID_MINE)
		{
			if (force)
			{
				// We only reveal the mine if it was 'forced'.
				// Forcing only occurs on the first click, all
				// recursive functions do not force.
				gameState = Const.LOSE;
			}
			return;
		}

		if (value >= Const.REVEALED_MIN && value < Const.REVEALED_MAX)
		{
			// Already revealed.
			return;
		}

		if (value == Const.GRID_UNKNOWN)
		{
			// Check neighbours for mines.
			byte neighbouringMines = Const.REVEALED_MIN;
			for (int neighbourX = x - 1; neighbourX <= x + 1; neighbourX++)
				for (int neighbourY = y - 1; neighbourY <= y + 1; neighbourY++)
				{
					if (neighbourX == x && neighbourY == y)
						continue;

					byte neighbouringCell = getValueAtCoordinate(neighbourX, neighbourY);
					if (neighbouringCell == Const.GRID_MINE)
						neighbouringMines++;
				}

			setValueAtCoordinate(x, y, neighbouringMines);

			// Try reveal neighbours. These will not force any
			// reveal on mines.
			revealGrid(x - 1, y + 0, false);
			revealGrid(x + 0, y - 1, false);
			revealGrid(x + 1, y + 0, false);
			revealGrid(x + 0, y + 1, false);
		}
	}

	private byte getValueAtCoordinate(int x, int y)
	{
		int index = getIndex(x, y);
		if (index == -1)
			// Out of bounds.
			return Const.GRID_OUT_OF_BOUNDS;

		return mineGrid[index];
	}

	private void setValueAtCoordinate(int x, int y, byte value)
	{
		int index = getIndex(x, y);
		if (index == -1)
			return;

		mineGrid[index] = value;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(" ");
		for (int x = 0; x < width; x++)
		{
			// Add the row of numbers across the top.
			// Check to see if the number will take up
			// one or two characters.
			if (x > 10)
				sb.append("  " + x); // Needs two spaces.
			else
				sb.append("   " + x); // Needs three spaces.
		}
		sb.append('\n');
		Utils.addHorizontalLineToStringBuilder(sb, width, true);

		for (int y = 0; y < height; y++)
		{
			// Add the number at the beginning of a row.
			sb.append(y);
			if (y < 10)
				sb.append(" ");
			sb.append("┃");

			for (int x = 0; x < width; x++)
			{
				// Add each grid item.
				byte value = mineGrid[getIndex(x, y)];

				sb.append(" ");
				sb.append(Const.dataDefinitions[value]);
				sb.append(" "); // ╂│┼
				if (x + 1 < width)
					sb.append("┼");
			}

			// End row.
			sb.append("┃\n");
		}

		// Add final row.
		Utils.addHorizontalLineToStringBuilder(sb, width, false);
		return sb.toString();
	}
}
