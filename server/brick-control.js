const net = require('net')

class BrickControl {
    constructor() {
        // Il y aura un seul robot à la fois, on stocke un pointeur vers le client ici
        this.socket = null

    }

    static init(port) {
        const control = new BrickControl()

        const tcp = net.createServer(socket => {
            control.configureSocket(socket)
        })

        tcp.listen(port, () => {
            console.log('TCP serveur bound to :' + port)
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
            //
        })
    }

    columnToByte(position) {
        return position.charCodeAt(0) - 97
    }

    lineToByte(position) {
        return parseInt(position.charAt(1)) - 1
    }

    move(start, end, capture) {
        const buffer = Buffer.from([
            this.columnToByte(start),
            this.lineToByte(start),
            this.columnToByte(end),
            this.lineToByte(end),
            capture ? 1 : 0,
        ])

        if (!this.socket) {
            console.error('Move: No TCP socket !');
            return;
        }

        this.socket.write(buffer)
    }
}

module.exports = BrickControl
