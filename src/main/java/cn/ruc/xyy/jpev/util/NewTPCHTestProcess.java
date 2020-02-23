package cn.ruc.xyy.jpev.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NewTPCHTestProcess {
    public List<Double> ExecuteTPCHTest(String name) {
        String shell_path="src/main/script/tpch_test.sh";
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Run a shell command
        processBuilder.command(shell_path);
        List<Double> result = new ArrayList<Double>();

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
                String[] strArr = output.toString().split("\\s|,");
                for (String s: strArr) {
                    System.out.println(s);
                    result.add(Double.parseDouble(s));
                }
            } else {
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
