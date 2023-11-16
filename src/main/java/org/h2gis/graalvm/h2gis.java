package org.h2gis.graalvm;

import org.h2.jdbc.JdbcSQLException;
import org.h2.util.ScriptReader;
import org.h2gis.functions.factory.H2GISDBFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * First naive H2GIS naive class to test native library with GRAALVM
 * @author Erwan Bocher, CNRS
 */
public class h2gis {

    public static void main(String[] args) throws SQLException, IOException {
        Connection con = H2GISDBFactory.createSpatialDataBase(System.getProperty("java.io.tmpdir")+File.separator+"h2gis");
        Statement stat = con.createStatement();
        stat.execute("DROP TABLE IF EXISTS data; " +
                "CREATE TABLE data as SELECT ST_ACCUM(st_makepoint(-60 + x*random()/500.00, 30 + x*random()/500.00)) FROM GENERATE_SERIES(1, 1000);" +
                "CALL GEOJSONWRITE('/tmp/mydata.geojson', 'data', true);");

        //Parse the script and run it
        if (args.length > 0) {
            String script = args[0];
            File scriptFile = new File(script);
            if (scriptFile.exists()) {
                FileInputStream fis = new FileInputStream(scriptFile);
                ScriptReader scriptReader = new ScriptReader(new InputStreamReader(fis));
                scriptReader.setSkipRemarks(true);
                while (true) {
                    String commandSQL = scriptReader.readStatement();
                    if (commandSQL == null) {
                        break;
                    }
                    if (!commandSQL.isEmpty()) {
                        stat.addBatch(commandSQL);
                    }
                }
            }
            stat.executeBatch();
        }

        con.close();

    }
}
