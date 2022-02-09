package kdtree

import collection.mutable

case class Position(val x: Double, val y: Double)

// class Node {
//     var location = Position(0, 0)
//     var lhs: Option[Node] = None
//     var rhs: Option[Node] = None
// var axis: Int = 0
// }

class Node(val location: Position, val lhs: Option[Node], val rhs: Option[Node], val axis: Int)

class KdTree {

    def build(pointList: mutable.ArrayBuffer[Position], depth: Int = 0): Option[Node] = {
        buildRecursive(pointList, depth)
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
            
            val node = new Node(location = sortedPoints(median),
                                lhs = buildRecursive(sortedPoints.slice(0, median), depth + 1),
                                rhs = buildRecursive(sortedPoints.slice(median + 1, sortedPoints.length), depth + 1),
                                axis = axis
                                )
            Option(node)

        } else {
            None
        }
    }

}
