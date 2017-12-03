fs = require('fs');
fs.readFile('input.txt', calculate_checksum)

function difference(row) {
  var numbers = row.split(/(\s+)/).filter((s) => s.trim().length > 0).map((s) => parseInt(s, 10));
  var max = numbers.reduce((a, b) => Math.max(a, b));
  var min = numbers.reduce((a, b) => Math.min(a, b));
  return max - min;
}

function division(row) {
  var numbers = row.split(/(\s+)/).filter((s) => s.trim().length > 0).map((s) => parseInt(s, 10));
  for (var i = 0; i < numbers.length; i++) {
    n1 = numbers[i];
    for (var j = i + 1; j < numbers.length; j++) {
      n2 = numbers[j];
      if (n1 % n2 == 0) {
        return n1 / n2;
      }
      if (n2 % n1 == 0) {
        return n2 / n1;
      }
    }
  }
}

function calculate_checksum(err, data) {
  //command line arg: 1 for 1st puzzle, 2 for second
  var puzzle_version = parseInt(process.argv[2], 10); 

  var rows = data.toString().split("\n");

  if (puzzle_version == 1) {
    var diffs = rows.map(difference);
  } else {
    var diffs = rows.map(division);
  }
  var checksum = diffs.reduce((a, b) => a + b);
  console.log(checksum);
}