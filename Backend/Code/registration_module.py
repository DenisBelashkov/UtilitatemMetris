from module import Module
import smtplib
from flask import request, jsonify
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import jwt
from decoration import wrapper_for_token


class RegistrationModule(Module):
	def __init__(self):
		gmailUser = 'usertest1887@gmail.com'
		gmailPassword = 'UserTestPa$$w0rd'
		mailServer = smtplib.SMTP('smtp.gmail.com', 587)
		mailServer.ehlo()
		mailServer.starttls()
		mailServer.ehlo()
		mailServer.login(gmailUser, gmailPassword)
		self.server = mailServer
		self.gmailUser = gmailUser

	def setOptions(self, app, db, base):
		Users = base.classes.Users

		@app.route('/register/user ', methods=["POST"])
		def registration_user():
			r_json = request.json()
			user = db.session.query(Users).filter(Users.email == r_json["email"]).filter(Users.demo == 1).one_or_none()
			if user:
				return " ", 409
			token_user = jwt.encode({"password": r_json["password"], "email": r_json["email"]}, "secret",
									algorithm="HS256")
			link = "register/" + token_user + "/confirm"
			msg = MIMEMultipart()
			msg['From'] = self.gmailUser
			msg['To'] = r_json["email"]
			msg['Subject'] = "Registration"
			msg.attach(MIMEText(link))
			self.server.sendmail(self.gmailUser, r_json["email"], msg.as_string())
			return " ", 200

		@app.route('/register/<token>/confirm', methods=["GET"])
		def registration_confirm(token):
			try:
				token_decode = jwt.decode(token, "secret", algorithms=["HS256"])
				user = Users()
				user.email = token_decode["email"]
				user.password = token_decode["password"]
				db.session.add(user)
				db.session.commit()
				return " ", 200
			except Exception as e:
				return  " ", 400
			return " ", 400

		@app.route('/register/continue', methods=["POST"])
		@wrapper_for_token
		def registration_continue():
			token_decode = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
			user = db.session.query(Users).filter(Users.email == token_decode["email"]).filter(Users.demo == 1).first()
			if user:
				return " ", 409
			user = db.session.query(Users).filter(Users.email == token_decode["email"]).first()
			user.password = request.json["password"]
			user.demo = 1
			db.session.commit()
			return " ", 200


	def __del__(self):
		self.server.close()
