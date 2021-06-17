from module import Module
from flask import request, jsonify
from sqlalchemy import and_
import jwt
from decoration import wrapper_for_token

def address_to_string(a):
	str1 = ""
	while True:
		str1 += a.type_address.name + ": " + str(a.name) + ", "
		if not a.id_include:
			return str1
		a = a.address


class MetricModule(Module):

	@staticmethod
	def make_json(metrics):
		res_list = []
		for m in metrics:
			res_list.append({"id": m.id,
							 "balance": float(m.balance),
							 "typeMetric": m.typeMetric,
							 "prevValue": float(m.prev_value),
							 "currValue": float(m.curr_value),
							 "tariff": float(m.tariff),
							 "address": address_to_string(m.Address)})
		return res_list

	@staticmethod
	def make_json_with_save(metric, save):
		return {"metric": metric, "isSaved": save}

	@staticmethod
	def make_json_with_address(metrics, address):
		res_list = []
		str_address = address_to_string(address)
		for m in metrics:
			res_list.append({"id": m.id,
							 "balance": float(m.balance),
							 "typeMetric": m.typeMetric,
							 "prevValue": float(m.prev_value),
							 "currValue": float(m.curr_value),
							 "tariff": float(m.tariff),
							 "address": str_address})
		return res_list

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics
		User_metrics = base.classes.User_metrics
		Address = base.classes.Address

		@app.route('/metrics/update', methods=["PUT"])
		@wrapper_for_token
		def update_metric():
			try:
				token = request.headers["token"]
				token_decode = jwt.decode(token, "secret", algorithms=["HS256"])
				r_json = request.json
				metric = db.session.query(Metrics).join(Flat,
														Flat.id_personal_account == Metrics.id_personal_account).filter(
					Flat.id_owner_user == token_decode["id"]).filter(Metrics.identifier == r_json['identifier']).one_or_none()
				if not metric:
					return "", 404
				if metric.curr_value > r_json['currentValue']:
					return "", 400
				metric.curr_value = r_json['currentValue']
				db.session.commit()
				return "", 200
			except Exception as e:
				db.session.rollback()
				return "", 500

		# @app.route('/metrics/byUserId/<user_id>', methods = ["GET"])
		# @wrapper_for_token
		# def get_all_metric_of_user(user_id):
		# 	try:
		# 		id = int(user_id)
		# 		metrics = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value,
		# 								   Metrics.curr_value, Tariff.price.label("tariff"),
		# 								   Type_metric.name.label("typeMetric")) \
		# 			.join(Tariff, Tariff.id_tariff == Metrics.id_tariff) \
		# 			.join(Type_metric, Type_metric.id_type == Metrics.id_type) \
		# 			.join(Flat, Flat.id_personal_account == Metrics.id_personal_account) \
		# 			.filter(Flat.id_owner_user == id).all()
		# 		if len(metrics) > 0:
		# 			return jsonify(self.make_json(metrics))
		# 	except Exception as e:
		# 		return " ", 404

		@app.route('/metrics/byFlatId/<flat_id>', methods=["GET"])
		@wrapper_for_token
		def get_metrics_by_id_flat(flat_id):
			try:
				metrics = db.session.query(Metrics.id_metrics.label("id"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric"))\
					.join(Tariff,Tariff.id_tariff == Metrics.id_tariff)\
					.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
					.filter(Metrics.id_personal_account == flat_id).all()
				if len(metrics) > 0:
					address = db.session.query(Address).join(Flat, and_(Flat.id_address == Address.id_address, Flat.id_personal_account == flat_id)).first()
					return jsonify(self.make_json_with_address(metrics, address))
			except Exception as e:
				return " ", 400
			return jsonify([])

		@app.route('/metrics/byId/<identifier>', methods=["GET"]) # testing
		@wrapper_for_token
		def get_metric_identifier(identifier):
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				metric = db.session.query(Metrics.identifier.label("id"),Metrics.id_metrics.label("idf"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric"), Address)\
					.join(Tariff,Tariff.id_tariff == Metrics.id_tariff)\
					.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
					.join(Flat,  Flat.id_personal_account == Metrics.id_personal_account)\
					.join(Address, Flat.id_address == Address.id_address)\
					.filter(Metrics.identifier == identifier)\
					.all()
				if len(metric) > 0:
					save = db.session.query(User_metrics).filter(User_metrics.id_metrics == metric[0].idf).filter(User_metrics.id_user == decode_token["id"]).one_or_none()
					return jsonify( self.make_json_with_save(self.make_json(metric)[0], save is not None))
			except Exception as e:
				print(e)
				return " ", 500
			return " ", 404

		@app.route('/metrics/byUser', methods=["GET"])
		@wrapper_for_token
		def get_metrics_by_user():
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				metrics = db.session.query(Metrics.identifier.label("id"), Metrics.balance, Metrics.prev_value,
										   Metrics.curr_value, Tariff.price.label("tariff"),
										   Type_metric.name.label("typeMetric"), Address)\
					.join(Tariff,Tariff.id_tariff == Metrics.id_tariff)\
					.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
					.join(User_metrics, and_(User_metrics.id_user == decode_token["id"], User_metrics.id_metrics == Metrics.id_metrics))\
					.join(Flat, Flat.id_personal_account == Metrics.id_personal_account)\
					.join(Address, Address.id_address == Flat.id_address)\
					.all()
				if len(metrics) > 0:
					return jsonify(self.make_json(metrics))
				return jsonify([])
			except Exception as e:
				return " ", 500

		@app.route('/metrics/<identifier>', methods=['POST'])
		@wrapper_for_token
		def post_bind_metrics_to_user(identifier):
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				user = db.session.query(User_metrics).join(Metrics, and_(User_metrics.id_metrics == Metrics.id_metrics,
																		 Metrics.identifier == identifier)).filter(User_metrics.id_user == decode_token["id"]).first()
				if user:
					return " ", 409
				user = User_metrics()
				metric = db.session.query(Metrics.id_metrics).filter(Metrics.identifier == identifier).first()
				user.id_metrics = metric.id_metrics
				user.id_user = decode_token["id"]
				db.session.add(user)
				db.session.commit()
				return " ", 200
			except Exception as e:
				print(e)
				return " ", 400

		@app.route('/metrics/<identifier>', methods=['DELETE'])
		@wrapper_for_token
		def delete_bind_metrics_to_user(identifier):
			try:
				decode_token = jwt.decode(request.headers["token"], "secret", algorithms=["HS256"])
				user = db.session.query(User_metrics).join(Metrics, and_(User_metrics.id_metrics == Metrics.id_metrics,
																		 Metrics.identifier == identifier)).filter(
					User_metrics.id_user == decode_token["id"]).first()
				if not user:
					return " ", 404
				db.session.delete(user)
				db.session.commit()
				return " ", 200
			except Exception as e:
				return " ", 400