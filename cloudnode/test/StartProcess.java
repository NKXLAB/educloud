import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class StartProcess {

	public static void main(String args[]) throws IOException {
		String line = "C:\\Arquivos de programas\\Oracle\\VirtualBox\\vboxwebsrv.exe";
		InputStream stderr = null;
		InputStream stdout = null;

		// launch EXE and grab stdin/stdout and stderr
		File dir1 = new File (".");
		String canonicalPath = dir1.getCanonicalPath();
		canonicalPath += "\\webservice.log";
		Process process = Runtime.getRuntime().exec(new String[] {line, "-F", canonicalPath},
		        null,
		        new File("C:\\Arquivos de programas\\Oracle\\VirtualBox"));
		stderr = process.getErrorStream();
		stdout = process.getInputStream();

		new Thread(new Receiver(stdout)).start();
		new Thread(new Receiver(stderr)).start();


		try {
			int waitFor = process.waitFor();
		} catch (InterruptedException e) {
		}
	}

}
