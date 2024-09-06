package lib_kafka

import cats.effect.IO
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties

abstract class Producer[K, V, A <: ProdRecord[K, V]](
      props: Properties,
      topic: String,
      keySerializerName: String,
      valueSerializerName: String) {

    props.put("key.serializer", keySerializerName)
    props.put("value.serializer", valueSerializerName)

    private val javaProducer = new KafkaProducer[K, V](props);

    def produce(record: ProdRecord[K, V]): IO[Unit] =  {
        IO.blocking {
            javaProducer.send(record.toJavaRecord(topic)).get()
        }
    }
}

class BikeEventProducer(props: Properties) extends Producer[Int, String, BikeEventRecord](
    props,
    "bike-event",
    "org.apache.kafka.common.serialization.IntegerSerializer",
    "org.apache.kafka.common.serialization.StringSerializer") {}
