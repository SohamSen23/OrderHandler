# OrderHandler

An application to handle restaurant orders asynchronously using Kafka.

This app is a simple introduction to Kafka. Using this, we can set up a Kafka consumer listener which accepts orders in one topic, 
mimics some business logic which adds some latency and then publishes the finished orders to another topic.
