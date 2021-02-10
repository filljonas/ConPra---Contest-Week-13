import sys
import math


def dist_sq(a, b):
    return math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]))


def center_vector(vector):
    a = vector[0]
    b = vector[1]
    return (a[0] + b[0]) / 2, (a[1] + b[1]) / 2, 10


def ccw(a, b, p):
    res = (p[1] - a[1]) * (b[0] - a[0]) - (p[0] - a[0]) * (b[1] - a[1])
    if res > 0:
        return 'l'
    elif res < 0:
        return 'r'
    else:
        return 'c'


def sort_counter_clockwise(vector_a, vector_b):
    if vector_a[0][1] < vector_a[1][1] or (vector_a[0][1] == vector_a[1][1] and vector_a[0][0] < vector_a[1][0]):
        ap_1 = vector_a[0]
        ap_2 = vector_a[1]
    else:
        ap_1 = vector_a[1]
        ap_2 = vector_a[0]
    if vector_b[0][1] < vector_b[1][1] or (vector_b[0][1] == vector_b[1][1] and vector_b[0][0] < vector_b[1][0]):
        bp_1 = vector_b[0]
        bp_2 = vector_b[1]
    else:
        bp_1 = vector_b[1]
        bp_2 = vector_b[0]
    o = ccw(ap_1, ap_2, bp_1)
    if o == 'l':
        return ap_1, bp_2, ap_2, bp_1
    elif o == 'r':
        return ap_1, bp_1, ap_2, bp_2


def area_rect(A, B, C, D):
    a = dist_sq(A, B)
    b = dist_sq(B, C)
    return a * b


def find_rectangles(distances):
    max_area = 0
    # For every vector in the list, consider all vectors with equal distance
    for i in range(len(distances) - 1):
        for j in range(i + 1, len(distances)):
            # There are no vectors with equal distance anymore
            if distances[j][0] != distances[i][0]:
                break
            vector_a = distances[i][1], distances[i][2]
            vector_b = distances[j][1], distances[j][2]
            # Centers of the vectors
            center_a = center_vector(vector_a)
            center_b = center_vector(vector_b)
            if center_a == center_b:
                # We found a rectangle
                A, B, C, D = sort_counter_clockwise(vector_a, vector_b)
                cur_area = area_rect(A, B, C, D)
                if cur_area > max_area:
                    max_area = cur_area
    return max_area


def precompute_distances(points, n):
    distances = []
    # Get the distance between every pair of points (without repeating)
    # Store distance and the corresponding points in list
    for i in range(n - 1):
        for j in range(i + 1, n):
            distances.append((dist_sq(points[i], points[j]), points[i], points[j]))
    # Sort list by distance
    distances.sort(key=lambda x: x[0])
    return distances


if __name__ == '__main__':
    count_test_cases = sys.stdin.readline()
    for i in range(1, int(count_test_cases) + 1):
        n = int(sys.stdin.readline())
        # Keep list of all points
        points = []
        for _ in range(n):
            point = sys.stdin.readline().split()
            points.append((int(point[0]), int(point[1])))
        # Precompute distances between all pair of points
        distances = precompute_distances(points, n)
        sys.stdout.write(f'Case #{i}: {round(find_rectangles(distances))}\n')
        # Skip new line
        if i != int(count_test_cases):
            sys.stdin.readline()