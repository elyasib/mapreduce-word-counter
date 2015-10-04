package simpleMapReduce.Actors

import simpleMapReduce.Messages._
import akka.actor.{ Actor, Props, ActorLogging }
import akka.routing.{ ActorRefRoutee, RoundRobinRoutingLogic, Router }


class MasterActor extends Actor with ActorLogging {
  private[this] var mapMsgCounter = 0
  private[this] var reduceMsgCounter = 0
  private[this] val mapRouter = createRouter(Props[MapActor])
  private[this] val reduceRouter = createRouter(Props[ReduceActor])
  private[this] val aggregator = context.actorOf(Props[AggregatorActor].withDispatcher("priority-dispatcher"), name = "aggregate")

  def createRouter(props : Props) = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(props.withDispatcher("priority-dispatcher"))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }
  
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
