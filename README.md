# spring-batch-file-encryptor

## Technologies
* Spring Boot 
* Spring Batch

## Install
mvn install

## Run
Command line arguments
* numberOfThreads (1 - 20)
* pathToInputFile

mvn spring-boot:run -Dspring-boot.run.arguments=--numberOfThreads={noThread},--pathToInputFile={path}

e.g.:
mvn spring-boot:run -Dspring-boot.run.arguments=--numberOfThreads=5,--pathToInputFile=src/main/resources/sample.txt

##Output
./result/output-*.txt

## Logging
If time permitted, would add logging and unit tests  