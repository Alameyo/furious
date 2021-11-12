#!/bin/sh

docker run -p 27017-27019:27017-27019 --name mongodb mongo:4.2.3
