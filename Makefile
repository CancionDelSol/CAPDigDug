.DEFAULT_GOAL := generate

SOURCE_DIR := src
BUILD_DIR := src/build
CLASSES_OUTPUT := src/build/classes
PACKAGE_OUTPUT := src/package

package:
	@mkdir -p $(PACKAGE_OUTPUT)

classpath:
	@mkdir -p $(CLASSES_OUTPUT)

buildpath:
	@mkdir -p $(BUILD_DIR)

copyfiles:
	@cp src/*.java $(PACKAGE_OUTPUT)

javacbuild: package classpath buildpath copyfiles
	@echo "Creating classes"
	@javac -sourcepath src -d $(CLASSES_OUTPUT) $(PACKAGE_OUTPUT)/*.java

createmanifest: javacbuild
	@echo "Writing manifest"
	@echo Main-Class: Program>src/myManifest

createjar: createmanifest
	@echo "Creating jar"
	@jar cfm src/build/ConnectM.jar src/myManifest -C src/build/classes/ .
	@cp src/build/ConnectM.jar ConnectM.jar

generate: createjar
	@echo "Complete: Run make run to execute a standard Connect 4 game"

run: generate
	@java -jar ConnectM.jar 10 4 1

clean:
	@echo "Cleaning up..."
	@rm -r src/build/classes
	@rm src/myManifest
	@rm -r src/package
	@rm ./*.jar
	
