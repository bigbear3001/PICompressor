package com.perhab.pi;

import java.io.IOException;
import java.io.InputStream;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.perhab.math.BBP;
import com.perhab.math.BBP.Mode;

/**
 * PIInputStream gets you a all hexadecimal digits of PI with the help of Bailey�Borwein�Plouffe
 * @see http://en.wikipedia.org/w/index.php?title=Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula&oldid=609165265 
 * @author bigbear3001
 */
@Slf4j
public class PIInputStream extends InputStream {
	/**
	 * Initialize a new PIInputStream returning one hexadecimal digit per {@link #read()}
	 */
	public PIInputStream() {
		this(Mode.SINGLE_DIGIT);
	}
	
	/**
	 * Initialize a new PIInputStream returning the specified digits per {@link #read()}
	 * @param outputMode - mode to define how many digits to return for each read
	 */
	public PIInputStream(@NonNull Mode outputMode) {
		mode = outputMode;
	}
	
	/**
	 * Mode to define how many digits to return for each read
	 */
	private final Mode mode;

	/**
	 * next place to return if read is called again.
	 */
	int nextN = 0;
	
	@Override
	public int read() throws IOException {
		log.trace("Calculate place {}", nextN);
		try {
			return BBP.getPlace(nextN, mode);
		} finally {
			nextN += mode.getReturnDigits();
		}
	}


	
}
