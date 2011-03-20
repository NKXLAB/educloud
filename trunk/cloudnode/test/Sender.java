import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Sender implements Runnable {
	// ------------------------------ CONSTANTS ------------------------------

	/**
	 * e.g. \n \r\n or \r, whatever system uses to separate lines in a text
	 * file. Only used inside multiline fields. The file itself should use
	 * Windows format \r \n, though \n by itself will alsolineSeparator work.
	 */
	private static final String lineSeparator = System
			.getProperty("line.separator");

	// ------------------------------ FIELDS ------------------------------

	/**
	 * stream to send output to child on
	 */
	private final OutputStream os;

	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------

	/**
	 * method invoked when Sender thread started. Feeds dummy data to child.
	 */
	public void run() {
		try {
			final BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(os), 50 /* keep small for tests */);
			for (int i = 99; i >= 0; i--) {
				bw.write("There are " + i + " bottles of beer on the wall, "
						+ i + " bottles of beer.");
				bw.write(lineSeparator);
			}
			bw.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException sending data to child process.");
		}
	}

	// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * constructor
	 *
	 * @param os
	 *            stream to use to send data to child.
	 */
	Sender(OutputStream os) {
		this.os = os;
	}
}