package render;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL46.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Texture {
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        BufferedImage bi;
        try {
            // Import image and read to a buffered image
            bi = ImageIO.read(new File("./textures/"+filename));
            width = bi.getWidth();
            height = bi.getHeight();

            // Get the RGB values of every pixel in the image
            int[] pixels_raw;
            pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

            // This is where the pixels are stored/saved to later
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

            // Put pixel data from buffered image into byte buffer
            for(int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i * width + j];
                    pixels.put((byte)(pixel >> 16 & 0xFF)); //RED       This is being set to a RGBA color model
                    pixels.put((byte)(pixel >> 8 & 0xFF));  //GREEN
                    pixels.put((byte)(pixel & 0xFF));       //BLUE
                    pixels.put((byte)(pixel >> 24 & 0xFF)); //ALPHA
                }
            }

            // You have to flip from read to write for some reason
            pixels.flip();

            // Ask OpenGL for an address for texture (must be placed by OpenGL because data in a Java
            // program moves around because of garbage collection, etc.)
            id = glGenTextures();

            // Set OpenGl's focus on that address
            glBindTexture(GL_TEXTURE_2D, id);

            // Sets filters for texture, aligns subpixel values with nearest pixels
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            // Actually add the image to the texture id
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

            // Disable Texture??
        }

        catch(IOException ioe) {
            ioe.printStackTrace();
        }

    }


    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

}
