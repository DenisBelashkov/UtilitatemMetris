from module import Module
from flask import jsonify
from sqlalchemy.ext.automap import automap_base

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
		@app.route('/flat/<user_id>', methods=["GET"])
		def get_flats_by_id_user(user_id):
			try:
				id = int(user_id)
				flats = db.session.query(Flat.id_personal_account, Address).join(Address,
																				 Address.id_adress == Flat.id_adress).filter(
					Flat.id_owner_user == id).all()
				if len(flats) > 0:
					out = []
					for f in flats:
						out.append({"id": f.id_personal_account, "address": self.toString(f.Address)})
					return jsonify(out)
				return " ", 404
			except Exception as e:
				print(e)
			return " ", 400
