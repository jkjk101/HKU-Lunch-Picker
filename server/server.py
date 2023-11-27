import flask
import sqlite3
from flask import request
from datetime import datetime

app = flask.Flask(__name__)

@app.route('/get', methods=['GET'])
def get():
    con = sqlite3.connect('hkulunchpicker-db.db')
    cursor = con.execute("SELECT * from RESTAURANTS;")

    all = []
    for row in cursor:
        all.append(
            {
                'restaurant_id': row[0],
                'restaurant_name': row[1],
                'category': row[2],
                'price': row[3],
                'distance': row[4],
                'last_posted': row[5]
            }
        )
    con.close()

    outdata = {
        'data': all
    }

    return outdata

@app.route('/feature', methods=['GET'])
def feature():
    id = request.args.get('id', '')
    restaurant_id = int(id) + 1

    current_time = datetime.now()
    time_string = current_time.strftime('%Y-%m-%d %H:%M:%S')

    con = sqlite3.connect('hkulunchpicker-db.db')

    con.execute("UPDATE RESTAURANTS SET last_posted = ? WHERE restaurant_id = ?", (time_string, restaurant_id))
    con.commit()

    con.close()
    return {}


# adds host="0.0.0.0" to make the server publicly available
app.run(host="0.0.0.0")