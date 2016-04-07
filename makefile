JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default:
	$(JC)  *.java

clean:
	rm *.class

run:
	java Main
