package lib_kafka

import org.apache.kafka.clients.producer.ProducerRecord

trait ProdRecord[K, V] {
    def key: K
    def value: V

    def toJavaRecord(topic: String): ProducerRecord[K, V] = {
        new ProducerRecord[K, V](topic, key, value)
    }
}

final case class BikeEventRecord(key: Int, value: String) extends ProdRecord[Int, String]

