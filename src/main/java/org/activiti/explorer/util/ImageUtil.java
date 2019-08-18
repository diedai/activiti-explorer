 package org.activiti.explorer.util;
 
 import java.awt.image.BufferedImage;
 import java.awt.image.BufferedImageOp;
 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.util.Map;
 import javax.imageio.ImageIO;
 import org.activiti.explorer.Constants;
 import org.imgscalr.Scalr;
 import org.imgscalr.Scalr.Mode;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ImageUtil
 {
   protected static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class.getName());
   
 
 
 
   public static InputStream resizeImage(InputStream imageInputStream, String mimeType, int maxWidth, int maxHeight)
   {
     try
     {
       BufferedImage image = ImageIO.read(imageInputStream);
       
       int width = Math.min(image.getWidth(), maxWidth);
       int height = Math.min(image.getHeight(), maxHeight);
       
       Scalr.Mode mode = Scalr.Mode.AUTOMATIC;
       if (image.getHeight() > maxHeight) {
         mode = Scalr.Mode.FIT_TO_HEIGHT;
       }
       
       if ((width != image.getWidth()) || (height != image.getHeight())) {
         image = Scalr.resize(image, mode, width, height, new BufferedImageOp[0]);
       }
       
       ByteArrayOutputStream bos = new ByteArrayOutputStream();
       ImageIO.write(image, (String)Constants.MIMETYPE_EXTENSION_MAPPING.get(mimeType), bos);
       return new ByteArrayInputStream(bos.toByteArray());
     } catch (IOException e) {
       LOGGER.error("Exception while resizing image", e); }
     return null;
   }
 }


