from flask import Flask, request, jsonify
from flat_module import FlatModule
from metric_module import MetricModule
from payment_modul import PaymentModule
from controller import Controller

from LoginModule import LoginModule

import jwt
import smtplib
app = Flask(__name__)


if __name__ == '__main__':
	list_m = [MetricModule(), FlatModule(), PaymentModule(), LoginModule()]
	c = Controller(app, 'mysql+pymysql://tntrol:password@localhost:3306/mytp', list_m)

	#encoded_jwt = jwt.encode({"some": "payload"}, "secret", algorithm="HS256")
	#print(encoded_jwt)
	#print(jwt.decode(encoded_jwt, "secret", algorithms=["HS256"]))
	c.run()