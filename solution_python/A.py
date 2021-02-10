import sys


def total_numbers_soda(empty, c):
    """
    Returns the total numbers of soda Tim can drink
    :param empty: current number of empty bottles in Tim's possession
    :return:
    """
    # In the beginning, Tim hasn't drunken any sodas
    count = 0
    while True:
        # Sodas Tim can exchange in the current step
        new_soda = empty // c
        if new_soda == 0:
            # Tim can't afford any new soda
            return count
        count += empty // c
        # Adjust number of empty bottles (New soda he immediately drank + soda bottles he couldn't use in the last step)
        empty = new_soda + (empty % c)


if __name__ == '__main__':
    count_test_cases = sys.stdin.readline()
    for i in range(1, int(count_test_cases) + 1):
        first_line = sys.stdin.readline().split()
        # Initial number of empty bottles
        e = int(first_line[0])
        # Number of empty soda bottles found during the day
        f = int(first_line[1])
        # Number of bottles required to buy a new soda
        c = int(first_line[2])
        sys.stdout.write(f'Case #{i}: {total_numbers_soda(e + f, c)}\n')