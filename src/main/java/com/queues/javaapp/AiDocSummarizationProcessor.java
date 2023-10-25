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

    // Method to authenticate the client object with your key and endpoint
    static TextAnalyticsClient authenticateClient(String key, String endpoint) {
        return new TextAnalyticsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    public static String processDocument(String inputDocument) {
        AppConfigs appSettingConfigs = new AppConfigs();

        appSettingConfigs.languageKey = "";
        appSettingConfigs.languageEndpoint = "";
        // Get the config from environment variables
        EnvConfigProvider.ConfigValues(appSettingConfigs);

        //
        // Authenticate and create client
        //
        TextAnalyticsClient client = authenticateClient(appSettingConfigs.languageKey, appSettingConfigs.languageEndpoint);
        List<String> documents = new ArrayList<>();

        documents.add(inputDocument);

        StringBuilder sb = new StringBuilder();

        SyncPoller<AnalyzeActionsOperationDetail, AnalyzeActionsResultPagedIterable> syncPoller =
                client.beginAnalyzeActions(documents,
                        new TextAnalyticsActions().setDisplayName("{tasks_display_name}")
                                .setAbstractiveSummaryActions(
                                        new AbstractiveSummaryAction()),
                        "en",
                        new AnalyzeActionsOptions());
        syncPoller.waitForCompletion();
        syncPoller.getFinalResult().forEach(actionsResult -> {
            System.out.println("Abstractive Summarization action results:");
            for (AbstractiveSummaryActionResult actionResult : actionsResult.getAbstractiveSummaryResults()) {
                if (!actionResult.isError()) {
                    for (AbstractiveSummaryResult documentResult : actionResult.getDocumentsResults()) {
                        if (!documentResult.isError()) {
                            System.out.println("\tSummary:");
                            for (AbstractiveSummary summary : documentResult.getSummaries()) {
                                sb.append(String.format("Summary:\r\n%s\r\n", summary.getText()));
                                System.out.printf("\t\tSummary:\r\n%s\r\n", summary.getText());
                            }
                        } else {
                            sb.append( String.format(
                                "\tCannot extract summary. Error: %s%n",
                                documentResult.getError().getMessage()));
                            System.out.printf("\tCannot extract summary. Error: %s%n",
                                    documentResult.getError().getMessage());
                        }
                    }
                } else {
                    sb.append( String.format( 
                                       "\tCannot execute Abstractive Summarization action. Error: %s%n",
                                       actionResult.getError().getMessage()));
                    System.out.printf("\tCannot execute Abstractive Summarization action. Error: %s%n",
                            actionResult.getError().getMessage());
                }
            }
        });
        
        return sb.toString();
    }
}