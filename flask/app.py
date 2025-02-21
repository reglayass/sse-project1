from flask import Flask, request
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)


@app.before_request
def log_request_info():
    app.logger.info(f"Received request: {request.method} {request.url}")


@app.route('/')
def home():
    return "Hello, World!"


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)