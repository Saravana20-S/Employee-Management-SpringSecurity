package com.bridgelabz.employeemanagement.rabbitmq;

import com.bridgelabz.employeemanagement.entity.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishFileUploadedEvent(
            FileUpload upload) {

        EmployeeFileUploadedEvent event =
                EmployeeFileUploadedEvent.builder()
                        .uploadId(upload.getId())
                        .fileName(upload.getFileName())
                        .uploadedBy(
                                upload.getUploadedBy()
                                        .getEmail()
                        )
                        .eventType(
                                "EMPLOYEE_FILE_UPLOADED"
                        )
                        .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}