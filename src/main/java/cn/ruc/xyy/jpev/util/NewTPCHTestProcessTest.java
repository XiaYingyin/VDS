package cn.ruc.xyy.jpev.util;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewTPCHTestProcessTest {

    @Test
    void executeTPCHTest() {
        NewTPCHTestProcess nttp = new NewTPCHTestProcess();

        List<Double> result = nttp.ExecuteTPCHTest("pg_strom");

        for (Double d: result) {
            System.out.println(d);
        }
    }
}