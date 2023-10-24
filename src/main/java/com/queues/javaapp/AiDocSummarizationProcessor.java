package com.queues.javaapp;

import com.azure.storage.queue.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.ai.textanalytics.models.*;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import java.util.ArrayList;
import java.util.List;
import com.azure.core.util.polling.SyncPoller;
import com.azure.ai.textanalytics.util.*;


public class AiDocSummarizationProcessor {
    //private static String languageKey = System.getenv("LANGUAGE_KEY");
    //private static String languageEndpoint = System.getenv("LANGUAGE_ENDPOINT");
    
    // Method to authenticate the client object with your key and endpoint
    static TextAnalyticsClient authenticateClient(String key, String endpoint) {
        return new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    ///
    /// processDocument(String inputDocument)
    ///
    public String processDocument(String inputDocument){
        return summarizationExample(inputDocument);
    }

    static String summarizationExample(String inputDocument) {
        AppConfigs appSettingConfigs = new AppConfigs();
 
        appSettingConfigs.azureOpenAiKey =  "";
        appSettingConfigs.openAiEndpoint = "";
        appSettingConfigs.aiModelDeploymentName =  "";
 
        appSettingConfigs.azureStorageConnectionString =  "";
        appSettingConfigs.sourceQueueName =  "";
        appSettingConfigs.destinationQueueName =  "";
        appSettingConfigs.languageKey = "";
        appSettingConfigs.languageEndpoint = "";
        // Get the config from environment variables
        EnvConfigProvider.ConfigValues(appSettingConfigs);
        TextAnalyticsClient client = authenticateClient(appSettingConfigs.languageKey, appSettingConfigs.languageEndpoint);
        List<String> documents = new ArrayList<>();
        // documents.add(
        //         "The extractive summarization feature uses natural language processing techniques "
        //         + "to locate key sentences in an unstructured text document. "
        //         + "These sentences collectively convey the main idea of the document. This feature is provided as an API for developers. "
        //         + "They can use it to build intelligent solutions based on the relevant information extracted to support various use cases. "
        //         + "Extractive summarization supports several languages. "
        //         + "It is based on pretrained multilingual transformer models, part of our quest for holistic representations. "
        //         + "It draws its strength from transfer learning across monolingual and harness the shared nature of languages "
        //         + "to produce models of improved quality and efficiency.");
        documents.add(inputDocument);
   
        SyncPoller<AnalyzeActionsOperationDetail, AnalyzeActionsResultPagedIterable> syncPoller =
                client.beginAnalyzeActions(documents,
                        new TextAnalyticsActions().setDisplayName("{tasks_display_name}")
                                .setExtractiveSummaryActions(
                                        new ExtractiveSummaryAction()),
                        "en",
                        new AnalyzeActionsOptions());
   
        syncPoller.waitForCompletion();
       
        StringBuilder sb = new StringBuilder();
        syncPoller.getFinalResult().forEach(actionsResult -> {
            System.out.println("Extractive Summarization action results:");
            for (ExtractiveSummaryActionResult actionResult : actionsResult.getExtractiveSummaryResults()) {
                if (!actionResult.isError()) {
                    for (ExtractiveSummaryResult documentResult : actionResult.getDocumentsResults()) {
                        if (!documentResult.isError()) {
                            System.out.println("\tExtracted summary sentences:");
                            for (ExtractiveSummarySentence summarySentence : documentResult.getSentences()) {
                                sb.append(
                                String.format("\t\t Sentence text: %s, length: %d, offset: %d, rank score: %f.%n\r\n",
                                    summarySentence.getText(), summarySentence.getLength(),
                                    summarySentence.getOffset(), summarySentence.getRankScore()));
                                System.out.printf(
                                        "\t\t Sentence text: %s, length: %d, offset: %d, rank score: %f.%n",
                                        summarySentence.getText(), summarySentence.getLength(),
                                        summarySentence.getOffset(), summarySentence.getRankScore());
                            }
                        } else {
                            sb.append( String.format(
                                "\tCannot extract summary sentences. Error: %s%n",
                                documentResult.getError().getMessage()));
                            System.out.printf("\tCannot extract summary sentences. Error: %s%n",
                                    documentResult.getError().getMessage());
                        }
                    }
                } else {
                    sb.append( String.format(
                                       "\tCannot execute Extractive Summarization action. Error: %s%n",
                                       actionResult.getError().getMessage()));
                    System.out.printf("\tCannot execute Extractive Summarization action. Error: %s%n",
                            actionResult.getError().getMessage());
                }
            }
        });
        return sb.toString();
    }
}
