import Data.List

get_child_procs :: Maybe Int -> [String] -> [String]
get_child_procs Nothing = (\x -> [])
get_child_procs (Just idx) = drop idx

drop_comma :: String -> String
drop_comma str = case (last str) of
    ',' -> take (length str - 1) str
    otherwise -> str

proc_entry :: String -> (String, [String])
proc_entry line = 
    let proc_words = words line in
    let proc_name = head proc_words in
    let arrow_index = elemIndex "->" proc_words in
    let child_procs = [ drop_comma p | p <- get_child_procs arrow_index proc_words ] in
    (proc_name, child_procs)


main = do
    contents <- readFile "input.txt"
    let proc_list = lines contents
    let proc_entries = [ proc_entry l | l <- proc_list ]
    let (procs, child_procs) = unzip proc_entries
    let child_proclist = concat child_procs
    let root_procs = filter (\x -> not (elem x child_proclist)) procs
    putStrLn (head root_procs)