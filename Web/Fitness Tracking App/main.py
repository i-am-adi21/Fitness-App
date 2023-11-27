from flask import *

from public import public
from mainadmin import mainadmin
from admin import admin
from api import api

app=Flask(__name__)
app.secret_key="diet"

app.register_blueprint(public)
app.register_blueprint(mainadmin,url_prefix='/mainadmin')
app.register_blueprint(admin,url_prefix='/admin')

app.register_blueprint(api, url_prefix='/api')
# api=192.168.43.230
app.run(debug="true", host="0.0.0.0", port=5673)