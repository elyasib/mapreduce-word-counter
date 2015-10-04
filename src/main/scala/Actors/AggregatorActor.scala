package simpleMapReduce.Actors

import scala.collection.mutable.HashMap
import simpleMapReduce.Messages._
import akka.actor.{Actor, Props, ActorLogging}

class AggregatorActor extends Actor with ActorLogging { 
  //import AggregatorActor._

  //private[this] val result = HashMap[String, Int]()
  val result = HashMap[String, Int]()
  def receive : Receive = {
    case ReducedData(reducedData) => aggregate(reducedData)
    case Result => sender ! toString(result)
  }

  def toString(map : HashMap[String, Int]) : String = {
    "number of words: " + map.size.toString + "\n" + (map.toList sortWith (_._2 > _._2)).foldLeft("")((y,x) => y + x._1 + " -> " + x._2.toString + "\n")
  }

  def aggregate(reducedData : Map[String, Int]) : HashMap[String,Int] = {
    reducedData.foldLeft(result){
      (resultMap, elem) => {
        val (word, count) = elem
        if(result contains word) {
          result(word) = result(word) + count
        } else {
          result += (word -> count)
        }
        result
      }
    }
  }
}
