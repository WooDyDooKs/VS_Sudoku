#!/bin/sh
HOST="sudoku@sudoku.gebaschtel.ch"
ant
echo "put sudoku.jar /home/sudoku/sudoku.jar" | sftp -q -b - $HOST
echo "Restarting sudoku service"
ssh $HOST sudo systemctl restart sudoku
