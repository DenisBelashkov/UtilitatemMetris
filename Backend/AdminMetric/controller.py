import os

from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.ext.automap import automap_base

from module import Module
from flask import Flask

class Controller:
	def __init__(self, app: Flask, db_key: str, list_options : list[Module]):
		self.app = app
		app.config['DEBUG'] = True
		app.config['SQLALCHEMY_DATABASE_URI'] = db_key
		app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
		app.config['CSRF_ENABLED'] = True
		app.config['SECRET_KEY'] = 'a really really really really long secret key'
		app.config['JSON_AS_ASCII'] = False
		self.db: SQLAlchemy = SQLAlchemy(app, session_options={'autoflush': False})
		Base = automap_base()
		Base.prepare(self.db.engine, reflect=True)
		for m in list_options:
			m.setOptions(app, self.db, Base)

	def run(self):
		port = int(os.environ.get('PORT', 5005))
		self.app.run(port=port,host='0.0.0.0', debug=True)