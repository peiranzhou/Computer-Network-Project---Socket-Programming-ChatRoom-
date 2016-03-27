JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ClientReadThread.java \
	ClientWriteThread.java \
	OfflineMessage.java \
        mainServer.java \
	Client.java \
	Server.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class