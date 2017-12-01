#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import sys

def calculate(captcha, puzzle_version=1):
    nums = [int(digit) for digit in captcha]

    total = 0

    captcha_length = len(nums)
    for i in range(captcha_length):
        if puzzle_version == 1:
            next_idx = (i + 1)
        else:
            next_idx = i + (captcha_length // 2)

        next_idx = next_idx % captcha_length

        if nums[i] == nums[next_idx]:
            total += nums[i]

    return total

if __name__ == '__main__':
    puzzle_version = int(sys.argv[1])
    captcha_value = sys.argv[2]
    print(calculate(captcha_value, puzzle_version=puzzle_version))