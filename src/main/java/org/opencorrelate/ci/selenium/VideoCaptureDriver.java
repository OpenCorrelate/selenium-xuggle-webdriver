package org.opencorrelate.ci.selenium;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opencorrelate.ci.util.Recorder;
import org.openqa.selenium.WebDriver;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;


/**
 * 
 * @author Presley H. Cannady, Jr. <revprez@opencorrelate.org>
 *
 */
public class VideoCaptureDriver extends TestWebDriver {

	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final Future<IMediaWriter> result;
	private final File output;
	
	public VideoCaptureDriver(WebDriver driver, File output) {
		super(driver);
		
		this.output = output;
		
		try {
			Robot robot = new Robot();
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Rectangle screenBounds = new Rectangle(toolkit.getScreenSize());
			
			IMediaWriter writer = ToolFactory.makeWriter(output.getAbsolutePath()); 
			writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, screenBounds.width, screenBounds.height);
			
			Recorder recorder = new Recorder(robot,writer,screenBounds);
			result = executor.submit(recorder);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override public void close() {
		try {
			if (!executor.isShutdown()) {
				executor.shutdownNow();
				result.get().close();
				System.out.println("Finished recording");
			}
		} catch (Exception e) {
			if (output.exists())
				output.delete();
			throw new RuntimeException(e);
		} finally {
			getDelegateDriver().close();
		}
	}
	
	@Override public void quit() {
		try {
			if (!executor.isShutdown()) {
				executor.shutdownNow();
				result.get().close();
				System.out.println("Finished recording");
			}
		} catch (Exception e) {
			if (output.exists())
				output.delete();
			System.out.println(String.format("Failed to save recording at %s", output.getAbsolutePath()));
			throw new RuntimeException(e);
		} finally {
			getDelegateDriver().quit();
		}
	}
	
	@Override public WebDriver getDelegateDriver() {
		if (super.getDelegateDriver() instanceof TestWebDriver)
			return ((TestWebDriver)super.getDelegateDriver()).getDelegateDriver();
		
		return super.getDelegateDriver();
	}

}
