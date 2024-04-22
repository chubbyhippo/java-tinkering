# new 
```
java -XX:StartFlightRecording=duration=60s filename=myrecording.jfr
```
# running 
### find <pid>
```
jcmd
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
# reading jfr
https://github.com/openjdk/jmc
```
jmc
```

