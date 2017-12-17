(ns aoc.solve)

(def instructions
  (clojure.string/split-lines
   (slurp "input.txt")))

(defn parse-op
  "turn comparison string into clojure function"
  [op-str]
  (case op-str
    "!=" not=
    (eval (read-string op-str))))

(defn parse-instruction
  "parse an instruction into a useful map"
  [instruction]
  (let [inst (clojure.string/split instruction #" ")]
    {:reg (first inst)
     :op (nth inst 1)
     :op-val (eval (read-string (nth inst 2)))
     :cond-reg (nth inst 4)
     :cond-op (parse-op (nth inst 5))
     :cond-val (eval (read-string (nth inst 6)))}))

(defn cond-true
  "determine if condition part of instruction is true"
  [instruction reg-map]
  (let [cond-reg (get reg-map (:cond-reg instruction) 0)]
    ((:cond-op instruction) cond-reg (:cond-val instruction))))

(defn update-regs
  "update reg-map based on instruction (assumes cond is met)"
  [instruction reg-map]
  (let [reg (:reg instruction)
        reg-val (get reg-map reg 0)
        op (:op instruction)
        op-val (:op-val instruction)
        new-val (case op
                  "inc" (+ reg-val op-val)
                  "dec" (- reg-val op-val))]
    (assoc reg-map reg new-val)))


(defn process-inst
  "update reg-map according to a given instruction"
  [instruction-str reg-map]
  (let [instruction (parse-instruction instruction-str)]
    (if (cond-true instruction reg-map)
      (update-regs instruction reg-map)
      reg-map)))

(defn reg-max
  [reg-map]
  (if (empty? reg-map)
    0
    (apply max (vals reg-map))))

(defn print-maxes
  [current-max running-max]
  (println "final max:" current-max)
  (println "overall max:" running-max))

(defn process
  "process a list of instructions, updating reg_map as needed"
  [instructions]
  (loop [remaining-instructions instructions
         reg-map {}
         running-max 0]
    (if (empty? remaining-instructions)
      (print-maxes (reg-max reg-map) running-max)
      (recur (rest remaining-instructions)
             (process-inst (first remaining-instructions) reg-map)
             (max running-max (reg-max reg-map))))))

(defn print-max
  "print maximum register value from map"
  [reg-map]
  (println (apply max (vals reg-map))))

(defn -main
  "Put it all together" [] (process instructions))
;  (let [final-regs (process instructions)]
;    (print-max final-regs)))
