const path = require('path')
const Engine = require('node-uci').Engine
const os = require('os')
const Chess = require('chess.js').Chess

class ChessEngine {

    constructor() {
        this.chess = new Chess()
    }

    static async init() {
        const ce = new ChessEngine()
        // Init engine
        ce.engine = new Engine(path.join(__dirname, 'stockfish', 'stockfish_8_x64'))
        await ce.engine.init()

        // Config
        await ce.engine.setoption('Threads', `${os.cpus().length}`)
        // DOU U WANT THIS ROBOT TO BE A DUMB ASS?
        // await ce.engine.setoption('Skill Level', `0`)

        // Wait for ready
        await ce.engine.isready()
        console.log('Chess engine ready !')

        return ce
    }

    async playerMove(move) {
        this.chess.move(move)
        return this.chess.fen()
    }

    async computeRobotMove() {
        const fen = await this.chess.fen()
        if (fen !== undefined)
            await this.engine.position(fen)
        const startComputeTime = Date.now()
        const result = await this.engine.go({ nodes: 2500000 })
        console.log(`Robot played in ${(Date.now() - startComputeTime) / 1000}s`)
        this.chess.move(result.bestmove, { sloppy: true })
        return this.chess.fen()
    }

    async kill() {
        return this.engine.quit()
    }
}

module.exports = ChessEngine