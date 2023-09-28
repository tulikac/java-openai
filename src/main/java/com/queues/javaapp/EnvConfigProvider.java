package com.queues.javaapp;

public class EnvConfigProvider {
    public static void ConfigValues(AppConfigs appSettingConfigs) {
        String storageConString = System.getenv(Constants.AZURE_STORAGE_CONNECTION_STRING);

        if (storageConString != null && storageConString != "") {
            appSettingConfigs.azureStorageConnectionString = storageConString;
        }

        String configSourceQueueName = System.getenv(Constants.AZURE_STORAGE_SOURCE_QUEUE_NAME);
        if (configSourceQueueName != null && configSourceQueueName != "") {
            appSettingConfigs.sourceQueueName = configSourceQueueName;
        }

        String configDestinationQueueName = System.getenv(Constants.AZURE_STORAGE_DESTINATION_QUEUE_NAME);
        if (configDestinationQueueName != null && configDestinationQueueName != "") {
            appSettingConfigs.destinationQueueName = configDestinationQueueName;
        }

        String configOpenApiKey = System.getenv(Constants.AZURE_OPEN_API_KEY);
        if (configOpenApiKey != null && configOpenApiKey != "") {
            appSettingConfigs.azureOpenAiKey = configOpenApiKey;
        }

        String configOpenApiEndpoint = System.getenv(Constants.AZURE_OPEN_API_ENDPOINT);
        if (configOpenApiEndpoint != null && configOpenApiEndpoint != "") {
            appSettingConfigs.openAiEndpoint = configOpenApiEndpoint;
        }

        String configOpenApiModelDeploymentName = System.getenv(Constants.AZURE_OPEN_API_MODEL_DEPLOYMENT_NAME);
        if (configOpenApiModelDeploymentName != null && configOpenApiModelDeploymentName != "") {
            appSettingConfigs.aiModelDeploymentName = configOpenApiModelDeploymentName;
        }
    }
}
