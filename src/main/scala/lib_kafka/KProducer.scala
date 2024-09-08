package lib_kafka

import cats.effect.IO
import org.apache.kafka.clients.producer.{KafkaProducer, RecordMetadata}

import java.util.Properties

abstract class KProducer[K, V, A <: KRecord[K, V]](
      props: Properties,
      topic: String,
      keySerializerName: String,
      valueSerializerName: String) {

    props.put("key.serializer", keySerializerName)
    props.put("value.serializer", valueSerializerName)

    private val javaProducer = new KafkaProducer[K, V](props);

    def produce(record: KRecord[K, V]): IO[RecordMetadata] = {
        IO.blocking {
            javaProducer.send(record.toJavaRecord(topic)).get()
        }
    }
}

