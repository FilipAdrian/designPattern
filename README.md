# Design Patterns

## Description
The main goal in this LW is to create an App with with the use Design Patterns. 

## Design Paterns Used:
	- Factory Method,
	- Singleton,
	- Template method.
	Factory and Singleton Methods are Creational Design Patterns which are about class instantiation. This patterns can be further divided into class-creation patterns and object-creational patterns. While class-creation patterns use inheritance effectively in the instantiation process, object-creation patterns use delegation effectively to get the job done.
## Factory Method - 
	is to creating objects as Template Method is to implementing an algorithm. A superclass specifies all standard and generic behavior, and then delegates the creation details to subclasses that are supplied by the client. Makes a design more customizable and only a little more complicated. Is often used as the standard way to create objects; but it isn't necessary if: the class that's instantiated never changes, or instantiation takes place in an operation that subclasses can easily override. Are routinely specified by an architectural framework, and then implemented by the user of the framework.

```SQL
public class Factory {
    public Player GetPlayer(PlayerType type) {
        switch (type) {
            case Citizen: {
                return new Citizen();
            }
            case Mafia: {
                return new Mafia();
            }
            case Cop: {
                return new Cop();
            }
            default: {
                throw new RuntimeException();
            }
        }
    }
}
```
##Singleton Method - 
	make the class of the single instance object responsible for creation, initialization, access, and enforcement. Provide a public static member function that encapsulates all initialization code, and provides access to the instance. The client calls the accessor function whenever a reference to the single instance is required.

```SQL
 private TcpMultiServer() {
    }


    public static TcpMultiServer getInstance() {
        if (singleTcpServerInstance == null) {
            singleTcpServerInstance = new TcpMultiServer();
        }
        return singleTcpServerInstance;
    }
```
##Template method - 
	is a Behavioral Design Patterns, which are about Class's objects communication. Behavioral patterns are those patterns that are most specifically concerned with communication between objects. 
	Define the skeleton of an algorithm in an operation, deferring some steps to client subclasses. Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.
	Base class declares algorithm 'placeholders', and derived classes implement the placeholders.

```SQL
abstract boolean choose();
```