public class Const
{
	// Grid design.
	public static final String HORIZONTAL_START = " ╺";
	public static final String HORIZONTAL_SEGMENT_FIRST = "╋━━━";
	public static final String HORIZONTAL_SEGMENT_TOP = "╇━━━";
	public static final String HORIZONTAL_SEGMENT_BOTTOM = "╈━━━";
	public static final String HORIZONTAL_END = "╋╸\n";

	// Grid data types.
	public static final byte GRID_UNKNOWN = 0;
	public static final byte GRID_MINE = 1;
	public static final byte GRID_OUT_OF_BOUNDS = 2;
	public static final byte REVEALED_MIN = 10;
	public static final byte REVEALED_MAX = 19;

	// Grid display characters.
	public static final char[] dataDefinitions = {
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
}
