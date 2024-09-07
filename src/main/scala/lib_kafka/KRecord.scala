package lib_kafka

import org.apache.kafka.clients.producer.ProducerRecord

trait KRecord[K, V] {
    def key: K
    def value: V

    def toJavaRecord(topic: String): ProducerRecord[K, V] = {
        new ProducerRecord[K, V](topic, key, value)
    }
}


