# jfr 
```
jfr
```
# running 
# new
```
java -XX:StartFlightRecording=filename=myrecording.jfr,dumponexit=true App.java
```
```
java -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr
```
### find <pid>
```
jcmd
```
### list all commands
```
jcmd <pid> help
```
### start
```
jcmd <pid> JFR.start duration=60s filename=myrecording.jfr
```
### stop
```
jcmd <pid> JFR.stop
```
### check
```
jcmd <pid> JFR.check
```
### dump
```
jcmd <pid> JFR.dump
```
# reading jfr
https://github.com/openjdk/jmc
```
jmc
```

