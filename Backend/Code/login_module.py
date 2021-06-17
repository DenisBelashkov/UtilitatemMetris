from module import Module
from flask import request, jsonify
import jwt

class LoginModule(Module):
	def setOptions(self, app, db, base):
		User = base.classes.Users
		@app.route("/login/user", methods=["POST"])
		def login_user():
			r_json = request.json
			user = db.session.query(User).filter(r_json["email"] == User.email).filter(r_json["password"] == User.password).first()
			if not user:
				return " ", 404
			token = jwt.encode({"id": user.id_user, "type": "user", "email": user.email}, "secret", algorithm="HS256")

			return jsonify({"token": token})

		@app.route("/login/quick", methods=["POST"])
		def login_quick():
			r_json = request.json
			user = db.session.query(User).filter(r_json["email"] == User.email).filter(User.demo == 0).first()
			if user:
				token = jwt.encode({"id": user.id_user, "type": "demo", "email": user.email}, "secret",
								   algorithm="HS256")
				return jsonify({"token": token, "id": user.id_user})
			user = User()
			user.email = r_json["email"]
			user.password = "e"
			user.demo = 0
			db.session.add(user)
			db.session.commit()
			token = jwt.encode({"id": user.id_user, "type": "demo", "email": r_json["email"]}, "secret",
							   algorithm="HS256")
			return jsonify({"token": token})