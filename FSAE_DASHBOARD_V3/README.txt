It is an eclipse project, you could load it directly from eclipse. If you want
to load it by compiler, then go to src folder, type

$find . -name \*.class -type f -delete
$javac -target 1.8 -source 1.8 Control/Main.java
$java Control.Main


If you are linux system, you can write a bash for above commands.
Note that you need to install JDK instead of JRE only since this is source code,
not .class bytecode.  Any question could tell me on slack.

This is the first draft, later will improve. Welcome any suggestions and ideas.


