type direction = UP | DOWN | LEFT | RIGHT
type coord = int * int

module Mem = Map.Make(struct
    type t = coord
    let compare = Pervasives.compare
end)

let add_sum mem (x, y) =
    let add_square current_sum coord =
        match Mem.find_opt coord mem with
        | None -> current_sum
        | Some n -> current_sum + n in
    let adj = [-1; 0 ; 1] in
    let adj_coords = List.map (fun i -> List.map (fun j -> x + i, y + j) adj) adj 
    |> List.concat
    |> List.filter (fun i -> i <> (x,y)) in
    List.fold_left add_square 0 adj_coords

let find_next_coord mem (x, y) = function
    | UP -> (* go up unless left is empty; then turn *)
        if Mem.mem (x - 1, y) mem then (x, y + 1), UP else (x - 1, y), LEFT
    | DOWN -> (* go down unless right is empty; then turn *)
        if Mem.mem (x + 1, y) mem then (x, y - 1), DOWN else (x + 1, y), RIGHT
    | LEFT -> (* go left unless down is empty; then turn *)
        if Mem.mem (x, y - 1) mem then (x - 1, y), LEFT else (x, y - 1), DOWN
    | RIGHT -> (* go right unless up is empty; then turn *)
        if Mem.mem (x, y + 1) mem then (x + 1, y), RIGHT else (x, y + 1), UP 

let input_val = int_of_string Sys.argv.(1)

let memory_bank = Mem.add (0, 0) 1 Mem.empty


let rec find_answer mem coord direction =
    let latest_sum = add_sum mem coord in
    if latest_sum > input_val then latest_sum else
    let mem' = Mem.add coord latest_sum mem in
    let coord', direction' = find_next_coord mem coord direction in
    find_answer mem' coord' direction'

let answer = find_answer memory_bank (1, 0) UP

let _ = Printf.printf "%d\n" answer
