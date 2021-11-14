#!/usr/bin/env python
from boofuzz import *
def main():
    session = Session(
        target=Target(
            connection=TCPSocketConnection("127.0.0.1", 8080)
        ),
    )

    s_initialize(name="Request")
    with s_block("Request-Line"):
        s_static("POST /furious/movie/review")
        s_static("\r\n")
        s_static("Content-Type: application/json")
        s_static("\r\n")
        s_static("Content-Length: ")
        s_size("put body", output_format="ascii", signed=True, fuzzable=True)
        s_static("\r\n")
        s_static("\r\n")

        if s_block_start("put body"):
            s_string('"{"rate": 5,"comment": "elo"}"', name="RequestBody")
        s_block_end()
        s_static("\r\n")
    s_static("\r\n", "Request-CRLF")

    session.connect(s_get("Request"))

    session.fuzz()


if __name__ == "__main__":
    main()