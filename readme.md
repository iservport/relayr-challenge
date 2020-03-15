# Relayr Challenge

## The Elevator Problem

An elevator control system should be able to handle a few elevators and provide an interface for:

1. querying the state of the elevators,
2. receiving an update about the status of an elevator,
3. receiving a pickup request,
4. time-stepping the simulation.

## Assumptions

1. Elevators should be limited in capacity,
2. Time advances by moving all elevators 1 floor,
3. Doors movements, loading and unloading occur instantaneously.

## Domain model

Three domain classes describe the main context:

1. **Elevator**: keeps the elevator state, including id, floor, direction, content and elevator type.
2. **Trip**: represents a load with source and destination, as well as an optional weight, in case a load limitation should be considered.
3. **Elevator Control System**: receives commands to coordinate how a set of elevators respond to a given demand.

Additionally, an **ElevatorType** is described to set physical characteristics of each elevator.

Events are modelled as follows:

1. **Status**: when information about the current state of all elevators is required,
1. **Pickup**: when a trip is requested
1. **Tick**: when time advances allowing elevators floor by floor.
1. **Quit**: terminates the simulation.

The error model signals problems that may be reported:

1. **ElevatorNotFound**: the given id does not match any elevator,
1. **ElevatorIdNotValid**: the elevator id is not an integer,
1. **ElevatorFloorNotValid**: the floor is not an integer,
1. **ElevatorTickNotValid**: not an integer to time advance,
1. **MaximumWeightExceeded**: pickup request rejected by excess of weight,
1. **MaximumCapacityExceeded**: pickup request rejected by excess capacity.

##Build and run

In order to demonstrate the capabilities of the system, there are three different submodules:

1. **common**: domain model and common classes,
1. **simple**: a naive approach, only to demonstrate and test a system with only one elevator,
1. **effectful**: a purely functional approach.

Scala 2.13.x and sbt 1.3.x are required to build and run each module. Only the **zio** submodule is can be run from sbt: 

    $sbt project zio
    $sbt run
    
To see results from the simple module:

    $sbt project simple
    $sbt test
    
Once one of the runnable submodules is started, it is ready to receive CLI commands (case insensitive):

1. **status | s**, list all elevators,
1. **pickup | p from to \[weight\]**, e.g. "p 0 2 10" will move 10 kg from ground floor to the 2nd floor, if weight is omitted, 0 is assumed,
1. **tick | t \[n\]**, e.g. "t 3" advances time 3 times, if n is omitted, 1 is assumed,

Any other command should halt the system.

## Future improvements

The system can be made sensitive to variations when elevators start, stop, loads or unloads. 

----
Maurício Fernandes de Castro

March 2020




