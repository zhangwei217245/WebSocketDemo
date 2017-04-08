
First, run the program using the following command

```
mvn clean package
cd target/bin
./WebSocket-demo -ip 127.0.0.1 -port 8080
```

Open your web browser, open this link:  http://www.websocket.org/echo.html
 
And if you see "This web browser support WebSocket", which means you can test
this websocket on this web page now. 

In the location part, input
ws://localhost:8080/message-endpoint

In the message box, type:

Test WebSocket

Press send button, you should be able to see your message in your console. 

In the message box, type:

GET

Press send button, you should be able to see the random messages sent by your
java program to the web browser. 
