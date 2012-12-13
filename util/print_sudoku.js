db = db.getSiblingDB("vs_sudoku");

function print_solution(id) {
    var ROW_SEP = "-------------------------------";

    for(var row=0; row<9; row++) {
        if(row%3 == 0) print(ROW_SEP);
        var line = "";
    
        for(var col=0; col<9; col++) {
            var cell = db.solutions.cells.findOne({ 
                "solution_id" : id,
                "row" : row+1,
                "column" : col+1
            });
            
            if(col%3 == 0) line += "|";
            
            if(cell.clue) {
                line += "(" + cell.value + ")";
            } else {
                line += " " + cell.value + " ";
            }
        }
        
        print(line + "|");
    }
    print(ROW_SEP);
}

print_solution(id);
