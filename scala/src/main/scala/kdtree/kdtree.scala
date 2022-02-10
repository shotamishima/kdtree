package kdtree

import collection.mutable
import math.{abs, sqrt}
import scala.annotation.tailrec

case class Position(val x: Double, val y: Double)

class Node {
    var location: List[Double] = List(0, 0)
    var lhs: Option[Node] = None
    var rhs: Option[Node] = None 
    var axis = 0
}

class KdTree {

    private var node = new Node

    def build(pointList: mutable.ArrayBuffer[List[Double]], depth: Int = 0): Unit = {
        buildRecursive(pointList, depth) match {
            case Some(nodeBuilt) => {
                node = nodeBuilt
                println("Success to build kd-tree")
            }
            case None => println("Failed to build kd-tree")
        }
    }
   
    def nnSearch(query: List[Double]) = {
        var minDist = 10000000 // TODO: change to infinity
        var guess = List(0.0, 0.0)
        nnSearchRecursive(Option(node), query, minDist, guess)
        
    }

    private def buildRecursive(pointList: mutable.ArrayBuffer[List[Double]], 
                               depth: Int = 0
                               ): Option[Node] = {
        if ( pointList.nonEmpty ) {
            // get k-dimention
            // TODO finite 2-dimention. need to update p(0).length and add method length to case classPosition 
            val k = 2
            // select axis to judge
            val axis = depth % k 
            
            // sort by axis
            val sortedPoints = pointList.sortBy(_(axis))
            // calculate index located in median 
            val median: Int = (sortedPoints.length - 1) / 2 
            
            println(axis)
            println(median)
            println(sortedPoints(median))
            println(sortedPoints)
            println("************************")
            
            val node = new Node
            node.location = sortedPoints(median)
            node.axis = axis
            node.lhs = buildRecursive(sortedPoints.slice(0, median), depth + 1) 
            node.rhs = buildRecursive(sortedPoints.slice(median + 1, sortedPoints.length), depth + 1) 
            Option(node)

        } else {
            None
        }
    }
    
    private def nnSearchRecursive(node: Option[Node], 
                                    query: List[Double], 
                                    minDist: Double, 
                                    guess: List[Double]
                                ): Tuple2[List[Double], Double] = {
        node match {
            case Some(node) => {
                // update nearest neighbor point 
                val currentPoint = node.location
                val dist = distance(query, currentPoint)
                
                // update guess point and minimum distance
                val (minDistUpdate, guessUpdate) = if ( dist < minDist ) {
                    (dist, currentPoint)
                } else { // not update
                    (minDist, guess)
                }
                
                // select axis to compare node
                val axis = node.axis
                val (nextNode, nextNode_) = if ( query(axis) < currentPoint(axis) ) {
                    (node.lhs, node.rhs)
                } else {
                    (node.rhs, node.lhs)
                }
                // if ( query(axis) < currentPoint(axis) ) {
                //     val nextNode = node.lhs
                //     val nextNode_ = node.rhs
                // } else {
                //     val nextNode = node.rhs
                //     val nextNode_ = node.lhs
                // }
                
                // step into next node
                val (guessReturn, minDistReturn) = nnSearchRecursive(nextNode, query, minDistUpdate, guessUpdate)

                // check neighbor node
                val diff = abs(query(axis) - currentPoint(axis))
                if ( diff < minDist ) {
                    val (guessReturn, minDistReturn) = nnSearchRecursive(nextNode_, query, minDistUpdate, guessUpdate)
                }
                (guessReturn, minDistReturn)
            }
            case None => {
                (guess, minDist)
            }
        }
    }
    
    private def distance(p1: List[Double], p2: List[Double]): Double = {
        var dist = 0.0
        for ( (elem1, elem2) <- p1.zip(p2) ) {
            dist += (elem1 - elem2) * (elem1 - elem2)
        }
        sqrt(dist)
    }

}
