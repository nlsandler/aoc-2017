let square n = n * n

let get_ring_end ring_dim = square(ring_dim)

let get_ring_start ring_dim = 
    if ring_dim == 1 then 1 else
    let end_of_previous = get_ring_end (ring_dim - 2) in
    end_of_previous + 1

let calculate_distance square_id =
    if square_id == 1 then 0 else
    (* square_id is in NXN concentric ring *)
    let n = square_id |> float |> sqrt |> ceil |> int_of_float in
    let n = if (n mod 2 == 0) then n + 1 else n in
    let ring_start = get_ring_start n in
    let index_from_ring_start = square_id - ring_start + 1 in
    let index_from_ring_end = ((get_ring_end n) - square_id) in
    let distance_from_corner_1 = index_from_ring_start mod (n - 1) in
    let distance_from_corner_2 = index_from_ring_end mod (n - 1) in
    let distance_from_corner = min distance_from_corner_1 distance_from_corner_2 in
    let ring_number = (n - 1) / 2 in
    (2 * ring_number) - distance_from_corner

let dist = calculate_distance (int_of_string Sys.argv.(1))

let _ = Printf.printf "%d\n" dist