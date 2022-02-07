from random import random

from kdtree.kdtree import *


if __name__ == '__main__':

    point_list = [[random()*10, random()*10] for _ in range(10)]
    print(point_list)

    kdtree = kdtree(point_list)    
    kdtree.build()

    print()
    print('kdtree create----------------')
    print(kdtree.node.location)

    print()
    print('nnsearch----------------------')
    # nnsearch 
    query = [2,2]
    guess = kdtree.nnsearch(query)
    print(guess)
    
    answer, answer_point = test_nnsearch(point_list, query)
    print(f'answer nnsearch: {answer_point}')
    
    print()
    print('knnsearch--------------')
    knn_list, dist_list = kdtree.knnsearch(query, 2)
    print(knn_list)
    print(dist_list)
    
    print()
    print('radius search --------------------')
    pos_list, dist_list = kdtree.radius_search(query, 5)
    print(pos_list)
    print(dist_list)