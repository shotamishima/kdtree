package kdtree

import collection.mutable
import util.Random

object Main extends App {

    // create sample points
    val pointList = createSamplePoint(10)
    println(s"pointList: $pointList")

    val kdtree = new KdTree()

    val n = kdtree.build(pointList)
    
    n match {
        case Some(n) => {
            println("yes node")
            println(n.location)
            println(n.lhs.get.location)
        }
        case None => println("non node")
    }
    
    def createSamplePoint(size: Int): mutable.ArrayBuffer[Position] = {
        val r = new Random
        val xs = for ( i <- 0 to size ) yield r.nextInt(size).toDouble
        val ys = for ( i <- 0 to size ) yield r.nextInt(size).toDouble
        
        val pointList = for ( (x, y) <- xs.zip(ys) ) yield Position(x, y)
        mutable.ArrayBuffer(pointList: _*)

    }
}