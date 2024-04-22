# new 
```
java -XX:StartFlightRecording=duration=60s filename=myrecording.jfr
```
# running 
### find pid
```
jcmd
```
### start
```
jcmd <pid> JFR.start duration=60s filename=myrecording.jfr
```
