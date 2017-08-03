const process = require('process')
const http = require('http')
const io = require('socket.io')

const ChessEngine = require('./chess-engine')
const httpServer = http.createServer()
const BrickControl = require('./brick-control')

; (async () => {
    // CHESS
    const chessEngine = await ChessEngine.init()
    const brickControl = BrickControl.init(4000)

    // HTTP
    httpServer.on('request', (req, res) => {
        res.writeHead(200, { 'Content-Type': 'text/plain' })
        res.end('RobotsJU')
    })
    httpServer.listen(8080)

    // SOCKET.IO
    const socket = io.listen(httpServer);
    
    socket.on('connection', client => {
        client.emit('gameChange', {
            fen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
        })
        client.on('playerMove', async ({ move }) => {
            console.log('Player moved')
            client.emit('gameChange', { fen: await chessEngine.playerMove(move) })
            
            // ROBOT PLAYS AFTER THE PLAYER
            const fen = await chessEngine.computeRobotMove()
            client.emit('gameChange', { fen })
        })
        console.log('Client connected')
    })
})()
