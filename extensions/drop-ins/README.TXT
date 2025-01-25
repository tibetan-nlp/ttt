Developers:

The Ant build file, Jskad/build.xml, looks in this directory for JARs.
Any found are added to the classpath during an Ant run.  Example: put
the JAR for a regexp library in here, and you can then change, say,
org/thdl/tib/input/Jskad.java to use it, and your build will work
fine.

DC
