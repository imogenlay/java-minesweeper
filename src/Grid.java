import java.util.Arrays;
import java.util.Random;

public class Grid
{
	//public static final char PIPE_HORIZONTAL = 0x2501;
	public static final String HORIZONTAL_START = " ╺";
	public static final String HORIZONTAL_SEGMENT_FIRST = "╋━━━";
	public static final String HORIZONTAL_SEGMENT_TOP = "╇━━━";
	public static final String HORIZONTAL_SEGMENT_BOTTOM = "╈━━━";
	public static final String HORIZONTAL_END = "╋╸\n";

	private final int width;
	private final int height;
	private final byte[] mineGrid;
	public static final byte GRID_UNKNOWN = 0;
	public static final byte GRID_MINE = 1;
	public static final byte GRID_OUT_OF_BOUNDS = 2;
	public static final byte REVEALED_MIN = 10;
	public static final byte REVEALED_MAX = 19;

	private final char[] dataDefinitions = {
			'░', // Unknown = 0 ░
			'●', // Mine = 1 ●
			'?', // Out of Bounds = 2
			'?', // 3
			'?', // 4
			'?', // 5
			'?', // 6
			'?', // 7
			'?', // 8
			'?', // 9
			// Revealed
			' ', // 0 Mines nearby = 10
			'1', // 1 Mines nearby = 11
			'2', // 2 Mines nearby = 12
			'3', // 3 Mines nearby = 13
			'4', // 4 Mines nearby = 14
			'5', // 5 Mines nearby = 15
			'6', // 6 Mines nearby = 16
			'7', // 7 Mines nearby = 17
			'8', // 8 Mines nearby = 18
			'9', // Revealed Max   = 19
	};

	public Grid(int _width, int _height)
	{
		width = _width;
		height = _height;
		mineGrid = new byte[width * height];
		restart();
	}

	public void restart()
	{
		Random random = new Random();
		for (int i = 0; i < 50; i++)
		{
			int bombX = random.nextInt(width);
			int bombY = random.nextInt(height);
			mineGrid[getIndex(bombX, bombY)] = GRID_MINE;
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
			if (mineGrid[i] == GRID_UNKNOWN)
			{
				winner = false;
				break;
			}

		if (winner)
			System.out.println("WINNER O CLOCK");
	}

	private void revealGrid(int x, int y, boolean force)
	{
		byte value = getValueAtCoordinate(x, y);

		if (value == GRID_OUT_OF_BOUNDS)
			return;

		if (value == GRID_MINE)
		{
			if (force)
			{
				// We only reveal the mine if it was 'forced'.
				// Forcing only occurs on the first click, all
				// recursive functions do not force.
				System.out.println("HIT THE MINE WHOOPSIE");
			}
			return;
		}

		if (value >= REVEALED_MIN && value < REVEALED_MAX)
		{
			// Already revealed.
			return;
		}

		if (value == GRID_UNKNOWN)
		{
			// Reveal self and maybe neighbours.
			byte neighbouringMines = REVEALED_MIN;
			for (int neighbourX = x - 1; neighbourX <= x + 1; neighbourX++)
				for (int neighbourY = y - 1; neighbourY <= y + 1; neighbourY++)
				{
					if (neighbourX == x && neighbourY == y)
						continue;

					byte neighbouringCell = getValueAtCoordinate(neighbourX, neighbourY);
					if (neighbouringCell == GRID_MINE)
						neighbouringMines++;
				}

			setValueAtCoordinate(x, y, neighbouringMines);

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
			return GRID_OUT_OF_BOUNDS;

		return mineGrid[index];
	}

	private void setValueAtCoordinate(int x, int y, byte value)
	{
		int index = getIndex(x, y);
		if (index == -1)
			return;

		mineGrid[index] = value;
	}

	private void addHorizontalLineToStringBuilder(StringBuilder sb, boolean isTop)
	{
		sb.append(HORIZONTAL_START);
		for (int i = 0; i < width; i++)
		{
			if (i == 0)
				sb.append(HORIZONTAL_SEGMENT_FIRST);
			else if (isTop)
				sb.append(HORIZONTAL_SEGMENT_TOP);
			else
				sb.append(HORIZONTAL_SEGMENT_BOTTOM);
		}
		sb.append(HORIZONTAL_END);
	}

	public String printGrid()
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
		addHorizontalLineToStringBuilder(sb, true);

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
				sb.append(dataDefinitions[value]);
				sb.append(" "); // ╂│┼
				if (x + 1 < width)
					sb.append("┼");
			}

			// End row.
			sb.append("┃\n");
		}


		// Add final row.
		addHorizontalLineToStringBuilder(sb, false);
		return sb.toString();
	}


}
