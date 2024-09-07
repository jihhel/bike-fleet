package bike_simulation

import lib_kafka.KProducer

import java.util.Properties

class BikeProducer(props: Properties) extends KProducer[Int, BikeEvent, BikeRecord](
  props,
  "bike-event",
  "org.apache.kafka.common.serialization.IntegerSerializer",
  "org.apache.kafka.common.serialization.StringSerializer") {}