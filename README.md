# RouterSimulator
CSE214 Homework #4

Randomly generates packets which are sent through three levels of routers: dispatcher, intermediate, and destination. Generated packets are sent to a dispatcher which are then sent to an intermediate router with the least buffer. In each simulation time unit, the first packet in each intermediate router is processed and sent to the destination. The amount of time required to process the packets depends on the packet size. The simulation keeps track of the total number of packets served, total service time, average service time of each packet, and total number of packets dropped.
