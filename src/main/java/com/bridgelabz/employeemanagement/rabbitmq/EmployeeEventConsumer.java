package com.bridgelabz.employeemanagement.rabbitmq;

import com.bridgelabz.employeemanagement.service.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventConsumer {

    private final BatchService batchService;

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE
    )
    public void consumeEmployeeUploadEvent(
            EmployeeFileUploadedEvent event) {

        log.info(
                "Received employee upload event for uploadId: {}",
                event.getUploadId()
        );

        batchService.startBatchJob(
                event.getUploadId()
        );
    }
}