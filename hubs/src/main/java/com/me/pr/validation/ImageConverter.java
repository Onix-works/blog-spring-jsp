package com.me.pr.validation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.me.pr.controllers.BlogController;

public  class ImageConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageConverter.class);
	
	 public static BufferedImage createImageFromBytes(byte[] imageData) {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    try {
	        return ImageIO.read(bais);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, 
			int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    
	    return resizedImage;
	}
	
	public static Blob resizeBytesImage(byte[] fileContent, int targetWidth, 
			int targetHeight) throws IOException, SQLException {

		BufferedImage image = createImageFromBytes(fileContent);
	   	image = resizeImage(image, targetWidth, targetHeight);
	   	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
	   	return new SerialBlob(fileContent);
	   	}
}
