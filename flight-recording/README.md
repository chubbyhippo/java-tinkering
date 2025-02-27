# jfr 
```shell
jfr
```
# running 
# new
```shell
java -XX:StartFlightRecording=filename=myrecording.jfr,dumponexit=true App.java
```
```shell
java -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr -jar app.jar
```
### find <pid>
```shell
jcmd
```
### list all commands
```shell
jcmd <pid> help
```
### start
```shell
jcmd <pid> JFR.start duration=60s filename=myrecording.jfr
```
### stop
```shell
jcmd <pid> JFR.stop
```
### check
```shell
jcmd <pid> JFR.check
```
### dump
```shell
jcmd <pid> JFR.dump
```
# reading jfr
https://github.com/openjdk/jmc
```shell
jmc
```

