package org.columba.ristretto.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import junit.framework.TestCase;

public class VariableSizeFileBufferTest extends TestCase {

	public void testOneBlock() throws IOException {
		VariableSizeFileBuffer buffer = new VariableSizeFileBuffer();
		
		InputStream in = new VariableSizeFileBufferInputStream(buffer);
		OutputStream out = new VariableSizeFileBufferOutputStream(buffer);
		
		byte[] test = new byte[1000];
		Random rand = new Random();
		rand.nextBytes(test);
		
		// First write some stuff
		out.write(test);
		
		assertEquals(buffer.getSize(), test.length);
		
		// Read it back
		for( int i=0; i<test.length; i++) {
			assertEquals((byte)in.read(), test[i]);
		}
		
		// Must be eof
		assertEquals(-1, in.read());
	}

	public void testMoreBlocks() throws IOException {
		VariableSizeFileBuffer buffer = new VariableSizeFileBuffer();
		
		InputStream in = new VariableSizeFileBufferInputStream(buffer);
		OutputStream out = new VariableSizeFileBufferOutputStream(buffer);
		
		byte[] test = new byte[600000];
		Random rand = new Random();
		rand.nextBytes(test);
		
		// First write some stuff
		out.write(test);
		
		assertEquals(buffer.getSize(), test.length);
		
		// Read it back
		for( int i=0; i<test.length; i++) {
			assertEquals((byte)in.read(), test[i]);
		}
		
		// Must be eof
		assertEquals(-1, in.read());
	}
	
	
	public void testInterleaved() throws IOException {
		VariableSizeFileBuffer buffer = new VariableSizeFileBuffer();
		
		InputStream in = new VariableSizeFileBufferInputStream(buffer);
		OutputStream out = new VariableSizeFileBufferOutputStream(buffer);
		
		byte[] test = new byte[1000];
		Random rand = new Random();
		
		for( int j=1; j <= 1000; j++) {
		rand.nextBytes(test);
		
		// First write some stuff
		out.write(test);
		
		assertEquals(buffer.getSize(), test.length * j);
		
		// Read it back
		for( int i=0; i<test.length; i++) {
			assertEquals((byte)in.read(), test[i]);
		}

		// Must be eof
		assertEquals(-1, in.read());
		}
	}
	
	
	

	
}
