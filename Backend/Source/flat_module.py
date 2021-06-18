import jwt

from module import Module
from flask import jsonify, request
from decoration import wrapper_for_token

class FlatModule(Module):
	def toString(self, a):
		str1 = ""
		while True:
			str1 += str(a.name)
			if not a.id_include:
				return str1
			a = a.address

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Address = base.classes.Address


		@app.route('/flat', methods=["GET"])
		@wrapper_for_token
		def get_flats_by_id_user():
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				id = decode_token["id"]
				flats = db.session.query(Flat.id_personal_account, Address).join(Address,
																				 Address.id_address == Flat.id_address).filter(
					Flat.id_owner_user == id).all()
				if len(flats) > 0:
					out = []
					for f in flats:
						out.append({"id": f.id_personal_account, "address": self.toString(f.Address)})
					return jsonify(out)
				return jsonify([])
			except Exception as e:
				print(e)
				return " ", 500
