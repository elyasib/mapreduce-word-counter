package simpleMapReduce.Actors

import simpleMapReduce.Messages._
import akka.actor.{Actor, Props, ActorLogging}

class MapActor extends Actor with ActorLogging { 
  import MapActor._
  def receive : Receive = {
    case line : String => sender ! countWords(line)
  }
}

object MapActor {
  val separators = """[\s,."(,\s)(.\s)(\s")]+"""
  def countWords(line : String) : MappedData = MappedData {
    line split (separators) map (x => WordCount(x.toLowerCase, 1))
  }
}
