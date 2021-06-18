from flask import Flask, request, jsonify
from flat_module import FlatModule
from metric_module import MetricModule
from payment_modul import PaymentModule
from controller import Controller
from registration_module import RegistrationModule
from login_module import LoginModule
#from admin_module import AdminModule

import jwt
import smtplib
app = Flask(__name__)


if __name__ == '__main__':
	#s = "mysql+pymysql://tntrol:password@localhost:3306/mytp2"
	s = "mysql+pymysql://i7lukqqtwtt3csbt:nn40pafo0k3ephki@u3r5w4ayhxzdrw87.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/jals87gr7rk8l2sk"
	list_m = [MetricModule(), FlatModule(), PaymentModule(), LoginModule(), RegistrationModule()]
	c = Controller(app, s, list_m)
	#mysql+pymysql://tntrol:password@localhost:3306/mytp
	# 	#mysql+pymysql://i7lukqqtwtt3csbt:nn40pafo0k3ephki@u3r5w4ayhxzdrw87.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/jals87gr7rk8l2sk


	#encoded_jwt = jwt.encode({"some": "payload"}, "secret", algorithm="HS256")
	#print(encoded_jwt)
	#print(jwt.decode(encoded_jwt, "secret", algorithms=["HS256"]))
	c.run()