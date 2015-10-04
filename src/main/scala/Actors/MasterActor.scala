package simpleMapReduce.Actors

import simpleMapReduce.Messages._
import akka.actor.{ Actor, Props, ActorLogging }
import akka.routing.{ ActorRefRoutee, RoundRobinRoutingLogic, Router }


class MasterActor extends Actor with ActorLogging {
  var mapMsgCounter = 0
  var reduceMsgCounter = 0
  val mapRouter = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[MapActor].withDispatcher("priority-dispatcher"))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  val reduceRouter = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[ReduceActor].withDispatcher("priority-dispatcher"))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  val aggregator = context.actorOf(Props[AggregatorActor].withDispatcher("priority-dispatcher"), name = "aggregate")
  
  def receive : Receive = {
    case line : String => {
      mapMsgCounter += 1
      mapRouter route (line, self)
    }
    case mappedData : MappedData => {
      mapMsgCounter -= 1
      reduceMsgCounter += 1
      reduceRouter route (mappedData, self) 
    }
    case reducedData : ReducedData => {
      reduceMsgCounter -= 1
      aggregator ! reducedData
    }
    case Result => {
      if(mapMsgCounter != 0 || reduceMsgCounter != 0) 
        self forward Result
      else 
        aggregator forward Result
    }
  }
}
