Example Akka-based application integrating with random.org API

Source code ported to Java from the original Scala implementation, https://github.com/nurkiewicz/learning-akka

Make sure to checkout the excellent blog post series from which this example application origins, http://nurkiewicz.blogspot.no/2012/10/your-first-message-discovering-akka.html
Each incremental blog post implementaion has it's own tag in the repository:

1. Step one: Simple Akka messaging implemention with random.org integration
2. Step two: Request-response with java.util.Random implementation
3. Step three: Separate random.org requests to it's own actor, implement random request backlog

To try it out, simply run the application inside Maven:

`mvn exec:java -Dexec.mainClass="com.reuterwall.akka.Main"`