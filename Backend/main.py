from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import and_
from werkzeug.middleware.proxy_fix import ProxyFix
from sqlalchemy.ext.automap import automap_base
from datetime import date
from datetime import datetime
from flat_module import FlatModule
from metric_module import MetricModule
from payment_modul import PaymentModule
from controller import Controller

app = Flask(__name__)


if __name__ == '__main__':
	list_m = [MetricModule(), FlatModule(), PaymentModule()]
	c = Controller(app, 'mysql+pymysql://tntrol:password@localhost:3306/mytp', list_m)
	c.run()