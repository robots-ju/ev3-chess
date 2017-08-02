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
        await ce.engine.setoption('Minimum Thinking Time', '1000')

        // Wait for ready
        await ce.engine.isready()
        console.log('Chess engine ready !')

        return ce
    }

    async playerMove(move) {
        this.chess.move(move)
        return this.chess.fen()
    }

    async computeRobotMove(fen) {
        return new Promise(async resolve => {
            if (fen !== undefined)
                await this.engine.position(fen)
            const result = await this.engine.go({ nodes: 2500000 })
            resolve(result.bestmove)
        })
    }

    async kill() {
        return this.engine.quit()
    }
}

module.exports = ChessEngine