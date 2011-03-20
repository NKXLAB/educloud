import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Receiver implements Runnable {
	// ------------------------------ FIELDS ------------------------------

	/**
	 * stream to receive data from child
	 */
	private final InputStream is;

	// -------------------------- PUBLIC INSTANCE METHODS
	// --------------------------

	/**
	 * method invoked when Receiver thread started. Reads data from child and
	 * displays in on System.out.
	 */
	public void run() {
		try {
			System.out.println("aguardando...");
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					is), 50 /* keep small for testing */);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException receiving data from child process.");
		}
	}

	// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * contructor
	 *
	 * @param is
	 *            stream to receive data from child
	 */
	public Receiver(InputStream is) {
		this.is = is;
	}
}
