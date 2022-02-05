from random import random


class Node:
    def location(element):
        location = element

    def lhs(point_list):
        lhs = point_list
    
    def rhs(point_list):
        rhs= point_list


class kdtree:

    def __init__(self, point_list):
        self.point_list = point_list
        self.node = Node()

    def build(self, depth=0, leaf_size=4):
        self.node = self._build_recursive(self.point_list, depth, leaf_size)

    def _build_recursive(self, point_list, depth=0, leaf_size=4):
        if not point_list:
            return

        # 要素の次元数は同じとしてidx=0の次元を取得
        k = len(point_list[0])
        # 深さ毎に判定する軸を選択する
        axis = depth % k
        
        # axisでソート
        point_list.sort(key=lambda x: x[axis])
        # 中央値となるインデックスを算出
        median = len(point_list) %2
        left_list = point_list[0: median]
        right_list = point_list[median + 1:]

        # 左右とも再帰する場合
        if (len(left_list) > leaf_size) and (len(right_list) > leaf_size):
            node = Node()
            node.location = point_list[median] # その深さにおけるノードになる要素
            node.lhs= self._build_recursive(point_list[0: median], depth + 1)
            node.rhs =self._build_recursive(point_list[median + 1:], depth + 1)
            return node
        
        # 左だけ再帰する場合
        elif (len(left_list) > leaf_size) and (len(right_list) <= leaf_size):
            node = Node()
            node.location = point_list[median]
            node.lhs = self._build_recursive(point_list[0: median], depth + 1)
            return node

        # 右だけ再帰する場合
        elif (len(left_list) <= leaf_size) and (len(right_list) > leaf_size):
            node = Node()
            node.location = point_list[median]
            node.rhs = self._build_recursive(point_list[median + 1:], depth + 1)
            return node


if __name__ == '__main__':

    point_list = [[random()*10, random()*10] for _ in range(10)]
    print(point_list)

    kdtree = kdtree(point_list)    
    kdtree.build()

    print(kdtree.node.location)

