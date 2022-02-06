import copy
import heapq
import math
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
        guess, _ = self._nnsearch_recursive(self.node, query, min_dist, query)
        return guess
    
    def knnsearch(self, query, k):
        min_dist = 100000 # TODO infinity
        guess_list = []
        guess_list = self._knnsearch_recursive(self.node, query, guess_list, k)

        # return only k-position 
        return [pos for (dist, pos) in guess_list][:k]

        
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

        # print(axis)
        # print(median)
        # print(point_list)
        # print('-----------------')

        node = Node()
        node.location = point_list[median]
        node.axis = axis
        node.lhs = self._build_recursive(point_list[0: median], depth + 1)
        node.rhs = self._build_recursive(point_list[median + 1:], depth + 1)

        return node


    def _nnsearch_recursive(self, node, query, min_dist, guess):

        # Assuming the end of tree
        if not node:
            return guess, min_dist 

        # update nearest neighbor point 
        current_point = node.location
        dist = self._distance(current_point, query)
        if dist < min_dist:
            min_dist = dist
            guess = current_point
        
        print(f'dist: {dist}, min_dist: {min_dist}, current_point: {current_point}')

        # select axis to compare and update next_node
        axis = node.axis
        if query[axis] < current_point[axis]:
            next_node = node.lhs
            next_node_ = node.rhs
        else:
            next_node = node.rhs
            next_node_ = node.lhs
         
        # step into next_node
        # also return min_dist as min_dist update recursively
        guess, min_dist = self._nnsearch_recursive(next_node, query, min_dist, guess)

        # check neighbor node
        diff = abs(query[axis] - current_point[axis])
        if diff < min_dist:
            guess, min_dist = self._nnsearch_recursive(next_node_, query, min_dist, guess)
        
        return guess, min_dist

    def _knnsearch_recursive(self, node, query, guess_list, k):
        # assuming the end of tree
        if not node:
            return guess_list

        # add current position and dist to candidate list
        current_point = node.location
        dist = self._distance(current_point, query)
        guess_list.append((dist, current_point))
        # print(guess_list)
        guess_list.sort(key=lambda x: x[0])
        
        # update axis for compare and update next_node
        axis = node.axis
        if query[axis] < current_point[axis]:
            next_node = node.lhs
            next_node_ = node.rhs
        else:
            next_node = node.rhs
            next_node_ = node.lhs
        
        # step into next_node
        guess_list = self._knnsearch_recursive(next_node, query, guess_list, k)

        diff = abs(query[axis] - current_point[axis])
        if (len(guess_list) < k) or (diff < guess_list[-1][0]):
            self._knnsearch_recursive(next_node_, query, guess_list, k)
        
        return guess_list
        

    def _distance(self, p1, p2):
       """
       p1, p2: list n-dimention
       """ 
       dist = 0
       for element1, element2 in zip(p1, p2):
           dist += (element1 - element2) * (element1 - element2)
       return math.sqrt(dist)

def test_nnsearch(point_list, query):
    def distance(p1, p2):
        dist = 0
        for element1, element2 in zip(p1, p2):
            dist += (element1 - element2) * (element1 - element2)
        return math.sqrt(dist)
    
    min_dist = 100000
    min_point = copy.copy(query)
    for point in point_list:
        dist = distance(point, query)
        if dist < min_dist:
            min_dist = dist
            min_point = copy.copy(point)
            
    return min_dist, min_point
        

if __name__ == '__main__':

    point_list = [[random()*10, random()*10] for _ in range(10)]
    print(point_list)

    kdtree = kdtree(point_list)    
    kdtree.build()

    print('kdtree create----------------')
    print(kdtree.node.location)
    print(kdtree.node.axis)

    print('nnsearch----------------------')
    # nnsearch 
    query = [2,2]
    guess = kdtree.nnsearch(query)
    print(guess)
    
    answer, answer_point = test_nnsearch(point_list, query)
    print(f'answer nnsearch: {answer_point}')
    
    print('knnsearch--------------')
    knn_list = kdtree.knnsearch(query, 2)
    print(knn_list)