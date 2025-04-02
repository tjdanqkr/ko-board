package com.example.koboard.exception

class BoardNotFoundException : RuntimeException {
    constructor() : super()
    constructor(id:Int) : super("Board(${id}) not found")
}