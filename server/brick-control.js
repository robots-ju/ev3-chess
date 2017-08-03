const net = require('net')

class BrickControl {
    constructor() {
        // Il y aura un seul robot à la fois, on stocke un pointeur vers le client ici
        this.socket = null
        this.resolveMove = null
    }

    static init(port) {
        const control = new BrickControl()

        const tcp = net.createServer(socket => {
            control.configureSocket(socket)
        })

        tcp.listen(port, () => {
            console.log('TCP serveur bound to :' + port)
        })

        tcp.on('err', err => {
            this.socket = null
            throw err
        })

        return control;
    }

    configureSocket(socket) {
        console.log('TCP client connected')

        this.socket = socket

        this.socket.on('end', () => {
            console.log('TCP client disconnected')

            this.socket = null
        })

        this.socket.on('data', data => {
            if (this.resolveMove != null) {
                this.resolveMove()
                this.resolveMove = null
            } else {
                console.log('No move to resolve')
            }
        })
    }

    columnToByte(position) {
        return position.charCodeAt(0) - 97
    }

    lineToByte(position) {
        return parseInt(position.charAt(1)) - 1
    }

    move(move) {
        return new Promise((resolve, reject) => {
            const buffer = Buffer.from([
                this.columnToByte(move.from),
                this.lineToByte(move.from),
                this.columnToByte(move.to),
                this.lineToByte(move.to),
                move.capture ? 1 : 0,
            ])

            if (!this.socket) {
                reject('No TCP socket')
                return
            }

            this.socket.write(buffer)
            this.resolveMove = resolve
        })
    }
}

module.exports = BrickControl
