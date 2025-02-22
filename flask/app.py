from flask import Flask
import psycopg2

app = Flask(__name__)

def get_db_connection():
    conn = psycopg2.connect(host='db',
                            database='root',
                            user='root',
                            password='password')
    return conn

@app.route('/names')
def index():
    conn = get_db_connection()
    cur = conn.cursor()
    cur.execute('SELECT * FROM name_basics LIMIT 5;')
    names = cur.fetchall()
    cur.close()
    conn.close()
    return names

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)