package simpleMapReduce.Messages

sealed trait MapReduceMessage
case class WordCount(word : String, count : Int) extends MapReduceMessage
case class MappedData(countArray : Array[WordCount]) extends MapReduceMessage
case class ReducedData(countMap : Map[String, Int]) extends MapReduceMessage
case class Result() extends MapReduceMessage
