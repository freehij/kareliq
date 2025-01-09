how to build:
1. `git clone https://github.com/realfreehij/kareliq.git`
2. go to project root directory and use `gradlew build`
3. after building ends the .jar will appear in `build/libs` as `kareliq-1.0-SNAPSHOT-all.jar` (other one is not needed)

how to use:
1. open your instance settings and find `Java arguments`
2. add `-javaagent:path/to/your/jar` to the field
3. save settings and launch instance as normal
