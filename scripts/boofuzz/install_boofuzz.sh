#!/usr/bin/env bash
sudo apt-get install python3-pip python3-venv build-essential
python3 -m venv env
source env/bin/activate
pip install -U pip setuptools
pip install boofuzz