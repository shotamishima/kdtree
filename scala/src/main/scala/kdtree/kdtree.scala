package kdtree

import collection.mutable

case class Position(val x: Double, val y: Double)

class Node {
    var location = Position(0, 0) 
    var lhs: Option[Node] = None
    var rhs: Option[Node] = None 
    var axis = 0
}

class KdTree {

    private var node = new Node

    def build(pointList: mutable.ArrayBuffer[Position], depth: Int = 0): Unit = {
        buildRecursive(pointList, depth) match {
            case Some(nodeBuilt) => {
                node = nodeBuilt
                println("Success to build kd-tree")
            }
            case None => println("Failed to build kd-tree")
        }
    }
   
    def nnSearch(query: Position) = {

    }

    private def buildRecursive(pointList: mutable.ArrayBuffer[Position], 
                               depth: Int = 0
                               ): Option[Node] = {
        if ( pointList.nonEmpty ) {
            // get k-dimention
            // TODO finite 2-dimention. need to update p(0).length and add method length to case classPosition 
            val k = 2
            // select axis to judge
            val axis = depth % k 
            
            // sort by axis
            val sortedPoints = if (axis == 0) {
                pointList.sortBy(_.x)
            } else {
                pointList.sortBy(_.y)
            }
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
            // val node = new Node(location = sortedPoints(median),
            //                     lhs = buildRecursive(sortedPoints.slice(0, median), depth + 1),
            //                     rhs = buildRecursive(sortedPoints.slice(median + 1, sortedPoints.length), depth + 1),
            //                     axis = axis
            //                     )
            Option(node)

        } else {
            None
        }
    }

}
