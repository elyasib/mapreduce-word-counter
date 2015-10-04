MapReduce Word Counter
=========================

This is a MapReduce for word counting implementation. 

The algorithm was designed using the Actor Model with aid of the Akka toolkit. Everything in the code is written to be non-blocking. This actor system uses a combination of priority mailboxes and message counting in order to guarantee (try the best it can?) that every message in the system is processed before it is shut-down, and also to guarantee that the last message processed is the request for the final result.

This algorithm can be easily used in a distributed system with a small number of changes/additions in the configuration files.

##Pending...
- Supervision strategies have yet to be implemented to improve the resilience of the system
- Add tests
