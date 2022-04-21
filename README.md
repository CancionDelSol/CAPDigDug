# CAPDigDug
# Roger Johnson

# Usage:
#  - There are three different runtimes for this application
#  - The default setup is sufficient for seeing quick results,
#  -  however better Agent performance can be acheived by 
#  -  modifying the network structure to be larger and running
#  -  the training algorithm for a long period of time. Results
#  -  of the algorithm can be viewed in real time, as will
#  -  be explained below

# To run the test suite enter the following into the command line:
#  - "make debug"
#  -  This will run the unit tests to ensure everything is working as expected.

# To run the training algorithm enter the following into the command line:
#  - "make train"
#  - This will create a new neural network if one is not already
#  -  present on disk and will start the genetic
#  -  algorithm. This algorithm will save the network periodically for use in
#  -  the live run as explained below. This will run until the defined minimum
#  -  error is met and will then run the live display below. In practice, a really
#  -  low threshold will effectively make the training continue for a long period of time

# To run the current network in real time enter the following into the command line:
#  - "make liverun"
#  - This will run the current network on disk in a randomly generated world
#  -  After the game completes, the network will be reloaded from disk and
#  -  a new world will be generated, allowing for real-time viewing of the
#  -  training algorithm's output. This will run until the user ends the process

# CLI Arguments:
#  There are preset arguments placed into the Makefile for each mode. 
#   The applications configuration is most easily modified through the Makefile
#   But if you would like to play around with the settings, all values in the Settings.java
#   file can be modified, although i haven't bug-tested everything quite yet. I would avoid
#   changing the Agent's Field of View to too large of a number, as this will most likely 
#   lead to very long computation times. 

# Gameboard visuals
# Here are the tiles and their visualizations:
#  "&" : Enemy tile
#  " " : Empty tile
#  "^" : Player tile
#  "*" : Coin tile
#  "#" : Dirt tile
#  "@" : Rock tile

# Some lessons learned and gotchas
#  - Migrating all constants and configurations into the Settings.java file
#     helped immensly when debugging. I've had to tweak and retweak the configuration
#     quite a bit to get some useful results
#  - Be very careful about how the heuristic (error function) is defined. I've noticed
#     that having too much reward for killing an enemy led to the Agent sitting in one
#     spot and waiting for an enemy to walk by. All in all, the Agent will optimize the error 
#     function EXACTLY as it is defined, of course.
#  - Can never have enough unit tests

# Things I would like to change:
#  - I would like to make it so that the enemy tiles can occupy the same 
#     space as another tile. This is how the game functions in the real DigDug
#  - I would like to swap to using a Recurrent Neural Network (Preferebly LSTM) and
#     see if I get better results.
#  - I would implement the UserPlay where the user can play the game alongside the Agent
#     I realized towards the end that implementing this would take another few days due 
#     to how complicated it would be so i opted to leave it out. Attempting to set the CLI flag
#     will throw an error during program execution with a message to notify the user.