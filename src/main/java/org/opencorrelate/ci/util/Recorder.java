package org.opencorrelate.ci.util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;

/**
 * 
 * @author Presley H. Cannady, Jr. <revprez@opencorrelate.org>
 *
 */
public class Recorder implements Callable<IMediaWriter> {

	private static final double FRAME_RATE = 50;
	
	private final Robot robot;
	private final IMediaWriter writer;
	private final Rectangle screenBounds;
	private static volatile boolean exit = false;
	
	public Recorder(Robot robot, IMediaWriter writer, Rectangle screenBounds) {
		this.robot = robot;
		this.writer = writer;
		this.screenBounds = screenBounds;
		
	}

	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			exit = true;
		}
	}
	
	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {

        BufferedImage image;

        // if the source image is already the target type, return the source image
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        // otherwise create a new image of the target type and draw the new image
        else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;

    }

	@Override
	public IMediaWriter call() throws Exception {
		long startTime = System.nanoTime();
		int i = 0;
		while (!Thread.currentThread().isInterrupted() && !exit) {
			BufferedImage screen = convertToType(robot.createScreenCapture(screenBounds), BufferedImage.TYPE_3BYTE_BGR);
			writer.encodeVideo(0, screen, System.nanoTime()-startTime, TimeUnit.NANOSECONDS);	
			sleep((long)(1000/FRAME_RATE));
			i++;
		}
		System.out.println(String.format("Encoded %d frames %s.  Exiting...", i, writer.getUrl()));
		return writer;
	}

}
