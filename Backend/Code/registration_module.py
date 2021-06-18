from module import Module
import smtplib
from flask import request, jsonify
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import jwt
from decoration import wrapper_for_token


class RegistrationModule(Module):
	def __init__(self):
		self.gmailUser = 'usertest1887@gmail.com'
		self.gmailPassword = 'UserTestPa$$w0rd'

	def setOptions(self, app, db, base):
		Users = base.classes.Users

		def send_all(from_, name, msg):
			try:
				mailServer = smtplib.SMTP_SSL('smtp.gmail.com', 465)
				mailServer.login(self.gmailUser, self.gmailPassword)
				mailServer.sendmail(from_, name, msg)
				mailServer.quit()
				return True
			except Exception as e:
				return False

		@app.route('/register/user', methods=["POST"])
		def registration_user():
			r_json = request.json
			user = db.session.query(Users).filter(Users.email == r_json["email"]).filter(Users.demo == 1).one_or_none()
			if user:
				return " ", 409
			token_user = jwt.encode({"password": r_json["password"], "email": r_json["email"]}, "secret",
									algorithm="HS256")
			link = "https://sleepy-savannah-37295.herokuapp.com/register/" + token_user + "/confirm"
			msg = MIMEMultipart()
			msg['From'] = self.gmailUser
			msg['To'] = r_json["email"]
			msg['Subject'] = "Registration"
			msg.attach(MIMEText(link))
			if send_all(self.gmailUser, r_json["email"], msg.as_string()):
				return " ", 200
			return " ", 500

		@app.route('/register/<token>/confirm', methods=["GET"])
		def registration_confirm(token):
			try:
				token_decode = jwt.decode(token, "secret", algorithms=["HS256"])
				user = db.session.query(Users).filter(Users.email == token_decode["email"]).filter(Users.demo == 1).one_or_none()
				if user:
					return " ", 403
				user = db.session.query(Users).filter(Users.email == token_decode["email"]).filter(Users.demo == 0).one_or_none()
				if user is None:
					user = Users()
					user.email = token_decode["email"]
					user.password = token_decode["password"]
					user.demo = 1
					db.session.add(user)
				else:
					user.password = token_decode["password"]
					user.demo = 1
				db.session.commit()
				return "Successfully registration!"
			except Exception as e:
				return  " ", 400
			return " ", 400

		@app.route('/register/continue', methods=["POST"])
		@wrapper_for_token
		def registration_continue():
			token_decode = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
			user = db.session.query(Users).filter(Users.email == token_decode["email"]).filter(Users.demo == 1).one_or_none()
			if user:
				return " ", 409
			user = db.session.query(Users).filter(Users.email == token_decode["email"]).first()
			user.password = request.json["password"]
			user.demo = 1
			db.session.commit()
			return " ", 200


	def __del__(self):
		# self.server.close()
		pass
