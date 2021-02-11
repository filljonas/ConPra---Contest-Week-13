import sys
import math
import time


count = 0
all_prime = True
condition_satisfied = False


# Taken from: https://www.geeksforgeeks.org/primality-test-set-1-introduction-and-school-method/
def is_prime(n):
    if n <= 1:
        return False
    if n <= 3:
        return True
    if n % 2 == 0 or n % 3 == 0:
        return False
    i = 5
    while i * i <= n:
        if n % i == 0 or n % (i + 2) == 0:
            return False
        i = i + 6
    return True


# Idea taken from: https://www.geeksforgeeks.org/distinct-permutations-string-set-2/
def permutations(s, border):
    def permutations_inner(i, s):
        global count, all_prime, condition_satisfied
        if i == len(s) - 1:
            # Convert list of string to integer
            num_as_str = ''
            num = int(num_as_str.join(s))
            if is_prime(num):
                # Prime number
                count += 1
            else:
                # We found a number which is not prime
                all_prime = False
            if count > border:
                condition_satisfied = True
            return
        prev = '*'
        for j in range(i, len(s)):
            tmp = s.copy()
            if j > i and tmp[i] == tmp[j]:
                continue
            if prev != '*' and prev == s[j]:
                continue
            # Swap elements
            t = tmp[i]
            tmp[i] = tmp[j]
            tmp[j] = t
            prev = s[j]
            # Recursive call
            permutations_inner(i + 1, tmp)
    permutations_inner(0, s)


def iterate_over_primes(k, upper):
    """
    Iterate over all numbers previously calculated. If it is a prime number, we have to consider it for further
    investigation
    """
    global count, all_prime, condition_satisfied
    for x in range(k + 1, upper + 1):
        if is_prime(x):
            # Check if enough permutations are primes
            count = 0
            all_prime = True
            condition_satisfied = False
            permutations(list(str(x)), math.log10(x))
            if all_prime or condition_satisfied:
                return x
    return -1


if __name__ == '__main__':
    count_test_cases = sys.stdin.readline()
    for i in range(1, int(count_test_cases) + 1):
        k = int(sys.stdin.readline())
        # Build table consisting of all prime numbers in the range we're interested in
        res = iterate_over_primes(k, (10 ** (int(math.log10(k)) + 1)) - 1)
        sys.stdout.write(f'Case #{i}: {res}\n')
