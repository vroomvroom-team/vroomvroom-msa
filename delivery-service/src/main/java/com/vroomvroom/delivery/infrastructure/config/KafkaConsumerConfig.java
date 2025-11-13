package com.vroomvroom.delivery.infrastructure.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfig {

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Bean
//    public ConsumerFactory<String, ManagerAssignmentEvent> consumerFactory() {
//        Map<String, Object> configProps = new HashMap<>();
//
//        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, deliveryAssignmentGroup);
//
//        JsonDeserializer<ManagerAssignmentEvent> deserializer =
//            new JsonDeserializer<>(ManagerAssignmentEvent.class);
//        deserializer.addTrustedPackages("*");
//
//        return new DefaultKafkaConsumerFactory<>(
//            configProps,
//            new StringDeserializer(),
//            new ErrorHandlingDeserializer<>(deserializer)
//        );
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, ManagerAssignmentEvent>
//    kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, ManagerAssignmentEvent> factory =
//            new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}
