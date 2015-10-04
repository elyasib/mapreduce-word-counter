package simpleMapReduce

import java.io.File
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import Messages._
import Actors._
import scala.io.Source
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object simpleMapReduce {
  def main(args : Array[String]) : Unit = {

    val wordCounterSys = ActorSystem("simpleMapReduce")
    val masterActor = wordCounterSys.actorOf(Props[MasterActor].withDispatcher("priority-dispatcher"), name = "master")
    implicit val timeout = Timeout(5 seconds)

    val lines = Source.fromFile("""/Users/elyasib/code/akka-apps/simpleMapReduce/src/main/resources/inFile.txt""").getLines.toArray 
    //send each line to the masterActor to process the file
    println("number of lines: " + lines.size)
    lines foreach (masterActor ! _)

    //get the result as a Future
    val result : Future[String] = (masterActor ? Result).mapTo[String]

    //val r = for {
    //  a <- result.mapTo[String]
    //} yield a
    //
    //r foreach (x => println(">>>>>>>>>>> salida: " + x)) 

    result.onComplete{
      case Success(x) => {
        println("success")
        println(x)
        wordCounterSys.shutdown()
      }
      case Failure(x) => {
        println("fail")
        println(x.getMessage)
        wordCounterSys.shutdown()
      }
    }
  }
}
