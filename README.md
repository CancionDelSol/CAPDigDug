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
#  - This will create a new neural network if needed and will start the genetic
#  -  algorithm. This algorithm will save the network periodically for use in
#  -  the live run as explained below

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