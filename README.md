# City simulation game


Peontopia is a city builder with some elements of classic 4X games mixed in and far more focus on
victory, survival and challenge rather than building your dream city at a leisurely pace.

## Code layout

* The model package contains stateful, mutable models of all the components of the simulation with
  no intelligence.
* The limits package contains stateless constants about the simulation, such as number of workers
  in a factory of a specific type, the number of simulation steps in a single day, etc.
* The analysis package contains code that tries to predict outcomes. This analysis ties into the
  simulation in different ways. The market anaysis is used to determine the open market price for
  various goods, for example.
* The actors package contain actions that the individual actors can take, like travel, eating or
  declaring bankrupcy.
* The simulation package contains code that ties things together. There are simulations for
  different actor types like peons and factories that determine what actions (eat, sleep, work, etc)
  that they should take.
