package simpleMapReduce.MPPriorityMailBox

import simpleMapReduce.Messages._
import akka.dispatch.PriorityGenerator
import akka.dispatch.UnboundedStablePriorityMailbox
import com.typesafe.config.Config
import akka.actor.ActorSystem

//import akka.dispatch

class MyPriorityMailBox(settings: ActorSystem.Settings, config: Config) 
  extends UnboundedStablePriorityMailbox(
    PriorityGenerator{
      case x : WordCount => 0
      case x : MappedData => 0
      case x : ReducedData => 0
      case Result => 10
      case _ => 1
    }
  )
