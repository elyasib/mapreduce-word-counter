MapReduce Word Counter
=========================

This is a MapReduce for word counting implementation. 

The algorithm was designed using the Actor Model with aid of the Akka toolkit. Everithing in the code is written to be non-blocking. The actor systems uses a combination of priority mailboxes and message counting in order to guarantee that every message in the system is processed before it is shutt-down, and guarantee that the last message processed is the request for the final result.

This is algorithm can be easily used in a distributed system with a small number of changes/additions in the configuration files.
