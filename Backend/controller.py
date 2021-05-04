from module import Module
from flask import Flask

class Controller:
	def __init__(self, app: Flask, db_key: str, list_optoins : list[Module]):
		self.app = app
		for m in list_optoins:
			m.setOptions(app)

	def run(self):
		self.app.run(debug=True)