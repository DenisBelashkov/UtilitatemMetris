from module import Module
import smtplib
from flask import request, jsonify
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import jwt

class RegistrationModule(Module):
	def __init__(self):
		gmailUser = 'tntrol.go@gmail.com'
		gmailPassword = ''
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
			user = db.session.query(Users).filter(Users.email == r_json["email"]).one_or_none()
			if not user:
				return " ", 400
			token_user = jwt.encode({"password": r_json["password"], "email": r_json["email"]}, "secret", algorithm="HS256")
			link = "register/" + token_user + "/confirm"
			msg = MIMEMultipart()
			msg['From'] = self.gmailUser
			msg['To'] = r_json["email"]
			msg['Subject'] = "Registration"
			msg.attach(MIMEText(link))
			self.server.sendmail(self.gmailUser, r_json["email"], msg.as_string())
			return " ", 200

		@app.route('register/<token>/confirm',methods=["GET"])
		def registration_confirm(token):
			token_decode = jwt.decode(token, "secret", algorithms=["HS256"])
			user = Users()
			user.email = token_decode["email"]
			user.password = token_decode["password"]
			db.session.add(user)
			db.session.commit()

	def __del__(self):
		self.server.close()
