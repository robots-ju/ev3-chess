const net = require('net')

const port = 4000
const responseTime = 3000

const tcp = net.createServer(socket => {
    socket.on('data', data => {
        console.log('Got packet', data)
        setTimeout(() => {
            console.log('Sending response')
            socket.write(Buffer.from([1]))
        }, responseTime)
    })
})

tcp.listen(port, () => {
    console.log('TCP server bound to :' + port)
})
