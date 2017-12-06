#!/usr/bin/env ruby

maze = File.readlines("maze.txt").map { |s| s.to_i }

steps = 0
current_step = 0

#run the maze
while current_step < maze.length and current_step >= 0
    steps += 1
    jump_val = maze[current_step]
    if jump_val >= 3 then
        maze[current_step] -= 1
    else
        maze[current_step] += 1
    end
    current_step += jump_val
end

puts steps