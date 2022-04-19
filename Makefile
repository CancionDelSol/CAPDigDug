.DEFAULT_GOAL := generate

SOURCE_DIR := src
BUILD_DIR := src/build
CLASSES_OUTPUT := src/build/classes
JAVA_FILES := $(find . -name '*.java')
NET_STRUCT := 25:7
MUT_RATE := 1.0
MAP_SIZE := 3

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
	@java -jar DigDug.jar -UNITTEST -l DEBUG -rate 1.0 -M 10

train: generate
	@clear
	@java -jar DigDug.jar -GENETICALG -l DEBUG -rate $(MUT_RATE) -EPOCHS 1000 -pop 5 -M $(MAP_SIZE) -N $(NET_STRUCT)

liverun: generate
	@clear
	@java -jar DigDug.jar -LIVERUN -l DEBUG -M $(MAP_SIZE) -N $(NET_STRUCT)

clean:
	@echo "Cleaning up..."
	@rm -r src/build/classes
	@rm src/myManifest
	@rm src/build/*.jar
	@rm -r src/build
	@rm ./*.jar
	
