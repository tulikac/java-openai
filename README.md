# OpenAI with Java
The project demonstrate the integration with Azure OpenAI text-davinci-003 model, Azure Storage Queue using JAVA. The purpose of this repository is to provide a reference point with the Azure OpenAI "text-davinci-003" model POC as an example to build a java webjob or for an independent local run.
## Pre-requisite
- Azure Open AI service created and text-davinci-003 model deployed
- Have a Azure Storage source and destination queues created
- Acquire the below config information and have the needed environment variables setup

## How to Run
- Install platform specific Java 17 onwards, e.g. for windows https://learn.microsoft.com/en-us/java/openjdk/download
- Make sure JAVA_HOME environment variable is configured
- Install mvn(maven) https://maven.apache.org/download.cgi

## Build
- Clone the repo locally
- Navigate to the cloned directory
- run "mvn compile" then "mvn package"
- Running "mnv package" would generate the jar file "openapi-javaapp-1.0.0.jar"
## Running jar locally
- Have these environment variable configured that contains the configuration for the azure open AI, azure storage queue
  #### AZURE_STORAGE_CONNECTION_STRING  
  #### AZURE_STORAGE_SOURCE_QUEUE_NAME
  #### AZURE_STORAGE_DESTINATION_QUEUE_NAME
  #### AZURE_OPEN_API_KEY
  #### AZURE_OPEN_API_ENDPOINT
  #### AZURE_OPEN_API_MODEL_DEPLOYMENT_NAME

- Run the jar

## Running As WebJob in Azure
Currently EastUSEUAP2 has the code deployed that enable running webjobs on Linux. 
 - Create a Java Linux container webapp, please make sure the container, custom or blessed image based have Java 17 + in it and JAVA_HOME environment varialbel configured
 - Have the above mentioned config environment varialbes configured as app settings
 - Please zip these files "openai-java-webjob-start.sh", "settings.job" and "openapi-javaapp-1.0.0.jar". And deploy the zip file as webjob
