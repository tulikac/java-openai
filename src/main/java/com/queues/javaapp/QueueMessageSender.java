package com.queues.javaapp;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;

public class QueueMessageSender implements IMessageSender {
    private final QueueClient _queueClient;

    public QueueMessageSender(String connectioString, String queueName) {
        _queueClient = new QueueClientBuilder()
                .connectionString(connectioString)
                .queueName(queueName)
                .buildClient();
    }

    public void sendMessage(String messageText) {
        _queueClient.sendMessage(messageText);
    }
}
