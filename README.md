# H2GIS-GRAALVM
H2GIS-GRAALVM test

A project to test H2GIS native library compilation with GRAALVM 

Commands to build the native lib :


1. mvn clean package


2. mvn -Pnative -Dagent exec:exec@java-agent


3. mvn -Pnative -Dagent package

4. Go to the target folder in the source code and run ./h2gis [path to a sql script]



./target/h2gis /tmp/db /tmp/script.sql