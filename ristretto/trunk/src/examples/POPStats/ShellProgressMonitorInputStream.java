package POPStats;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ShellProgressMonitorInputStream class
 * @author timo
 *
 */
public class ShellProgressMonitorInputStream extends FilterInputStream {

	private static final int TOTAL_STEPS = 20;

	int total;
	int position;
	int stepsize;
	int printed;

	public ShellProgressMonitorInputStream(InputStream in) {
		super(in);

		try {
			total = in.available();
			stepsize = total / TOTAL_STEPS;
		} catch (IOException e) {
			total = -1;
			stepsize = 5000; // bytes
		}
	}

	@Override
	public int read() throws IOException {
		int result = super.read();
		incPosition(1);
		return result;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = super.read(b, off, len);
		incPosition(len);
		return result;
	}

	private void incPosition(int s) {
		position += s;
		int shouldBePrinted = position % stepsize;

		while (shouldBePrinted > printed) {
			System.out.print('#');
			printed++;
		}
	}

	@Override
	public void close() throws IOException {
		System.out.println(".");
		super.close();
	}
}
