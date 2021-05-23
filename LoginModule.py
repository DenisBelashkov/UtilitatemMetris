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
			return jsonify({"token": token, "id": user.id_user})

		@app.route("/login/quick", methods=["POST"])
		def login_quick():
			r_json = request.json
			user = db.session.query(User).filter(r_json["email"] == User.email).first()
			if user:
				token = jwt.encode({"id": user.id_user, "type": "demo", "email": user.email}, "secret",
								   algorithm="HS256")
				return jsonify({"token": token, "id": user.id_user})
			token = jwt.encode({"id": -1, "type": "na", "email": r_json["email"]}, "secret",
							   algorithm="HS256")
			return jsonify({"token": token, "id": -1})