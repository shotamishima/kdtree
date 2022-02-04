from random import random


class Node:
    def location(element):
        self.location = element

    def left_child(point_list):
        self.left_child = point_list
    
    def right_child(point_list):
        self.righ_child = point_list


def kdtree(point_list, depth=0, leaf_size=4):
    if not point_list:
        return

    # 要素の次元数は同じとしてidx=0の次元を取得
    k = len(point_list[0])
    # 深さ毎に判定する軸を選択する
    axis = depth % k
    
    # axisでソート
    point_list.sort(key=lambda x: x[axis])
    # 中央値となるインデックスを算出
    median = len(point_list) % 2
    left_list = point_list[0: median]
    right_list = point_list[median + 1:]

    # 左右とも再帰する場合
    if (len(left_list) > leaf_size) and (len(right_list) > leaf_size):
        node = Node()
        node.location = point_list[median] # その深さにおけるノードになる要素
        node.left_child= kdtree(point_list[0: median], depth + 1)
        node.right_child = kdtree(point_list[median + 1:], depth + 1)
        return node
    
    # 左だけ再帰する場合
    elif (len(left_list) > leaf_size) and (len(right_list) <= leaf_size):
        node = Node()
        node.location = point_list[median]
        node.left_child = kdtree(point_list[0: median], depth + 1)
        return node

    # 右だけ再帰する場合
    elif (len(left_list) <= leaf_size) and (len(right_list) > leaf_size):
        node = Node()
        node.location = point_list[median]
        node.right_child = kdtree(point_list[median + 1:], depth + 1)
        return node


if __name__ == '__main__':

    point_list = [[random()*10, random()*10] for _ in range(10)]
    print(point_list)
    node = kdtree(point_list)
    print(node.location)
