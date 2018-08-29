Implementation of the [TIME](https://tools.ietf.org/html/rfc868) protocol, it sends a message, which contains a 
32-bit integer, without receiving any requests and closes the connection once the message is sent.

See [Writing a Time Server](https://netty.io/wiki/user-guide-for-4.x.html#wiki-h3-9) 
in the official documentation.  

Because we are going to ignore any received data but to send a message as soon as a connection is established, 
we cannot use the channelRead() method this time. Instead, we should override the channelActive() method.
