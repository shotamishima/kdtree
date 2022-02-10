package kdtree

import collection.mutable
import math.{abs, sqrt}
import scala.annotation.tailrec
import scala.annotation.varargs

case class Position(val x: Double, val y: Double)

class Node {
    var location: List[Double] = List(0, 0)
    var lhs: Option[Node] = None
    var rhs: Option[Node] = None 
    var axis = 0
}

// using knnSearch and radiusSearch
class nPoint(val dist: Double, val point: List[Double])

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
   
    def nnSearch(query: List[Double]): Tuple2[List[Double], Double] = {
        val minDist = 10000000 // TODO: change to infinity
        val guess = List(0.0, 0.0)
        nnSearchRecursive(Option(node), query, minDist, guess)
    }
    
    def knnSearch(query: List[Double], k: Int): Tuple2[mutable.ArrayBuffer[Double], mutable.ArrayBuffer[List[Double]]] = {
        val guessList = mutable.ArrayBuffer[nPoint]()
        val guessListReturn = knnSearchRecursive(Option(node), query, guessList, k)

        // extend
        val knnDistList = for ( knnPoint <- guessListReturn.slice(0, k) ) yield knnPoint.dist
        val knnPointList = for ( knnPoint <- guessListReturn.slice(0, k) ) yield knnPoint.point

        (knnDistList, knnPointList)
    }
    
    def radiusSearch(query: List[Double], radius: Double): Tuple2[mutable.ArrayBuffer[Double], mutable.ArrayBuffer[List[Double]]] = {
        val guessList = mutable.ArrayBuffer[nPoint]()
        val guessListReturn = radiusSearchRecursive(Option(node), query, guessList, radius)
        
        // extend 
        val distList = for ( npoint <- guessListReturn ) yield npoint.dist
        val pointList = for ( npoint <- guessListReturn ) yield npoint.point
        
        (distList, pointList)
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
                
                // step into next node
                val (guessReturn, minDistReturn) = nnSearchRecursive(nextNode, query, minDistUpdate, guessUpdate)

                // check neighbor node
                val diff = abs(query(axis) - currentPoint(axis))
                if ( diff < minDist ) {
                    val (guessReturnIf, minDistReturnIf) = nnSearchRecursive(nextNode_, query, minDistReturn, guessReturn)
                    return (guessReturnIf, minDistReturnIf)
                }
                (guessReturn, minDistReturn)
            }
            case None => {
                (guess, minDist)
            }
        }
    }
    
    private def knnSearchRecursive(node: Option[Node],
                                   query: List[Double],
                                   guessList: mutable.ArrayBuffer[nPoint],
                                   k: Int
                                   ): mutable.ArrayBuffer[nPoint] = {
        node match {
            case Some(node) => {
                // add current point and dist from query into guessList
                val currentPoint = node.location
                val dist = distance(query, currentPoint)
                guessList += (new nPoint(dist, currentPoint))
                val sortedGuessList = guessList.sortBy(_.dist)

                // update axis for compare and update next node
                val axis = node.axis
                val (nextNode, nextNode_) = if ( query(axis) < currentPoint(axis) ) {
                    (node.lhs, node.rhs)
                } else {
                    (node.rhs, node.lhs)
                }

                // step into next node
                val guessListReturn = knnSearchRecursive(nextNode, query, sortedGuessList, k)

                // check neighbor node
                val diff = abs(query(axis) - currentPoint(axis))
                if ( sortedGuessList.length < k || diff < guessListReturn(guessListReturn.length-1).dist ) {
                    val guessListReturnIf = knnSearchRecursive(nextNode_, query, guessListReturn, k)
                    return guessListReturnIf
                }
                guessListReturn
            }
            case None => {
                guessList
            }
        }
        
    }

    private def radiusSearchRecursive(node: Option[Node],
                                      query: List[Double],
                                      guessList: mutable.ArrayBuffer[nPoint],
                                      radius: Double
                                      ): mutable.ArrayBuffer[nPoint] = {
        node match {
            case Some(node) => {
                // append curernt position if dist < radius
                val currentPoint = node.location
                val dist = distance(query, currentPoint)
                if ( dist < radius ) {
                    guessList += (new nPoint(dist, currentPoint))
                }

                // update axis for compare and update next node
                val axis = node.axis
                val (nextNode, nextNode_) = if ( query(axis) < currentPoint(axis) ) {
                    (node.lhs, node.rhs)
                } else {
                    (node.rhs, node.lhs)
                }

                // step into next node
                val guessListReturn = radiusSearchRecursive(nextNode, query, guessList, radius)

                // search neighbor tree if parent dist < radius
                val diff = abs(query(axis) - currentPoint(axis))
                if ( diff < radius ) {
                    val guessListReturnIf = radiusSearchRecursive(nextNode_, query, guessListReturn, radius)
                    return guessListReturnIf
                }
                guessListReturn
            }
            case None => {
                guessList
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
