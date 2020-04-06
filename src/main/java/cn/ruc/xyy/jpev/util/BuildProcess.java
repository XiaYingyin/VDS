package cn.ruc.xyy.jpev.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BuildProcess {
    public static String buildProject(String path) {
        String shell_path="src/main/script/build.sh";
        String[] list = {shell_path, path};

        ProcessBuilder processBuilder = new ProcessBuilder(list);

        // Run a shell command
        processBuilder.command(list);
        String result = null;

        StringBuilder output = new StringBuilder();
        StringBuffer errorInfo = new StringBuffer();
        System.out.println("Start process shell script!");
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            while ((line = error.readLine()) != null) {
                errorInfo.append(
                        line + "\n"
                );
            }
            System.out.println("Waiting for the process!");
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                result = output.toString();
            } else {
                result = errorInfo.toString();
                //abnormal...
                System.out.println(output);
                System.out.println("Error: " + exitVal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String installExtension(String path, String name) {
        String shell_path="src/main/script/install.sh";
        ProcessBuilder processBuilder = new ProcessBuilder(shell_path, path, name);

        // Run a shell command
        //processBuilder.command(shell_path);
        String result = null;

        StringBuilder output = new StringBuilder();
        System.out.println("Start process shell script!");
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            System.out.println("Waiting for the process!");
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                result = output.toString();
            } else {
                result = "Install failed!\n";
                //abnormal...
                System.out.println(output);
                System.out.println("Error: " + exitVal);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
