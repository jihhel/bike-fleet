package bike_simulation.bike_event

import io.circe._
import io.circe.syntax._
import org.apache.kafka.common.serialization.Serializer

class BikeEventSerializer extends Serializer[BikeEvent] {

  override def serialize(topic: String, data: BikeEvent): Array[Byte] = {
    data.asJson.toString.getBytes
  }
}
