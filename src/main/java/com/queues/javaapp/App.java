package com.queues.javaapp;

import com.azure.storage.queue.*;

//import com.queues.javaapp.AiDocSummarizationProcessor;

/*
    Creating jar file
    Creating a Java jar file using Azure Storage Queue package using maven

    C:\repo>mvn compile

    C:\repo>mvn package

    Seeing the content of the Jar file:
    C:\repo\target>jar -tf target\queues-javaapp-1.0-0.jar

    Run the jar
    C:\repo>java -jar target\queues-javaapp-1.0-0.jar
 */

public class App {

    public static void main(String[] args) {
        try {

            AppConfigs appSettingConfigs = new AppConfigs();

            appSettingConfigs.azureOpenAiKey =  "";
            appSettingConfigs.openAiEndpoint = "";
            appSettingConfigs.aiModelDeploymentName =  "";

            appSettingConfigs.azureStorageConnectionString =  "";
            appSettingConfigs.sourceQueueName =  "";
            appSettingConfigs.destinationQueueName =  "";

            // Get the config from environment variables
            EnvConfigProvider.ConfigValues(appSettingConfigs);

            // Build the source queue client, in this case the question queue
            QueueClient queueClient = new QueueClientBuilder()
                    .connectionString(appSettingConfigs.azureStorageConnectionString)
                    .queueName(appSettingConfigs.sourceQueueName)
                    .buildClient();

            // Build the destination QueueMessageSender, in this case the answer queue
            QueueMessageSender queueMessageSender = new QueueMessageSender(
                    appSettingConfigs.azureStorageConnectionString,
                    appSettingConfigs.destinationQueueName);

            //AiDocSummarizationProcessor aiDocSummarizer = new AiDocSummarizationProcessor();

            // Process the received messages through the OpenAI model
            queueClient.receiveMessages(10).forEach(
                    // "Process" the message
                    receivedMessage -> {
                        String input = receivedMessage.getBody().toString();
                        System.out.println("Input: " + input);

                        // Process the received input through the AI model
                        //String aiModelOutput = openAiProcessor.processPrompt(input);
                        String summaryDoc = AiDocSummarizationProcessor.processDocument(input);
                        System.out.println("Output: " + summaryDoc);

                        // Send the aiModelOutput to the destination
                        queueMessageSender.sendMessage(summaryDoc);

                        // Let the service know we're finished with
                        // the message and it can be safely deleted.
                        queueClient.deleteMessage(receivedMessage.getMessageId(), receivedMessage.getPopReceipt());
                    });

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
}
