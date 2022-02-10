package kdtree

import collection.mutable
import util.Random

object Main extends App {

    // create sample points
    val pointList = createSamplePoint(10)
    println(s"pointList: $pointList")

    // build kd-tree
    val kdtree = new KdTree()
    kdtree.build(pointList)
    
    // nearest neighbor search
    println("nn search --------------------------")
    val query = List(2.0, 2.0)
    val (nnpoint, nndist) = kdtree.nnSearch(query)
    println(nnpoint)
    println(nndist)
    println()
    // k-nearest neighbor search
    println("knn search -------------------------")
    val (knnDistList, knnPointList) = kdtree.knnSearch(query, 4)
    println(knnDistList)
    println(knnPointList)
    println()

    def createSamplePoint(size: Int): mutable.ArrayBuffer[List[Double]] = {
        val r = new Random
        val xs = for ( i <- 0 to size ) yield r.nextInt(size).toDouble
        val ys = for ( i <- 0 to size ) yield r.nextInt(size).toDouble
        
        val pointList = for ( (x, y) <- xs.zip(ys) ) yield List(x, y)
        mutable.ArrayBuffer(pointList: _*)

    }
}