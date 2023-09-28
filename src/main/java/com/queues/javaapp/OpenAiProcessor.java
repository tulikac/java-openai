package com.queues.javaapp;

import java.util.ArrayList;
import java.util.List;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.Completions;
import com.azure.ai.openai.models.CompletionsOptions;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.IterableStream;

public class OpenAiProcessor {
    final String _deploymentModelName;
    final OpenAIClient _client;

    public OpenAiProcessor(String openAiKey, String endpoint, String deploymentModelName) {
        _deploymentModelName = deploymentModelName;
        _client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(openAiKey))
                .buildClient();
    }

    public String processPrompt(String promptInput) {
        List<String> prompt = new ArrayList<>();
        prompt.add(promptInput);
        IterableStream<Completions> completionsStream = _client.getCompletionsStream(
                _deploymentModelName,
                new CompletionsOptions(prompt).setMaxTokens(1000).setStream(true));

        StringBuilder sb = new StringBuilder();
        completionsStream
                .stream()
                // Remove .skip(1) when using Non-Azure OpenAI API
                // Note: the first chat completions can be ignored when using Azure OpenAI
                // service which is a known service bug.
                // TODO: remove .skip(1) when service fix the issue.
                .skip(1)
                .forEach(completions -> {
                    String compTxString = completions.getChoices().get(0).getText();
                    sb.append(compTxString);
                    System.out.print(compTxString);
                });

        String ansString = "Question:\n" + promptInput + "\n" + "Answer:\n" + sb.toString();
        return ansString;
    }
}
