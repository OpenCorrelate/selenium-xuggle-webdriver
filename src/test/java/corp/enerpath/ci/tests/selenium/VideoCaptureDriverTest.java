package corp.enerpath.ci.tests.selenium;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.opencorrelate.ci.selenium.VideoCaptureDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class VideoCaptureDriverTest {

	private final String TEST_NAME = "test-videocapturedriver";
	
	private final String OUTPUT_ID = ( System.getProperty("test.video.output.id") != null) ? 
			String.format("$s-%s" , TEST_NAME, System.getProperty("test.video.output.id")) : TEST_NAME;
	
	private final String OUTPUT_FILENAME =  ( System.getProperty("test.video.output.type") != null) ? 
			String.format("%s.%s" , OUTPUT_ID, System.getProperty("test.video.output.type")) : String.format("%s.%s", TEST_NAME, "mov");
			
	private final File path = new File(getClass().getResource("/").getFile());
	private final File output = new File(path, OUTPUT_FILENAME);
	
	@Before public void setUp() {
		if (output.exists())
			output.delete();
		
	}
	
	@Test public void testVideoCaptureDriver() throws Exception {
		assertFalse(output.exists());
		WebDriver driver = new VideoCaptureDriver(new FirefoxDriver(), output);
		assertNotNull(driver);
		driver.get("http://www.google.com");
		driver.get("http://www.yahoo.com");
		driver.quit();
		assertTrue(output.exists());
	}
}
