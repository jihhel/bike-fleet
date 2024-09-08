package backend

import stations.*

import scala.concurrent.Future

class StationsServiceImpl extends StationsService {

  private val stations = List.range(0, 5).map(idx => Station(idx))

  private val bikes = List.range(6, 55).map(idx => {
    Bike(idx)
  })

  private val stationsWithBikes = stations.map(s => {
    val stationId = s.stationId
    s.stationId -> bikes.slice(stationId * 10, stationId * 10 + 10)
  }).toMap

  override def getStations(in: GetStationsRequest): Future[GetStationsResponse] = Future.successful(GetStationsResponse(stations))

  override def getBikesFromStation(in: GetBikesFromStationRequest): Future[GetBikesFromStationResponse] = {
    Future.successful(GetBikesFromStationResponse(in.stationId, stationsWithBikes.getOrElse(in.stationId, List.empty[Bike])))
  }
}
