from random import random

from kdtree.kdtree import *


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