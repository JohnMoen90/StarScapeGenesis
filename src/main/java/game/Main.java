package game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import io.Timer;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.TileRenderer;

public class Main {

    public Main() {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));


        if (!glfwInit()) {
            System.err.println("GLFW Failed to initialize");
            System.exit(1);
        }

        Window win = new Window();
        win.setSize(1280, 768);
        win.setFullscreen(false);
        win.createWindow("Game");

        // Creates context
        GL.createCapabilities();

        Camera camera = new Camera(640, 480);

        glEnable(GL_TEXTURE_2D);

        TileRenderer tiles = new TileRenderer();

        Shader shader = new Shader("shader");

        Texture tex = new Texture("test.png");

        Matrix4f scale = new Matrix4f().scale(32);
        Matrix4f target = new Matrix4f();

        camera.setPosition(new Vector3f(0,0,0));

        double frame_cap = 1.0/60.0;

        double frame_time = 0;
        int frames = 0;

        double time = Timer.getTime();
        double unprocessed = 0;

        // game.Main Loop
        while(!win.shouldClose()) {
            boolean can_render = false;

            double time_2 = Timer.getTime();
            double passed = time_2 - time;
            unprocessed += passed;
            frame_time += passed;

            time = time_2;

            while(unprocessed >= frame_cap) {
                unprocessed -= frame_cap;
                can_render = true;

                target = scale;
                if (win.getInput().isKeyPressed(GLFW_KEY_ESCAPE))  {
                    glfwSetWindowShouldClose(win.getWindow(), true);
                }

                win.update();   // Check for events
                if (frame_time >= 1.0) {
                    frame_time = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            if (can_render) {
                frames++;

                glClear(GL_COLOR_BUFFER_BIT);

//                shader.bind();
//                shader.setUniform("sampler", 0);
//                shader.setUniform("projection", camera.getProjection().mul(target));
//                tex.bind(0);
//                model.render();

                tiles.renderTile((byte)0, 0, 0, shader, target, camera);


                win.swapBuffer();
            }
        }


        glfwTerminate();

    }


    public static void main(String[] args) {
        new Main();
    }

}
