import sys

if __name__ == '__main__':
    count_test_cases = sys.stdin.readline()
    for i in range(1, int(count_test_cases) + 1):
        sys.stdout.write(f'Case #{i}:\n')
        # Skip new line
        if i != int(count_test_cases):
            sys.stdin.readline()