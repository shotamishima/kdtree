from email.errors import NonPrintableDefect
import math
from mimetypes import guess_all_extensions
from random import random
from tkinter import W


class Node:
    def __init__(self):
        self.location = None
        self.lhs = None
        self.rhs = None
        self.axis = None
        

class kdtree:

    def __init__(self, point_list):
        self.point_list = point_list
        self.node = Node()

    def build(self, depth=0, leaf_size=4):
        self.node = self._build_recursive(self.point_list, depth)

    def nnsearch(self, query):
        min_dist = 100000 # TODO infinity
        guess = self._nnsearch_recursive(self.node, query, min_dist)
        return guess
        
    def _build_recursive(self, point_list, depth=0):
        if not point_list:
            return

        # 要素の次元数は同じとしてidx=0の次元を取得
        k = len(point_list[0])
        # 深さ毎に判定する軸を選択する
        axis = depth % k
        
        # axisでソート
        point_list.sort(key=lambda x: x[axis])
        # 中央値となるインデックスを算出
        median = (len(point_list) - 1) // 2

        print(axis)
        print(median)
        print(point_list)
        print('-----------------')

        node = Node()
        node.location = point_list[median]
        node.axis = axis
        node.lhs = self._build_recursive(point_list[0: median], depth + 1)
        node.rhs = self._build_recursive(point_list[median + 1:], depth + 1)

        return node


    def _nnsearch_recursive(self, node, query, min_dist):

        current_point = node.location
        dist = self._distance(current_point, query)
        if dist < min_dist:
            min_dist = dist
            guess = current_point
        
        # 比較する軸を選択
        axis = node.axis
        if query[axis] < current_point[axis]:
            next_node = node.lhs
        else:
            next_node = node.rhs
        
        guess = self._nnsearch_recursive(self, next_node, query, min_dist)

        return guess


    def _distance(p1, p2):
       """
       p1, p2: list n-dimention
       """ 
       dist = 0
       for element1, element2 in zip(p1, p2):
           dist += (element1 - element2) * (element1 - element2)
       return math.sqrt(dist)


        

if __name__ == '__main__':

    point_list = [[random()*10, random()*10] for _ in range(10)]
    print(point_list)

    kdtree = kdtree(point_list)    
    kdtree.build()

    print(kdtree.node.location)
    print(kdtree.node.axis)

