package pe.edu.vallegrande.RestLosPinos.util;

import net.sf.jasperreports.engine.JasperCompileManager;
import java.io.File;

public class JasperCompiler {
    public static void main(String[] args) throws Exception {
        // Solo compilar ListDate y Subreporte1
        String[] jrxmlFiles = {
            "src/main/resources/reports/ListDate.jrxml",
            "src/main/resources/reports/Subreporte1.jrxml"
        };
        for (String jrxmlPath : jrxmlFiles) {
            File jrxml = new File(jrxmlPath);
            if (jrxml.exists()) {
                String jasperPath = jrxmlPath.replace(".jrxml", ".jasper");
                JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
                System.out.println("Compilado: " + jrxmlPath + " -> " + jasperPath);
            } else {
                System.out.println("No encontrado: " + jrxmlPath);
            }
        }
        System.out.println("Compilación finalizada.");
    }
} 