const net = require('net')

const port = 4000
const responseTime = 3000

const socket = net.createConnection({port}, () => {
    console.log('connecting to server on :' + port)
})

socket.on('data', data => {
    console.log('Got packet', data)
    setTimeout(() => {
        console.log('Sending response')
        socket.write(Buffer.from([1]))
    }, responseTime)
})
