# Tower of Hanoi

The aim of game is to move the disks from the source rod to the destiny rod by the order in a top with smallest disk and bottom with biggest size. To expanding the program, first, add four buttons in the main class located in the North panel. The usage of if statement in the actionPerformed method helps to illustrate what happen when clicking on different button.The doneSolving shows what happen when ending the solving.

In the solver class, by using the recursion, call moveTower method twice within the moveTower method but with  different parameters, and call the moveDisk which located in the main class for finishing the whole steps in the moveTower method. 

Additionally, added three methods: fasterDisk, slowerDisk, stopDisk which are called in the main class under the method of 'actionPerformed'.
