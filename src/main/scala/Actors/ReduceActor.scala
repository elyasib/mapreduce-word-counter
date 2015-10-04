package simpleMapReduce.Actors

import scala.collection.immutable.Map
import simpleMapReduce.Messages._
import akka.actor.{Actor, Props, ActorLogging}

class ReduceActor extends Actor with ActorLogging { 
  import ReduceActor._
  def receive : Receive = {
    case MappedData(mappedData) => sender ! accumulate(mappedData)
  }
}

object ReduceActor {
  def accumulate(mappedData : Array[WordCount]) : ReducedData = ReducedData {
    //Fold is used to accumulated the number of appeareances of any word in a given line (mappedData). 
    //The information is accumulated using a Map
    //pair is of type WordCount.
    mappedData.foldLeft(Map[String, Int]()){(accMap, pair) => {
        if(accMap.contains(pair.word)) 
          accMap + (pair.word -> (accMap.get(pair.word).get + 1))
        else 
          accMap + (pair.word -> 1) 
      }
    }
  }
}
