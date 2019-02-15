package render;

import org.lwjgl.BufferUtils;
import org.lwjgl.cuda.CUuuid;
import org.lwjgl.opengl.GL46.*;
import org.joml.*;

import java.io.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int program;
    private int vs;
    private int fs;

    public Shader(String filename) {
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename+".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename+".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "textures");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
        }
        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.err.println(glGetProgramInfoLog(program));
        }
        glValidateProgram(program);

    }

    public void setUniform(String name, int value) {
        int location = glGetUniformLocation(program, name);
        if(location != -1)
            glUniform1i(location, value);
    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if(location != -1)
            glUniformMatrix4fv(location, false, buffer);
    }


    public void bind() {
        glUseProgram(program);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(new File("./shaders/" + filename)))){
            String line;
            while( (line = br.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return string.toString();
    }

}
