.DEFAULT_GOAL := generate

# Source 
SOURCE_DIR := src

# Build
BUILD_DIR := src/build

# Classes
CLASSES_OUTPUT := src/build/classes

# Java
JAVA_FILES := $(find . -name '*.java')

# Network structure, will default to one 
#  hidden layer with dimension 5
#  This is the smallest network I've 
#  had decent results with after training
#  for an afternoon
NET_STRUCT := 25:25

# Mutation rate for genetic algorithm
#  This represents the range allowed 
#  for a random adjustment to a parameter
MUT_RATE := .5

# Size of the map
#  Defaults to 3
MAP_SIZE := 18

# Population size
POP_SIZE := 10

# Epochs per loop
#  A new set of training worlds
#  are generated between loops
EPs := 10

# Logging level
LOG_LEVEL := DEBUG

# Command line arguments for the live run and training session program types
CLI_ARGS := -l $(LOG_LEVEL) -rate $(MUT_RATE) -e $(EPs) -pop $(POP_SIZE) -m $(MAP_SIZE) -n $(NET_STRUCT)

classpath:
	@mkdir -p $(CLASSES_OUTPUT)

buildpath:
	@mkdir -p $(BUILD_DIR)

javacbuild: classpath buildpath
	@echo "Creating classes"
	@javac -sourcepath src -d $(CLASSES_OUTPUT) $(SOURCE_DIR)/**/*.java $(SOURCE_DIR)/*.java

createmanifest: javacbuild
	@echo "Writing manifest"
	@echo Main-Class: Program>src/myManifest

createjar: createmanifest
	@echo "Creating jar"
	@jar cfm src/build/DigDug.jar src/myManifest -C src/build/classes/ .
	@cp src/build/DigDug.jar DigDug.jar

generate: createjar
	@echo "Complete"

run: generate
	@clear
	@java -jar DigDug.jar

debug: generate
	@clear
	@java -jar DigDug.jar -UNITTEST

train: generate
	@clear
	@java -jar DigDug.jar -GENETICALG $(CLI_ARGS)

liverun: generate
	@clear
	@java -jar DigDug.jar -LIVERUN $(CLI_ARGS)

clean:
	@echo "Cleaning up..."
	@rm -r src/build/classes
	@rm src/myManifest
	@rm src/build/*.jar
	@rm -r src/build
	@rm ./*.jar
	
