package bike_simulation

import bike_simulation.bike_event.{BikeEvent, BikeRecord}
import lib_kafka.KProducer

import java.util.Properties

class BikeProducer(props: Properties) extends KProducer[Int, BikeEvent, BikeRecord](
  props,
  "bike-event",
  "org.apache.kafka.common.serialization.IntegerSerializer",
  "bike_simulation.bike_event.BikeEventSerializer") {}
