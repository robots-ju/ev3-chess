const process = require('process')
const http = require('http')
const io = require('socket.io')

const ChessEngine = require('./chess-engine')
const httpServer = http.createServer()
const BrickControl = require('./brick-control')

; (async () => {
    // CHESS
    const chessEngine = await ChessEngine.init()
    const brickControl = BrickControl.init('192.168.0.26', 4000)

    // HTTP
    httpServer.on('request', (req, res) => {
        res.writeHead(200, { 'Content-Type': 'text/plain' })
        res.end('RobotsJU')
    })
    httpServer.listen(8080)

    // SOCKET.IO
    const socket = io.listen(httpServer);
    
    const doBrickMoves = async () => {
            for (let move of chessEngine.brickMoves()) {
                console.log('Sending move to brick', move)
                try {
                    await brickControl.move(move)
                } catch(err) {
                    console.log('Brick move error', err)
                }
                console.log('Brick is finished')
            }
    }

    socket.on('connection', client => {
        client.emit('gameChange', {
            fen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1'
        })
        client.on('playerMove', async ({ move }) => {
            console.log('Player moved')
            client.emit('gameChange', { fen: await chessEngine.playerMove(move) })

            await doBrickMoves()

            // ROBOT PLAYS AFTER THE PLAYER
            const fen = await chessEngine.computeRobotMove()
            client.emit('gameChange', { fen })

            await doBrickMoves()
        })
        console.log('Client connected')
    })
})()
