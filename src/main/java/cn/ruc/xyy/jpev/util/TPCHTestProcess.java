package cn.ruc.xyy.jpev.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TPCHTestProcess {
    public TPCHTestProcess() {

    }

    // create extension
    // use this extension
    public List<Double> ExecuteTPCHTest(String name) throws InterruptedException, IOException {
        String shell_path="src/main/script/tpch_test.sh" + " " + name;
        List<Double> result = new ArrayList<Double>();
		    System.out.println("Java Process Builder: execute test script.");
        Process ps = Runtime.getRuntime().exec(shell_path);
        ps.waitFor();

        System.out.println(shell_path);

        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        System.out.println(sb.toString());
        String[] strArr = sb.toString().split(",|\\s");
        for (String s: strArr) {
            System.out.println(s);
            result.add(Double.parseDouble(s));
        }
        System.out.println(result);
        return result;
    }
}
